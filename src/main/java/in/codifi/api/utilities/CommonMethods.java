package in.codifi.api.utilities;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.api.entity.EmailTemplateEntity;
import in.codifi.api.entity.ErrorLogEntity;
import in.codifi.api.repository.EmailTemplateRepository;
import in.codifi.api.repository.ErrorLogRepository;
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
}
