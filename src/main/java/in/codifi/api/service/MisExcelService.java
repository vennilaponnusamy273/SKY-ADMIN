package in.codifi.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import in.codifi.api.config.ApplicationProperties;
import in.codifi.api.entity.ApplicationUserEntity;
import in.codifi.api.entity.ReferralEntity;
import in.codifi.api.repository.ApplicationUserRepository;
import in.codifi.api.repository.BackOfficeApiRepository;
import in.codifi.api.repository.DocumentEntityRepository;
import in.codifi.api.repository.ReferralRepository;
import in.codifi.api.request.model.MISModel;
import in.codifi.api.service.spec.IMisExcelService;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.DateUtil;
import in.codifi.api.utilities.EkycConstants;

@ApplicationScoped
public class MisExcelService implements IMisExcelService {
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static final Logger logger = LogManager.getLogger(MisExcelService.class);
	@Inject
	CommonMethods commonMethods;
	@Inject
	ApplicationUserRepository applicationUserRepository;
	@Inject
	BackOfficeApiRepository backOfficeApiRepository;
	@Inject
	ReferralRepository notifyRepository;
	@Inject
	DocumentEntityRepository docrepository;
	@Inject
	ApplicationProperties props;

	@Override
	public Response ExcelDownload1(String frmDate, String toDate) {
	    try {
	        List<MISModel> misModels = prepareMISModels(frmDate, toDate);
	        for (MISModel model : misModels) {
	            System.out.println("Date: " + model.getDate());
	            System.out.println("Referral: " + model.getReferral());
	            System.out.println("TotalAccOpen: " + model.getTotalAccOpen());
	            System.out.println("TotalAccEsign: " + model.getTotalAccEsign());
	            System.out.println("TotalAccBo: " + model.getTotalAccBo());
	            System.out.println("---------------------------------");
	        }
	        String filePath = generateExcelSheet(misModels);
	        File file = new File(filePath);
	        return Response.ok(file)
	                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
	                .build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.serverError().entity("An error occurred: " + e.getMessage()).build();
	    }
	}
	public List<MISModel> prepareMISModels(String frmDate, String toDate) {
	    List<MISModel> misModels = new ArrayList<>();
	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    LocalDate fromDate = LocalDate.parse(frmDate, inputFormatter);
	    LocalDate toDateObj = LocalDate.parse(toDate, inputFormatter);

	    // Get existingNotifyEntity for the specified date range
	    List<ReferralEntity> existingNotifyEntity = getExistingNotifyEntity(frmDate, toDate);

	    // Create a map to store MISModel for each referral and date
	    Map<String, Map<String, MISModel>> referralDateMap = new HashMap<>();

	    // Iterate over each referral entity
	    for (ReferralEntity referral : existingNotifyEntity) {
	        // Convert referral date to LocalDate
	        LocalDate referralDate = referral.getCreatedOn().toInstant()
	                .atZone(ZoneId.systemDefault())
	                .toLocalDateTime()
	                .toLocalDate();
	        
	        // Format referral date as dd-MM-yyyy
	        String formattedReferralDate = referralDate.format(inputFormatter);

	        // Get or create the map for the referral for the current date
	        Map<String, MISModel> referralMap = referralDateMap.computeIfAbsent(formattedReferralDate, k -> new HashMap<>());

	        // Get or create MISModel for the referral for the current date
	        MISModel misModel = referralMap.computeIfAbsent(referral.getRefByName(), k -> {
	            MISModel newModel = new MISModel();
	            newModel.setDate(formattedReferralDate); // Set the date
	            newModel.setReferral(referral.getRefByName()); // Set the referral name
	            newModel.setTotalAccOpen("0"); // Initialize to "0"
	            newModel.setTotalAccEsign("0"); // Initialize to "0"
	            newModel.setTotalAccBo("0"); // Initialize to "0"
	            return newModel;
	        });

	        // Increment counts based on status
	        ApplicationUserEntity userEntity = applicationUserRepository.findByMobileNo(referral.getMobileNo());
	        if (userEntity != null) {
	            String status = determineStatus(userEntity, referral);
	            if ("In Progress".equals(status)) {
	                misModel.setTotalAccOpen(String.valueOf(Integer.parseInt(misModel.getTotalAccOpen()) + 1));
	            } else if ("Submitted".equals(status)) {
	                misModel.setTotalAccEsign(String.valueOf(Integer.parseInt(misModel.getTotalAccEsign()) + 1));
	            } else if ("Completed".equals(status)) {
	                misModel.setTotalAccBo(String.valueOf(Integer.parseInt(misModel.getTotalAccBo()) + 1));
	            }
	        }

	        // Put the updated MISModel back to the referral map
	        referralMap.put(referral.getRefByName(), misModel);

	        // Put the referral map back to the date map
	        referralDateMap.put(formattedReferralDate, referralMap);
	    }

	    // Convert the referralDateMap to a list of MISModel
	    for (Map<String, MISModel> referralMap : referralDateMap.values()) {
	        misModels.addAll(referralMap.values());
	    }

	    return misModels;
	}



