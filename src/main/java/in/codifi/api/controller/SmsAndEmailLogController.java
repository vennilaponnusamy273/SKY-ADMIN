package in.codifi.api.controller;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Path;

import in.codifi.api.controller.spec.ISmsAndEmailLogController;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.ISmsAndEmailLogService;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.MessageConstants;

@Path("/smsEmailLog")
public class SmsAndEmailLogController implements ISmsAndEmailLogController {

	@Inject
	ISmsAndEmailLogService iSmsAndEmailLogService;
	@Inject
	CommonMethods commonMethods;
	
	@Override
	public ResponseModel getLog(@NotNull long limit, @NotNull long offset, @NotNull String logType) {
		ResponseModel response=new ResponseModel();
		try {
			if(limit>50) {
				  response.setStat(MessageConstants.SUCCESS_STATUS);
			        response.setMessage(MessageConstants.SUCCESS_MSG);
			        response.setResult(MessageConstants.LIMIT_VALIDATION_MESSAGE);
			        return response;
			}
			if(logType.equalsIgnoreCase("SMS")||logType.equalsIgnoreCase("EMAIL")) {
			response=iSmsAndEmailLogService.getLog(limit,offset,logType);
			}
			else{
				response = commonMethods.constructFailedMsg("Invalid log type. Please use SMS or EMAIL.");
				}
		}catch (Exception e) {
		response = commonMethods.constructFailedMsg(e.getMessage());
		response.setResult(response);
		response.setMessage(EkycConstants.FAILED_MSG);
		response.setStat(EkycConstants.FAILED_STATUS);
	}
	return response;
	}
}
