package in.codifi.api.service.spec;
import javax.validation.constraints.NotNull;

import in.codifi.api.request.model.ApiStatusModel;
import in.codifi.api.response.model.ResponseModel;


public interface IApiStatusService {

	/**
	 * Method to Update User Documenr Status by Admin 
 
	 * @param ApiStatusModel
	 * @return
	 */
	ResponseModel updateStatus(ApiStatusModel apiStatusModel) ;

	/**
	 * Method to Method to view User Documenr Status by Admin
	 
	 * @param applicationId
	 * @return
	 */
	ResponseModel getStatus(long applicationId);

	/**
	* Method to Send Email for Rejected user
	 
	 * @param applicationId
	 * @return
	 */
	
	ResponseModel sendMail(@NotNull long applicationId);

}