    public List<ReferralEntity> getExistingNotifyEntity(String frmDate, String toDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate fromDate = LocalDate.parse(frmDate, inputFormatter);
        LocalDate toDateObj = LocalDate.parse(toDate, inputFormatter);

        // Assuming your notifyRepository has a method findByDate
        List<ReferralEntity> existingNotifyEntity = notifyRepository.findByDate(
                Date.from(fromDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(toDateObj.plusDays(1).atStartOfDay().minusNanos(1).atZone(ZoneId.systemDefault()).toInstant()));

        return existingNotifyEntity;
    }
    
    
    private String determineStatus(ApplicationUserEntity userEntity, ReferralEntity referral) {
	    String status = userEntity.getStatus();
	    if (!userEntity.getStage().equals("13")) {
	            return "In Progress";
	        }
	     else if (userEntity.getStage().equals("13")) {
	        if (backOfficeApiRepository.findByapplicationId(userEntity.getId()) != null) {
	            return "Completed";
	        } else {
	            return "Submitted"; 
	        }
	    } else {
	        return "Unknown"; 
	    }
	}


    public String generateExcelSheet(List<MISModel> misModels) throws IOException {
        String slash = EkycConstants.UBUNTU_FILE_SEPERATOR;
        if (OS.contains(EkycConstants.OS_WINDOWS)) {
            slash = EkycConstants.WINDOWS_FILE_SEPERATOR;
        }
        String filePath = props.getFileBasePath() + slash
                + DateUtil.DDMMYYHHMMSS.format(DateUtil.getNewDateWithCurrentTime()) + ".xlsx";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("MIS Report");

        // Create header row with dates
        Row headerRow = sheet.createRow(0); // Start from row index 0
        List<String> dates = new ArrayList<>();
        for (MISModel model : misModels) {
            if (!dates.contains(model.getDate())) {
                dates.add(model.getDate());
            }
        }

        // Apply styles to header cells
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex()); // Set font color to white
        headerCellStyle.setFont(headerFont);

        Collections.sort(dates);
        for (int i = 0; i < dates.size(); i++) {
            Cell cell = headerRow.createCell(i * 3 + 1); // Start from column B (index 1)
            cell.setCellValue(dates.get(i));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i * 3 + 1, i * 3 + 3)); // Adjust merged region
            cell.setCellStyle(headerCellStyle);
        }

        // Create header row with Referral Name
        Row headerRow0 = sheet.createRow(1);
        Cell referralHeaderCell = headerRow0.createCell(0);
        referralHeaderCell.setCellValue("Referral Name");

        // Apply styles to Referral Name header cell
        CellStyle referralHeaderCellStyle = workbook.createCellStyle();
        referralHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
        referralHeaderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        referralHeaderCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        referralHeaderCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        referralHeaderCellStyle.setBorderTop(BorderStyle.THIN);
        referralHeaderCellStyle.setBorderBottom(BorderStyle.THIN);
        referralHeaderCellStyle.setBorderLeft(BorderStyle.THIN);
        referralHeaderCellStyle.setBorderRight(BorderStyle.THIN);
        Font referralHeaderFont = workbook.createFont();
        referralHeaderFont.setBold(true);
        referralHeaderFont.setColor(IndexedColors.BLUE.getIndex()); // Set font color to white
        referralHeaderCellStyle.setFont(referralHeaderFont);
        referralHeaderCell.setCellStyle(referralHeaderCellStyle);

        // Create second header row with "In Progress", "Submitted", "Completed"
        CellStyle secondHeaderCellStyle = workbook.createCellStyle();
        secondHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
        secondHeaderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        secondHeaderCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        secondHeaderCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        secondHeaderCellStyle.setBorderTop(BorderStyle.THIN);
        secondHeaderCellStyle.setBorderBottom(BorderStyle.THIN);
        secondHeaderCellStyle.setBorderLeft(BorderStyle.THIN);
        secondHeaderCellStyle.setBorderRight(BorderStyle.THIN);
        Font secondHeaderFont = workbook.createFont();
        secondHeaderFont.setBold(true);
        secondHeaderFont.setColor(IndexedColors.BLUE.getIndex()); // Set font color to white
        secondHeaderCellStyle.setFont(secondHeaderFont);

        for (int i = 0; i < dates.size(); i++) {
            Cell inProgressCell = headerRow0.createCell(i * 3 + 1); // Start from column B (index 1)
            inProgressCell.setCellValue("In Progress");
            inProgressCell.setCellStyle(secondHeaderCellStyle);
            Cell submittedCell = headerRow0.createCell(i * 3 + 2); // Next column (C)
            submittedCell.setCellValue("Submitted");
            submittedCell.setCellStyle(secondHeaderCellStyle);
            Cell completedCell = headerRow0.createCell(i * 3 + 3); // Next column (D)
            completedCell.setCellValue("Completed");
            completedCell.setCellStyle(secondHeaderCellStyle);
        }

     // Create referral rows
        Map<String, Integer> referralRowMap = new HashMap<>();
        int rowNum = 2; // Start row index from 2 for referral data
        CellStyle referralCellStyle = workbook.createCellStyle();
        referralCellStyle.setAlignment(HorizontalAlignment.CENTER);
        referralCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        referralCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        referralCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        referralCellStyle.setBorderTop(BorderStyle.THIN);
        referralCellStyle.setBorderBottom(BorderStyle.THIN);
        referralCellStyle.setBorderLeft(BorderStyle.THIN);
        referralCellStyle.setBorderRight(BorderStyle.THIN);
        Font referralFont = workbook.createFont();
        referralFont.setColor(IndexedColors.BLACK.getIndex());
        referralFont.setBold(true); // Set font to bold
        referralCellStyle.setFont(referralFont);

        for (MISModel model : misModels) {
            if (!referralRowMap.containsKey(model.getReferral())) {
                Row row = sheet.createRow(rowNum++);
                Cell referralCell = row.createCell(0);
                referralCell.setCellValue(model.getReferral());

                // Apply styles to referral rows
                referralCell.setCellStyle(referralCellStyle);

                referralRowMap.put(model.getReferral(), row.getRowNum());
            }
        }

        // Populate data for each referral and date
        int lastRow = sheet.getLastRowNum();
        int lastColumn = headerRow.getLastCellNum(); // Correctly fetch the last cell number

        for (MISModel model : misModels) {
            int rowIdx = referralRowMap.get(model.getReferral());
            int colIdx = dates.indexOf(model.getDate()) * 3; // Adjust column index to match merged cells
            Row row = sheet.getRow(rowIdx);
            if (row == null) {
                row = sheet.createRow(rowIdx);
            }
            Cell cellInProgress = row.createCell(colIdx + 1); // In Progress column
            cellInProgress.setCellValue(model.getTotalAccOpen());
            Cell cellSubmitted = row.createCell(colIdx + 2); // Submitted column
            cellSubmitted.setCellValue(model.getTotalAccEsign());
            Cell cellCompleted = row.createCell(colIdx + 3); // Completed column
            cellCompleted.setCellValue(model.getTotalAccBo());

            // Apply styles to data cells
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
            dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataCellStyle.setBorderTop(BorderStyle.THIN);
            dataCellStyle.setBorderBottom(BorderStyle.THIN);
            dataCellStyle.setBorderLeft(BorderStyle.THIN);
            dataCellStyle.setBorderRight(BorderStyle.THIN);
            Font dataFont = workbook.createFont();
            dataFont.setColor(IndexedColors.BLACK.getIndex());
            dataFont.setBold(true); // Set font to bold
            dataCellStyle.setFont(dataFont);
            
            cellInProgress.setCellStyle(dataCellStyle);
            cellSubmitted.setCellStyle(dataCellStyle);
            cellCompleted.setCellStyle(dataCellStyle);
        }


        // Apply styles to data cells
        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Apply borders to all cells
        dataCellStyle.setBorderTop(BorderStyle.THIN);
        dataCellStyle.setBorderBottom(BorderStyle.THIN);
        dataCellStyle.setBorderLeft(BorderStyle.THIN);
        dataCellStyle.setBorderRight(BorderStyle.THIN);

        // Apply font color
        Font dataFont = workbook.createFont();
        dataFont.setColor(IndexedColors.BLACK.getIndex());
        dataFont.setBold(true); // Set font to bold
        dataCellStyle.setFont(dataFont);

     // Iterate over all rows and cells to apply the style
        for (int i = 2; i <= lastRow; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            for (int j = 0; j <= lastColumn+1; j++) { // Adjusted loop condition to include the last column
                Cell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(dataCellStyle);

                // Fill empty cells with "0"
                if (cell.getCellType() == CellType.BLANK || cell.getStringCellValue().isEmpty()) {
                    cell.setCellValue(0);
                }
            }
        }

        // Auto-size columns
        for (int i = 0; i < lastColumn; i++) { // Adjusted loop condition
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        // Close the output stream
        fileOut.close();

        // Close the workbook to release resources
        workbook.close();

        return filePath;
    }

    
    
	public String generateExcelSheet1(List<MISModel> misModels, List<String> refByNames) {
		String slash = EkycConstants.UBUNTU_FILE_SEPERATOR;
		if (OS.contains(EkycConstants.OS_WINDOWS)) {
			slash = EkycConstants.WINDOWS_FILE_SEPERATOR;
		}
		String filePath = props.getFileBasePath() + slash
				+ DateUtil.DDMMYYHHMMSS.format(DateUtil.getNewDateWithCurrentTime()) + ".xlsx";
		try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath)) {
			Sheet sheet = workbook.createSheet("Sheet1");

			// Create CellStyle for the header
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 16); // Increase font size
			headerFont.setColor(IndexedColors.WHITE.getIndex()); // Set font color to white for better contrast
			headerStyle.setFont(headerFont);

			// Create CellStyle for the column headers
			CellStyle columnHeaderStyle = workbook.createCellStyle();
			columnHeaderStyle.setBorderBottom(BorderStyle.THIN);
			columnHeaderStyle.setBorderTop(BorderStyle.THIN);
			columnHeaderStyle.setBorderLeft(BorderStyle.THIN);
			columnHeaderStyle.setBorderRight(BorderStyle.THIN);
			columnHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			columnHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			columnHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			Font columnHeaderFont = workbook.createFont();
			columnHeaderFont.setBold(true);
			columnHeaderFont.setFontHeightInPoints((short) 10); // Increase font size if necessary
			columnHeaderStyle.setFont(columnHeaderFont);

			// Create CellStyle for the data cells
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.CENTER);
			dataStyle.setBorderBottom(BorderStyle.THIN);
			dataStyle.setBorderTop(BorderStyle.THIN);
			dataStyle.setBorderLeft(BorderStyle.THIN);
			dataStyle.setBorderRight(BorderStyle.THIN);

			// Create CellStyle for alternating row colors
			CellStyle altRowStyle = workbook.createCellStyle();
			altRowStyle.cloneStyleFrom(dataStyle);
			altRowStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			altRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Merge cells and set the header row
			Row headerRow = sheet.createRow(0);
			Cell headerCell = headerRow.createCell(0);
			headerCell.setCellValue("MIS - Account Opening - Daily Number & TAT");
			headerCell.setCellStyle(headerStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5 + refByNames.size())); // Merge cells A1 to remarks
																							// column

			// Create the second row with column headers and remarks field
			Row secondRow = sheet.createRow(1);
			String[] columnHeaders = { "S.No", "A/c Opening Date", "Total A/c Opened", "T Day", "> T Day" };

			// Add column headers
			for (int i = 0; i < columnHeaders.length; i++) {
				Cell cell = secondRow.createCell(i);
				cell.setCellValue(columnHeaders[i]);
				cell.setCellStyle(columnHeaderStyle); // Apply the style to column headers
			}

			// Add refByName values to the second row
			int columnIndex = 5; // Start column index for refByName values
			for (String refByName : refByNames) {
				Cell cell = secondRow.createCell(columnIndex++);
				cell.setCellValue(refByName.toUpperCase());
				cell.setCellStyle(columnHeaderStyle); // Apply the same style as column headers
			}

			// Add "Remarks" field after referral names
			Cell remarksCell = secondRow.createCell(columnIndex);
			remarksCell.setCellValue("REMARKS");
			remarksCell.setCellStyle(columnHeaderStyle); // Apply the same style as column headers

			// Write data to rows
			int rowNum = 2; // Start row for data
			for (MISModel model : misModels) {
				Row dataRow = sheet.createRow(rowNum++);
				createDataCells1(dataRow, model, rowNum % 2 == 0 ? dataStyle : altRowStyle, refByNames, sheet);
			}
			// After iterating through all rows, call updateTotalCount
			Map<String, Long> totalReferralCount = calculateTotalReferralCount(sheet, refByNames);
			updateTotalCount(sheet.getLastRowNum(), totalReferralCount, refByNames, sheet,rowNum % 2 == 0 ? dataStyle : altRowStyle);

			// Auto-size columns for better readability
			for (int i = 0; i < columnHeaders.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// Write to the file
			workbook.write(fileOut);
			System.out.println("Excel file generated successfully at: " + filePath);
			return filePath;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Map<String, Long> calculateTotalReferralCount(Sheet sheet, List<String> refByNames) {
	    Map<String, Long> totalReferralCountMap = new HashMap<>();

	    // Iterate over each row starting from the second row (excluding the header)
	    for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
	        Row row = sheet.getRow(rowIndex);
	        if (row != null) {
	            // Iterate over the refByNames list to populate referral counts
	            for (int columnIndex = 5; columnIndex < row.getLastCellNum(); columnIndex++) {
	                Cell cell = row.getCell(columnIndex);
	                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
	                    String refByName = refByNames.get(columnIndex - 5);
	                    long count = (long) cell.getNumericCellValue();
	                    // Update the count for the corresponding referral name
	                    totalReferralCountMap.put(refByName, totalReferralCountMap.getOrDefault(refByName, 0L) + count);
	                }
	            }
	        }
	    }

	    return totalReferralCountMap;
	}


	private void createDataCells1(Row row, MISModel model, CellStyle style, List<String> refByNames, Sheet sheet) {
		row.createCell(0).setCellValue(row.getRowNum()); // Adjust row number to start from 1
		String date = model.getDate() != null ? model.getDate() : "TOTAL"; // Check if date is null
		row.createCell(1).setCellValue(date);
		row.createCell(2).setCellValue(model.getTotalAccOpen());
		row.createCell(3).setCellValue(model.getTotalAccBo());
		row.createCell(4).setCellValue(model.getTotalAccEsign());

		// Apply style to all cells in the row
		for (int i = 0; i < 5; i++) {
			row.getCell(i).setCellStyle(style);
		}

		int columnIndex = 5;
		if (model.getDate() != null) {
			for (String refByName : refByNames) {
				long referralCount = getCountForReferral(refByName, model.getDate());
				Cell countCell = row.createCell(columnIndex++);
				countCell.setCellValue(referralCount);
				countCell.setCellStyle(style); // Apply the style
			}
		}
		Cell lastColumnCell = row.createCell(columnIndex);
		lastColumnCell.setCellStyle(style);
	}
	private void updateTotalCount(int lastRowNumber, Map<String, Long> totalReferralCount, List<String> refByNames, Sheet sheet,CellStyle style) {
	    Row lastRow = sheet.getRow(lastRowNumber);
	    if (lastRow == null) {
	        lastRow = sheet.createRow(lastRowNumber);
	    }

	    int columnIndex = 4; // Start column index for referral counts

	    Cell totalCell = lastRow.createCell(columnIndex);
	    totalCell.setCellValue(totalReferralCount.getOrDefault("Total", 0L));
	    totalCell.setCellStyle(style); 
	    // Iterate over the refByNames list to populate referral counts
	    for (String refByName : refByNames) {
	        columnIndex++;
	        Cell countCell = lastRow.createCell(columnIndex);
	        countCell.setCellValue(totalReferralCount.getOrDefault(refByName, 0L)); // Set individual counts
	        countCell.setCellStyle(style); // Apply the style
	    }
	    // Apply the style to the last column (total)
	    Cell countCell = lastRow.createCell(columnIndex+1);
	    countCell.setCellStyle(style);
	}



	private long getCountForReferral(String refByName, String date) {
		LocalDateTime startDateTime = LocalDateTime.parse(date + "T00:00:00");
		LocalDateTime endDateTime = LocalDateTime.parse(date + "T23:59:59");
		Date startDate = java.sql.Timestamp.valueOf(startDateTime);
		Date endDate = java.sql.Timestamp.valueOf(endDateTime);
		long referralCount = notifyRepository.getCountForReferralAndDateRange(refByName, startDate, endDate);
		return referralCount;
	}
}