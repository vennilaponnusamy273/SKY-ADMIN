package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import in.codifi.api.controller.spec.IMisExcelController;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.IMisExcelService;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.MessageConstants;

@Path("/mis")
public class MisExcelController implements IMisExcelController {

	@Inject
	IMisExcelService MisExcelService;
	@Inject
	CommonMethods commonMethods;

	@Override
	public Response ExcelDownload(String frmDate, String toDate) {
		if (frmDate != null && toDate != null) {
			return MisExcelService.ExcelDownload(frmDate, toDate);
		} else {
			if (toDate == null && toDate == null) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageConstants.DATE_NULL)
						.build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageConstants.PARAMETER_NULL)
						.build();
			}
		}
	}

}
