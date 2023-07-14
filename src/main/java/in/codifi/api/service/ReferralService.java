package in.codifi.api.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import in.codifi.api.config.ApplicationProperties;
import in.codifi.api.entity.ApplicationUserEntity;
import in.codifi.api.entity.ReferralEntity;
import in.codifi.api.repository.ApplicationUserRepository;
import in.codifi.api.repository.ReferralRepository;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.restservice.ISmsRestService;
import in.codifi.api.service.spec.IReferralService;
import in.codifi.api.utilities.CommonMail;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.MessageConstants;
import in.codifi.api.utilities.StringUtil;

@ApplicationScoped
public class ReferralService implements IReferralService {

	private static final Logger logger = LogManager.getLogger(GetUserService.class);

	@Inject
	CommonMethods commonMethods;
	@Inject
	CommonMail commonMail;
	@Inject
	ApplicationProperties props;
	@Inject
	ApplicationUserRepository applicationUserRepository;
	@Inject
	ReferralRepository notifyRepository;
	@Inject
	@RestClient
	ISmsRestService ismsRestService;

	@Override
	public ResponseModel notifyUser(ReferralEntity NotifyEntity) {
		ResponseModel response = new ResponseModel();
		try {
			ApplicationUserEntity applicationUserEntity = applicationUserRepository
					.findByMobileNo(NotifyEntity.getMobileNo());
			if (applicationUserEntity == null) {
				ReferralEntity existingNotifyEntity = notifyRepository.findByMobileNo(NotifyEntity.getMobileNo());
				if (existingNotifyEntity == null) {
					if (NotifyEntity.getMobileNo() != null && NotifyEntity.getMobileNo() > 0) {
						NotifyEntity.setEmailId(NotifyEntity.getEmailId());
						NotifyEntity.setIsNodify(1);
						NotifyEntity.setMobileNo(NotifyEntity.getMobileNo());
						NotifyEntity.setReferralName(NotifyEntity.getReferralName());
						notifyRepository.save(NotifyEntity);
						sendMessagetoMobile("test", NotifyEntity.getMobileNo());
						if (StringUtil.isNotNullOrEmpty(NotifyEntity.getEmailId())) {
							List<String> toAdd = new ArrayList<>();
							toAdd.add(NotifyEntity.getEmailId());
							commonMail.sendMail(toAdd, "test", "test");
						}
						response.setStat(MessageConstants.SUCCESS_STATUS);
						response.setMessage(MessageConstants.SUCCESS_MSG);
						response.setResult(NotifyEntity);
						response.setReason("Nodification send successfully");
					} else {
						response.setStat(MessageConstants.SUCCESS_STATUS);
						response.setMessage(MessageConstants.SUCCESS_MSG);
						response.setReason("Mobile number Is Empty");
					}
				} else {
					response.setStat(MessageConstants.SUCCESS_STATUS);
					response.setMessage(MessageConstants.SUCCESS_MSG);
					response.setReason("Already details Available in Referral Table");
				}
			} else {
				response.setStat(MessageConstants.SUCCESS_STATUS);
				response.setMessage(MessageConstants.SUCCESS_MSG);
				response.setReason("MobileNumber already available in Master table");
			}
		} catch (Exception e) {
			logger.error("An error occurred: " + e.getMessage());
			commonMethods.SaveLog(null, "nodifyService", "modifyUser", e.getMessage());
			commonMethods.sendErrorMail(
					"An error occurred while processing your request. In EkycAdmin modifyUser for the Error: "
							+ e.getMessage(),
					"ERR-001");
			response = commonMethods.constructFailedMsg(e.getMessage());
		}
		return response;
	}

	public void sendMessagetoMobile(String Message, long mobileNumber) {
		try {
			String Text = Message + " " + props.getSmsText();
			String message = ismsRestService.SendSms(props.getSmsFeedId(), props.getSmsSenderId(),
					props.getSmsUserName(), props.getSmsPassword(), String.valueOf(mobileNumber), Text);
			System.out.println(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
