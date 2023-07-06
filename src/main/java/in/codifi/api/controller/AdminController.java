package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import in.codifi.api.controller.spec.IAdminController;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.IAdminService;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.MessageConstants;
import io.smallrye.common.constraint.NotNull;

@Path("/admin")
public class AdminController implements IAdminController {

	@Inject
	IAdminService adminService;
	@Inject
	CommonMethods commonMethods;
	
	@Override
	public ResponseModel sendRejectionMail(@NotNull long applicationId, boolean confirmMail) {
		ResponseModel responseModel = null;
		if (applicationId > 0) {
			responseModel = adminService.sendRejectionMail(applicationId, confirmMail);
		} else {
			responseModel = commonMethods.constructFailedMsg(MessageConstants.INVLAID_PARAMETER);
		}
		return responseModel;
	}

}
