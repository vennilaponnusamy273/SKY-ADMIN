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
	
	/**
	 * Method to getBank details form IFSC code
	 * 
	 * @author prade
	 * @param ifscCode
	 * @return
	 */
	ResponseModel getIfsc(@NotNull String ifscCode);
	

	/**
	 * Method to send RiskDisCloure Document via Email
	 * 
	 * @param applicationId
	 * @return
	 */
	ResponseModel sendRiskDoc(long applicationId);
}
