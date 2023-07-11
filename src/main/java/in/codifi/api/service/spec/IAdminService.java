package in.codifi.api.service.spec;

import javax.validation.constraints.NotNull;

import in.codifi.api.response.model.ResponseModel;

public interface IAdminService {

	ResponseModel sendRejectionMail(@NotNull long applicationId, boolean confirmMail);

	/**
	 * Method to initiaze push to back office
	 * 
	 * @author prade
	 * @param applicationId
	 * @return
	 */
	ResponseModel pushBO(@NotNull long applicationId);
}
