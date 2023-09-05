package in.codifi.api.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.wildfly.common.annotation.NotNull;

import in.codifi.api.response.model.ResponseModel;

public interface IAdminController {

	/**
	 * Method to send mail on Esign Users
	 * 
	 * @author VENNILA
	 * @param
	 * @return
	 */

	@Path("/sendRejectionMail")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to dcheck trackWizz")
	public ResponseModel sendRejectionMail(@NotNull @QueryParam("applicationId") long applicationId,
			@NotNull @QueryParam("ConformMail") boolean ConformMail);

	/**
	 * Method to initiaze push to back office
	 * 
	 * @author prade
	 * @param applicationId
	 * @return
	 */
	@Path("/pushBO")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to dcheck trackWizz")
	public ResponseModel pushBO(@NotNull @QueryParam("applicationId") long applicationId);
	
	@GET
	@Path("/getIfsc")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseModel getIfsc(@NotNull @QueryParam("ifscCode") String ifscCode);
	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseModel test();
	
	@Path("/sendRiskDoc")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to send sendRiskDoc via Email")
	public ResponseModel sendRiskDoc(@NotNull @QueryParam("applicationId") long applicationId);
}
