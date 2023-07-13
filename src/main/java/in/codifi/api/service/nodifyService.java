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
import in.codifi.api.entity.notifyEntity;
import in.codifi.api.repository.ApplicationUserRepository;
import in.codifi.api.repository.nodifyRepository;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.restservice.ISmsRestService;
import in.codifi.api.service.spec.InodifyService;
import in.codifi.api.utilities.CommonMail;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.MessageConstants;

@ApplicationScoped
public class nodifyService implements InodifyService  {

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
	nodifyRepository NodifyRepository;
	@Inject
	@RestClient
	ISmsRestService ismsRestService;
	
	@Override
	public ResponseModel nodifyUser(notifyEntity NotifyEntity) {
	    ResponseModel response = new ResponseModel();
	    boolean sendMail = false;
	    boolean sendMessage = false;
	    try {
	        ApplicationUserEntity applicationUserEntity = applicationUserRepository.findByMobileNo(NotifyEntity.getMobileNo());
	        if (applicationUserEntity != null) {
	        	notifyEntity existingNotifyEntity = NodifyRepository.findByMobileNo(NotifyEntity.getMobileNo());
	            if (existingNotifyEntity == null && NotifyEntity.getIsNodify() == 1) {
	                if (NotifyEntity.getEmailId() != null) {
	                    NotifyEntity.setEmailId(NotifyEntity.getEmailId());
	                    NotifyEntity.setIsNodify(1);
	                    NotifyEntity.setMobileNo(NotifyEntity.getMobileNo());
	                    NotifyEntity.setReferralName(NotifyEntity.getReferralName());
	                    NodifyRepository.save(NotifyEntity);
	                    sendMail = true;
	                    sendMessage = true;
	                    
	    	            if (sendMessage) {
	    	            	sendMessagetoMobile("test", NotifyEntity.getMobileNo());
	    	            }
	    	            if (sendMail) {
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
	                    response.setReason("MailID Is Empty");
	                }
	            }
	            if (existingNotifyEntity != null) {
	                response.setStat(MessageConstants.SUCCESS_STATUS);
	                response.setMessage(MessageConstants.SUCCESS_MSG);
	                response.setReason("Already details Available");
	            }
	    }else {
            response.setStat(MessageConstants.SUCCESS_STATUS);
            response.setMessage(MessageConstants.SUCCESS_MSG);
            response.setReason("MobileNumber Not available in Master table");
        }
	    } catch (Exception e) {
	        logger.error("An error occurred: " + e.getMessage());
	        commonMethods.SaveLog(null, "nodifyService", "modifyUser", e.getMessage());
	        commonMethods.sendErrorMail("An error occurred while processing your request. In EkycAdmin modifyUser for the Error: " + e.getMessage(), "ERR-001");
	        response = commonMethods.constructFailedMsg(e.getMessage());
	    }
	    return response;
	}
	public void sendMessagetoMobile(String  Message, long mobileNumber) {
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
