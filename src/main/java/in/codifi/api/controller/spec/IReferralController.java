package in.codifi.api.controller.spec;

import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import in.codifi.api.entity.ReferralEntity;
import in.codifi.api.response.model.ResponseModel;

public interface IReferralController {
	/**
	 * Method to create referral and send sms
	 * 
	 * @param referralEntity
	 * @return
	 **/

	@Path("/setReferral")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = " Method to create referral and send sms")
	public ResponseModel setReferral(ReferralEntity referralEntity);

	/**
	 * Method to nodify user to Mail and Message
	 * 
	 * @param applicationId
	 * @return
	 **/

	@Path("/notifyClient")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = " Method to nodify user to Mail and Message")
	public ResponseModel notifyClient(@NotNull @QueryParam("applicationId") long applicationId,
			@NotNull @QueryParam("referralId") String referralId);

	/**
	 * get Referral Record by referral Id
	 * 
	 * @param referralId
	 * @return
	 */
	@Path("/getRecordByUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = " Method to get Record by referral Id")
	public ResponseModel getRecordByUser(@NotNull @QueryParam("referralId") String referralId);
}
