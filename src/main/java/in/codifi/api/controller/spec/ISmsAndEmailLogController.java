package in.codifi.api.controller.spec;

import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import in.codifi.api.request.model.SmsEmailReqModel;
import in.codifi.api.response.model.ResponseModel;

public interface ISmsAndEmailLogController {

	@Path("/getLog")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = " Method to get Sms And EMail logs")
	public ResponseModel getLog(@NotNull SmsEmailReqModel reqModel);
}
