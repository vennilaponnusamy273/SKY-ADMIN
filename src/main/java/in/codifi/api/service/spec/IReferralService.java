package in.codifi.api.service.spec;
import in.codifi.api.entity.ReferralEntity;
import in.codifi.api.response.model.ResponseModel;

public interface IReferralService {

	/**
	 * Method to nodify to MEssage and Email 
	 * 
	 * @author VennilA
	 * @param ReferralEntity
	 * @return
	 */
	ResponseModel notifyUser(ReferralEntity NotifyEntity);
}
