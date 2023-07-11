package in.codifi.api.controller.spec;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import in.codifi.api.request.model.ApiStatusModel;
import in.codifi.api.response.model.ResponseModel;

public interface IApiStatusController {

	/**
	 * Method to Update User Documenr Status by Admin
	 **/

	@Path("/setStatus")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Update User Documenr Status")
	public ResponseModel updateStatus(ApiStatusModel apiStatusModel);

	/**
	 * Method to view User Documenr Status by Admin
	 **/

	@Path("/getStatus")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method  to view User Documenr Status")
	public ResponseModel getStatus(@NotNull @QueryParam("applicationId") long applicationId);

	/**
	 * Method to Send Email for Rejected user
	 **/

	@Path("/sendMail")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to Send Email for Rejected user")
	public ResponseModel sendMail(@NotNull @QueryParam("applicationId") long applicationId);
}
