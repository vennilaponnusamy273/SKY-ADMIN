package in.codifi.api.controller.spec;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import in.codifi.api.entity.ReferralEntity;
import in.codifi.api.response.model.ResponseModel;

public interface IReferralController {

	/**
	 * Method to nodify user to Mail and Message
	 **/

	@Path("/setReferral")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = " Method to nodify user to Mail and Message")
	public ResponseModel notifyUser(ReferralEntity NotifyEntity);
}
