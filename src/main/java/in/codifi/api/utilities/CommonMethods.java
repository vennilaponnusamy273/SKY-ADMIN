package in.codifi.api.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.api.config.ApplicationProperties;
import in.codifi.api.entity.EmailTemplateEntity;
import in.codifi.api.entity.ErrorLogEntity;
import in.codifi.api.repository.EmailTemplateRepository;
import in.codifi.api.repository.ErrorLogRepository;
import in.codifi.api.request.model.BankAddressModel;
import in.codifi.api.response.model.ResponseModel;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

@ApplicationScoped
public class CommonMethods {

	@Inject
	EmailTemplateRepository emailTemplateRepository;
	
	@Inject
	Mailer mailer;
	@Inject
	CommonMail commonMail;
	@Inject
	ApplicationProperties props;
	
	@Inject
	ErrorLogRepository errorLogRepository;
	
	public ResponseModel constructFailedMsg(String failesMessage) {
		ResponseModel model = new ResponseModel();
		model.setStat(MessageConstants.FAILED_STATUS);
		model.setMessage(MessageConstants.FAILED_MSG);
		model.setReason(failesMessage);
		return model;
	}
	
	public void SaveLog(Long applicationId, String className, String methodName, String reason) {
	    ErrorLogEntity errorLogEntity = errorLogRepository.findByApplicationIdAndClassNameAndMethodName(applicationId, className, methodName);
	    if (errorLogEntity == null) {
	        errorLogEntity = new ErrorLogEntity();
	        errorLogEntity.setApplicationId(applicationId);
	        errorLogEntity.setClassName(className);
	        errorLogEntity.setMethodName(methodName);
	    }
	    errorLogEntity.setReason(reason);
	    if(errorLogEntity != null) {
	        errorLogRepository.save(errorLogEntity);
	    }
	}
	
	@Inject
	public void MailService(Mailer javaMailSender) {
		this.mailer = javaMailSender;
	}

	
	public void sendErrorMail(String errorMessage, String errorCode) {
	    EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findByKeyData("error");
	    if (emailTemplateEntity != null && emailTemplateEntity.getBody() != null && emailTemplateEntity.getSubject() != null&&emailTemplateEntity.getToAddress()!=null) {
	        String bodyMessage = emailTemplateEntity.getBody();
	        String body = bodyMessage.replace("{errorMessage}", errorMessage).replace("{errorCode}", errorCode);
	        String subject = emailTemplateEntity.getSubject();
	        Mail mail = Mail.withHtml(emailTemplateEntity.getToAddress(), subject, body);
	     
	        if (emailTemplateEntity.getCc() != null) {
	        	String[] ccAddresses = emailTemplateEntity.getCc().split(",");
	        	for (String ccAddress : ccAddresses) {
	                mail = mail.addCc(ccAddress.trim());
	            }
	        }
	        mailer.send(mail);
	        System.out.println("The email was sent in error message: " + mail);
	    }
	}
	
	/**
	 * Method to find bank address by ifsc
	 * 
	 * @author Vennila
	 * @param ifscCode
	 * @return
	 */
	public BankAddressModel findBankAddressByIfsc(String ifscCode) {
		BankAddressModel model = null;
		try {
			URL url = new URL(props.getRazorpayIfscUrl() + ifscCode);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				return model;
			}
			BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br1.readLine()) != null) {
				ObjectMapper om = new ObjectMapper();
				model = om.readValue(output, BankAddressModel.class);
			}
		} catch (Exception e) {
			return model;
		}
		return model;
	}

	/**
	 * Method to send IPV Link to email
	 * 
	 * @param Url
	 * @param mobileNumber
	 */
	public void sendRejectionMail(String userName, String emailId) throws MessagingException {
		EmailTemplateEntity emailTempentity = emailTemplateRepository.findByKeyData("Rejection");
		String body_Message = emailTempentity.getBody();
		String body = body_Message.replace("{UserName}", userName);
		String subject = emailTempentity.getSubject();
		List<String> toAdd = new ArrayList<>();
		toAdd.add(emailId);
		commonMail.sendMail(toAdd, subject, body);
	}	
}
