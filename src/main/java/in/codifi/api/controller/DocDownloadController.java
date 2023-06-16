package in.codifi.api.controller;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import in.codifi.api.utilities.MessageConstants;
import in.codifi.api.utilities.StringUtil;
import in.codifi.api.controller.spec.IDocDownloadController;
import in.codifi.api.service.spec.IDocDownloadService;

@Path("/Download")
public class DocDownloadController implements IDocDownloadController {

	@Inject
	IDocDownloadService docDownloadService;
	/**
	 * Method to download uploaded documents
	 */
	@Override
	public Response downloadFile(@NotNull long applicationId, @NotNull String type) {
		if (applicationId > 0 && StringUtil.isNotNullOrEmpty(type)) {
			return docDownloadService.downloadFile(applicationId, type);
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageConstants.PARAMETER_NULL)
					.build();
		}
	}

}
