package in.codifi.api.controller;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Path;

import in.codifi.api.controller.spec.IApiStatusController;
import in.codifi.api.request.model.ApiStatusModel;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.IApiStatusService;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.MessageConstants;
import in.codifi.api.utilities.StringUtil;

@Path("/updateStatus")
public class ApiStatusController implements IApiStatusController {

	@Inject
	IApiStatusService iApiStatusService;

	@Inject
	CommonMethods commonMethods;

	/**
	 * Method to Update User Documenr Status by Admin
	 * 
	 * @param ApiStatusModel
	 * @return
	 */

	@Override
	public ResponseModel updateStatus(ApiStatusModel apiStatusModel) {
		ResponseModel response = new ResponseModel();
		try {
			if (StringUtil.isNotNullOrEmptyA(apiStatusModel)) {
				response = iApiStatusService.updateStatus(apiStatusModel);
			} else {
				response = commonMethods.constructFailedMsg(MessageConstants.PARAMETER_NULL);
			}
		} catch (Exception e) {
			response = commonMethods.constructFailedMsg(e.getMessage());
		}
		return response;
	}

	/**
	 * Method to Method to view User Documenr Status by Admin
	 * 
	 * @param applicationId
	 * @return
	 */

	@Override
	public ResponseModel getStatus(long applicationId) {
		ResponseModel response = new ResponseModel();
		try {
			response = iApiStatusService.getStatus(applicationId);
		} catch (Exception e) {
			response = commonMethods.constructFailedMsg(e.getMessage());
		}
		return response;
	}

	/**
	 * Method to Send Email for Rejected user
	 * 
	 * @param applicationId
	 * @return
	 */

	@Override
	public ResponseModel sendMail(@NotNull long applicationId) {
		ResponseModel response = new ResponseModel();
		try {
			response = iApiStatusService.sendMail(applicationId);
		} catch (Exception e) {
			response = commonMethods.constructFailedMsg(e.getMessage());
		}
		return response;
	}
}
