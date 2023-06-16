package in.codifi.api.controller.spec;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

public interface IDocDownloadController {

	/**
	 * Method to download uploaded file
	 * 
	 * @param applicationId and docType
	 * @param type
	 * @return
	 */
	@Path("/getFile")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to downloadFile document ")
	public Response downloadFile(@NotNull @QueryParam("applicationId") long applicationId,
			@NotNull @QueryParam("documentType") String type);
}
