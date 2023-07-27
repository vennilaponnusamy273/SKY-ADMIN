package in.codifi.api.controller.spec;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import in.codifi.api.response.model.ResponseModel;

public interface ISmsAndEmailLogController {
	
	@Path("/getLog")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = " Method to get Sms And EMail logs")
	public ResponseModel getLog(@NotNull @QueryParam("limit") long limit,@NotNull @QueryParam("offset") long offset,
			@NotNull @QueryParam("Type") String logType);
}
