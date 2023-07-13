package in.codifi.api.controller.spec;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import in.codifi.api.entity.notifyEntity;
import in.codifi.api.response.model.ResponseModel;

public interface InotifyController {

	/**
	 * Method to nodify user to Mail and Message
	 **/

	@Path("/setNodify")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = " Method to nodify user to Mail and Message")
	public ResponseModel nodifyUser(notifyEntity NotifyEntity);
}
