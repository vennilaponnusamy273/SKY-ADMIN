package in.codifi.api.service.spec;

import javax.ws.rs.core.Response;

public interface IMisExcelService {

	
	/**
	 * Method to ExcelDownload
	 * 
	 * @author prade
	 * @param frmDate,toDate
	 * @return
	 */
	
	Response ExcelDownload(String frmDate, String toDate);

}
