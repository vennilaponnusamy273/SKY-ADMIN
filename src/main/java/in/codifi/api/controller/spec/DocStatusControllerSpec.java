package in.codifi.api.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import in.codifi.api.entity.DocStatusEntity;
import in.codifi.api.response.model.ResponseModel;
import io.smallrye.common.constraint.NotNull;

public interface DocStatusControllerSpec {

	
	@Path("/saveDocStatus")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to save DocumentStatus details")
	ResponseModel saveDocStatus(DocStatusEntity docStatusEntity);
	
	@Path("/getDocStatus")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to get bank Details")
	ResponseModel getDocDetails(@NotNull @QueryParam("applicationId") long applicationId);
}
