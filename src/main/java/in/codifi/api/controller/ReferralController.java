package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;
import in.codifi.api.controller.spec.IReferralController;
import in.codifi.api.entity.ReferralEntity;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.IReferralService;
import in.codifi.api.utilities.CommonMethods;

@Path("/referral")
public class ReferralController implements IReferralController {
	@Inject
	CommonMethods commonMethods;

	@Inject
	IReferralService inotifyService;

	@Override
	public ResponseModel notifyUser(ReferralEntity NotifyEntity) {
		ResponseModel responseModel = null;
		try {
			responseModel = inotifyService.notifyUser(NotifyEntity);
		} catch (Exception e) {
			responseModel = commonMethods.constructFailedMsg(e.getMessage());
		}
		return responseModel;
	}

}
