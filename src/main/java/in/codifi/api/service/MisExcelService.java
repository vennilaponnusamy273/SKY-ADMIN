package in.codifi.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import in.codifi.api.config.ApplicationProperties;
import in.codifi.api.entity.ApplicationUserEntity;
import in.codifi.api.entity.BackOfficeApiEntity;
import in.codifi.api.entity.DocumentEntity;
import in.codifi.api.entity.ReferralEntity;
import in.codifi.api.repository.ApplicationUserRepository;
import in.codifi.api.repository.BackOfficeApiRepository;
import in.codifi.api.repository.DocumentEntityRepository;
import in.codifi.api.repository.ReferralRepository;
import in.codifi.api.request.model.JourneyDetails;
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
	public Response ExcelDownload(String frmDate, String toDate) {
	  //  ResponseModel responseModel = new ResponseModel();

	    try {
	    	 List<ApplicationUserEntity> userEntity = null;
	         List<JourneyDetails> journeyDetails = new ArrayList<>(); // Initialize the list

	         String from = frmDate;
	         System.out.println("the from: " + from);
	         String to = toDate;
	         System.out.println("the to: " + to);
	         DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	         // Parse into LocalDate using the new format pattern
	         LocalDate fromDate = LocalDate.parse(from, inputFormatter);
	         LocalDate ToDate = LocalDate.parse(to, inputFormatter);

	         // Adjust the time to include the whole day
	         LocalDateTime fromDateTime = fromDate.atStartOfDay();
	         LocalDateTime endDateTime = ToDate.atTime(23, 59, 59, 999999999);

	         userEntity = applicationUserRepository.findByDate(Date.from(fromDateTime.atZone(ZoneId.systemDefault()).toInstant()), Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));
	        if (userEntity != null) {
	            for (ApplicationUserEntity applicationUserEntity : userEntity) {
	                ReferralEntity existingNotifyEntity = notifyRepository
	                        .findByMobileNo(applicationUserEntity.getMobileNo());
	                BackOfficeApiEntity backOfficeApiEntity = backOfficeApiRepository
	                        .findByapplicationId(applicationUserEntity.getId());
	                DocumentEntity document = docrepository
	                        .findByApplicationIdAndDocumentType(applicationUserEntity.getId(), EkycConstants.DOC_ESIGN);

	                // Create a new JourneyDetails object for each user entity
	                JourneyDetails details = new JourneyDetails();
	                details.setCreatedOn(applicationUserEntity.getCreatedOn());
	                details.setSignedDate((document != null && document.getCreatedOn() != null) ? document.getCreatedOn() : null);
	                details.setName(applicationUserEntity.getUserName());
	                details.setPan(applicationUserEntity.getPanNumber());
	                details.setUccCode(
	                        (applicationUserEntity.getUccCodePrefix() != null ? applicationUserEntity.getUccCodePrefix() : "") +
	                        (applicationUserEntity.getUccCodeSuffix() != null ? applicationUserEntity.getUccCodeSuffix() : ""));
	                details.setBackOfficePushDate((backOfficeApiEntity != null && backOfficeApiEntity.getCreatedOn() != null) ? backOfficeApiEntity.getCreatedOn() : null);
	                details.setMode(existingNotifyEntity != null ? "REFERRAL" : "ONLINE");
	                details.setReferralName(existingNotifyEntity != null ? existingNotifyEntity.getRefByName() : "");
	                details.setMobileNumber(applicationUserEntity.getMobileNo() != null ? applicationUserEntity.getMobileNo().toString() : "");
	                details.setEmail(applicationUserEntity.getEmailId() != null ? applicationUserEntity.getEmailId() : "");

	                // Add the journey details to the list
	                journeyDetails.add(details);
	            }
	        }

	        String filePath = generateExcelSheet(journeyDetails);
			File file = new File(filePath);
			return Response.ok(file)
			        .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
			        .build();

	    } catch (Exception e) {
	        logger.error("An error occurred: " + e.getMessage());
	       // responseModel = commonMethods.constructFailedMsg(e.getMessage());
	        e.printStackTrace();
	    	return Response.serverError().entity("An error occurred: " + e.getMessage()).build();
	    }
	}

	private String generateExcelSheet(List<JourneyDetails> ekycExcelClientDetails) {
		String slash = EkycConstants.UBUNTU_FILE_SEPERATOR;
		if (OS.contains(EkycConstants.OS_WINDOWS)) {
			slash = EkycConstants.WINDOWS_FILE_SEPERATOR;
		}
		String filePath = props.getFileBasePath() + "worksheet" + slash
				+ DateUtil.DDMMYYHHMMSS.format(DateUtil.getNewDateWithCurrentTime()) + ".xlsx";
		try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath)) {
            Sheet sheet = workbook.createSheet("Sheet1");

            // Set column widths
            int columnCount = 0;
            int[] columnWidths = { 15, 20, 15, 15, 20, 20, 20, 20, 20, 20 };
            for (int width : columnWidths) {
                sheet.setColumnWidth(columnCount++, width * 256);
            }

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headerTitles = { "Mobile Number", "Email", "Client Code", "Pan", "Journey Start Date", "Name",
                    "Mode Of Application", "Referral Name", "Esign Date", "Code Creation Date" };

            CellStyle headerStyle = createHeaderStyle(workbook);
            createRow(headerRow, headerTitles, headerStyle);

            // Create data rows
            CellStyle dataStyle = createDataStyle(workbook);
            int rowNum = 1;
            for (JourneyDetails model : ekycExcelClientDetails) {
                Row dataRow = sheet.createRow(rowNum++);
                createDataCells(dataRow, model, dataStyle);
            }

            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        return headerStyle;
    }

    private void createRow(Row row, String[] values, CellStyle style) {
        int cellNum = 0;
        for (String value : values) {
            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(value);
            cell.setCellStyle(style);
        }
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        return dataStyle;
    }

    private void createDataCells(Row row, JourneyDetails model, CellStyle style) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        row.createCell(0).setCellValue(model.getMobileNumber());
        row.createCell(1).setCellValue(model.getEmail());
        row.createCell(2).setCellValue(model.getUccCode());
        row.createCell(3).setCellValue(model.getPan());
        row.createCell(4).setCellValue(dateFormat.format(model.getCreatedOn()));
        row.createCell(5).setCellValue(model.getName());
        row.createCell(6).setCellValue(model.getMode());
        row.createCell(7).setCellValue(model.getReferralName());
        row.createCell(8).setCellValue(model.getSignedDate() != null ? dateFormat.format(model.getSignedDate()) : "");
        row.createCell(9).setCellValue(
                model.getBackOfficePushDate() != null ? dateFormat.format(model.getBackOfficePushDate()) : "");

        for (int i = 0; i < 10; i++) {
            row.getCell(i).setCellStyle(style);
        }
    }
}