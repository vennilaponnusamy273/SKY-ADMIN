package in.codifi.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import in.codifi.api.config.ApplicationProperties;
import in.codifi.api.entity.ApplicationUserEntity;
import in.codifi.api.entity.BackOfficeApiEntity;
import in.codifi.api.entity.DocumentEntity;
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
			List<ApplicationUserEntity> userEntity = null;
			List<DocumentEntity> document = null;
			List<BackOfficeApiEntity> backOfficeApiEntity = null;
			List<MISModel> misModels = new ArrayList<>();

			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate fromDate = LocalDate.parse(frmDate, inputFormatter);
			LocalDate toDateObj = LocalDate.parse(toDate, inputFormatter);

			userEntity = applicationUserRepository.findByDate(
					Date.from(fromDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(toDateObj
							.plusDays(1).atStartOfDay().minusNanos(1).atZone(ZoneId.systemDefault()).toInstant()));
//
//			document = docrepository.findByDate(
//					Date.from(fromDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(toDateObj
//							.plusDays(1).atStartOfDay().minusNanos(1).atZone(ZoneId.systemDefault()).toInstant()));

			backOfficeApiEntity = backOfficeApiRepository.findByDate(
					Date.from(fromDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(toDateObj
							.plusDays(1).atStartOfDay().minusNanos(1).atZone(ZoneId.systemDefault()).toInstant()));

			List<String> existingNotifyEntity = notifyRepository.findByDate(
					Date.from(fromDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(toDateObj
							.plusDays(1).atStartOfDay().minusNanos(1).atZone(ZoneId.systemDefault()).toInstant()));

			if (userEntity != null) {
				for (LocalDate date = fromDate; date.isBefore(toDateObj.plusDays(1)); date = date.plusDays(1)) {
					int totalAccOpenOnDate = 0;
					int totalEsignDateOnDate = 0;
					int totalBODateOnDate = 0;

					for (ApplicationUserEntity entity : userEntity) {
						LocalDate entityDate = entity.getCreatedOn().toInstant().atZone(ZoneId.systemDefault())
								.toLocalDate();
						if (entityDate.isEqual(date)) {
							totalAccOpenOnDate++;
						}
					}

//					if (document != null) {
//						for (DocumentEntity entity : document) {
//							LocalDate entityDate = entity.getCreatedOn().toInstant().atZone(ZoneId.systemDefault())
//									.toLocalDate();
//							if (entityDate.isEqual(date)) {
//								totalEsignDateOnDate++;
//							}
//						}
//					}

					if (backOfficeApiEntity != null) {
						for (BackOfficeApiEntity entity : backOfficeApiEntity) {
							LocalDate entityDate = entity.getCreatedOn().toInstant().atZone(ZoneId.systemDefault())
									.toLocalDate();
							if (entityDate.isEqual(date)) {
								totalBODateOnDate++;
							}
						}

					}

					for (String refByName : existingNotifyEntity) {
						System.out.println(refByName);
					}

					MISModel misModel = new MISModel();
					misModel.setDate(date.toString());
					misModel.setTotalAccOpen(String.valueOf(totalAccOpenOnDate));
					misModel.setTotalAccEsign(String.valueOf(totalAccOpenOnDate - totalBODateOnDate));
					misModel.setTotalAccBo(String.valueOf(totalBODateOnDate));
					misModels.add(misModel);
				}
				// Calculate overall counts
				long overallTotalAccOpen = 0;
				long overallTotalAccEsign = 0;
				long overallTotalAccBo = 0;
				for (MISModel model : misModels) {
					overallTotalAccOpen += Long.parseLong(model.getTotalAccOpen());
					overallTotalAccEsign += Long.parseLong(model.getTotalAccEsign());
					overallTotalAccBo += Long.parseLong(model.getTotalAccBo());
				}

				// Add overall counts to a new MISModel object
				MISModel overallCounts = new MISModel();
				// overallCounts.setDate("TOTAL");
				overallCounts.setTotalAccOpen(String.valueOf(overallTotalAccOpen));
				overallCounts.setTotalAccEsign(String.valueOf(overallTotalAccEsign));
				overallCounts.setTotalAccBo(String.valueOf(overallTotalAccBo));
				misModels.add(overallCounts);
			}

			String filePath = generateExcelSheet1(misModels, existingNotifyEntity);
			File file = new File(filePath);
			return Response.ok(file).header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
					.build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity("An error occurred: " + e.getMessage()).build();
		}
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