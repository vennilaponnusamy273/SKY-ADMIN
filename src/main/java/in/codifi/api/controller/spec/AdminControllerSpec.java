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

public interface AdminControllerSpec {
	
	/**
	 * Method to save Bank Details
	 * 
	 * @author VENNILA
	 * @param 
	 * @return
	 */
	
	@Path("/GetUserDetails")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to get USer records")
	ResponseModel getUserDetails(@NotNull @QueryParam("offset") long offset,@NotNull @QueryParam("limit") long limit);
}
