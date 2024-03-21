package in.codifi.api.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.wildfly.common.annotation.NotNull;

public interface IMisExcelController {

	/**
	 * Method to ExcelDownload
	 * 
	 * @author prade
	 * @param frmDate,toDate
	 * @return
	 */
	
	@Path("/ExcelDownload")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to send sendRiskDoc via Email")
	public Response ExcelDownload(@NotNull @QueryParam("frmDate") String frmDate,@NotNull @QueryParam("toDate") String toDate);
}
