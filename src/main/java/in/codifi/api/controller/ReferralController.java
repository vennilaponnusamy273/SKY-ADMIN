package in.codifi.api.controller;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Path;

import in.codifi.api.controller.spec.IReferralController;
import in.codifi.api.entity.ReferralEntity;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.IReferralService;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.MessageConstants;
import in.codifi.api.utilities.StringUtil;

@Path("/referral")
public class ReferralController implements IReferralController {
	@Inject
	CommonMethods commonMethods;

	@Inject
	IReferralService inotifyService;

	/**
	 * Method to create referral and send sms
	 */
	@Override
	public ResponseModel setReferral(ReferralEntity NotifyEntity) {
		ResponseModel responseModel = null;
		try {
			if (NotifyEntity != null && StringUtil.isNotNullOrEmpty(NotifyEntity.getReferralBy())
					&& NotifyEntity.getMobileNo() > 0) {
				responseModel = inotifyService.setReferral(NotifyEntity);
			} else {
				if (NotifyEntity == null) {
					responseModel = commonMethods.constructFailedMsg(MessageConstants.PARAMETER_NULL);
				} else {
					if (StringUtil.isNullOrEmpty(NotifyEntity.getReferralBy())) {
						responseModel = commonMethods.constructFailedMsg(MessageConstants.REFERRAL_BY_NULL);
					} else {
						responseModel = commonMethods.constructFailedMsg(MessageConstants.MOBILE_NO_NULL);
					}
				}
			}
		} catch (Exception e) {
			responseModel = commonMethods.constructFailedMsg(e.getMessage());
		}
		return responseModel;
	}

	/**
	 * Method to nodify user to Mail and Message
	 */
	@Override
	public ResponseModel notifyClient(@NotNull long id, @NotNull String referralId) {
		ResponseModel responseModel = null;
		if (id > 0) {
			responseModel = inotifyService.notifyUser(id, referralId);
		} else {
			responseModel = commonMethods.constructFailedMsg(MessageConstants.INVLAID_PARAMETER);
		}
		return responseModel;
	}

	/**
	 * get Referral Record by referral Id
	 */
	@Override
	public ResponseModel getRecordByUser(@NotNull String referralId) {
		ResponseModel responseModel = null;
		if (StringUtil.isNotNullOrEmpty(referralId)) {
			responseModel = inotifyService.getRecordByUser(referralId);
		} else {
			responseModel = commonMethods.constructFailedMsg(MessageConstants.INVLAID_PARAMETER);
		}
		return responseModel;
	}

}
