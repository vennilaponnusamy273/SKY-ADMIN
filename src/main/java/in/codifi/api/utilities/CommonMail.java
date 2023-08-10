package in.codifi.api.utilities;

import java.util.List;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import in.codifi.api.config.ApplicationProperties;
import in.codifi.api.repository.EmailTemplateRepository;

@ApplicationScoped
public class CommonMail {

	@Inject
	ApplicationProperties props;
	@Inject
	EmailTemplateRepository emailTemplateRepository;
	@Inject
	CommonMethods commonMethods;
//	@Inject
//	Mailer mailer;

	public String sendMail(List<String> mailIds, String subject, String msg) {
		StringBuilder builder = new StringBuilder();
		String success = EkycConstants.FAILED_MSG;
		try {
			Properties properties = new Properties();
			// Setup mail server
			properties.put(EkycConstants.CONST_MAIL_HOST, props.getMailHost());
			properties.put(EkycConstants.CONST_MAIL_USER, props.getMailUserName());
			properties.put(EkycConstants.CONST_MAIL_PORT, props.getMailPort());
			properties.put(EkycConstants.CONST_MAIL_SOC_FAC_PORT, props.getMailPort());
			properties.put(EkycConstants.CONST_MAIL_AUTH, EkycConstants.TRUE);
			properties.put(EkycConstants.CONST_MAIL_DEBUG, EkycConstants.TRUE);
			properties.put(EkycConstants.CONST_MAIL_STARTTLS_ENABLE, EkycConstants.REQUIRED);
			properties.put(EkycConstants.CONST_MAIL_SSL_PROTOCOLS, EkycConstants.CONST_MAIL_TLS_V2);

			// Create a Session instance with authenticator
			Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(props.getMailUserName(), props.getMailPassword());
				}
			});

			try {
				builder.append(msg);
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(props.getMailFrom()));
				message.addRecipients(Message.RecipientType.TO, getRecipients(mailIds));
				message.setSubject(subject);
				BodyPart messageBodyPart1 = new MimeBodyPart();
				messageBodyPart1.setContent(builder.toString(), EkycConstants.CONSTANT_TEXT_HTML);
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart1);
				message.setContent(multipart);

				// Use Transport.send() method to send the message
				Transport.send(message);
				success = EkycConstants.SUCCESS_MSG;
				commonMethods.storeEmailLog(msg, subject, success, subject, constructEmailIds(mailIds));
			} catch (MessagingException ex) {
				ex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	private static Address[] getRecipients(List<String> emails) {
		Address[] addresses = new Address[emails.size()];
		try {
			for (int i = 0; i < emails.size(); i++) {
				addresses[i] = new InternetAddress(emails.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addresses;
	}

	/**
	 * Method to add comma seperated for mail Ids
	 * 
	 * @param emails
	 * @return
	 */
	private String constructEmailIds(List<String> emails) {
		StringBuffer mailId = new StringBuffer();
		try {
			for (int i = 0; i < emails.size(); i++) {
				mailId.append(emails.get(i));
				mailId.append(",");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailId.toString();
	}

}
