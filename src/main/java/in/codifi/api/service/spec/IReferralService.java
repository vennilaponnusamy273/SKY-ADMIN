package in.codifi.api.service.spec;

import javax.validation.constraints.NotNull;

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
	ResponseModel setReferral(ReferralEntity NotifyEntity);

	/**
	 * Method to nodify user to Mail and Message
	 * 
	 * @author prade
	 * @param applicationId
	 * @return
	 **/
	ResponseModel notifyUser(@NotNull long id, @NotNull String referralId);

	/**
	 * get Referral Record by referral Id
	 * 
	 * @author prade
	 * @param referralId
	 * @return
	 */
	ResponseModel getRecordByUser(@NotNull String referralId);
}
