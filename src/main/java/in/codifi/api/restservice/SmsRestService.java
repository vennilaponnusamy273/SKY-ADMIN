package in.codifi.api.restservice;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import in.codifi.api.config.ApplicationProperties;

@ApplicationScoped
public class SmsRestService {
	@Inject
	@RestClient
	ISmsRestService iSmsRestService;
	@Inject
	ApplicationProperties props;

	/**
	 * Method to send otp to Mobile Number
	 * 
	 * @author Nila
	 * @param otp
	 * @param mobile Number
	 * @return
	 */

//	public void sendOTPtoMobile(int otp, long mobileNumber) {
//		try {
//			String Text = otp + " " + props.getSmsText();
//			String message = iSmsRestService.SendSms(props.getSmsFeedId(), props.getSmsSenderId(),
//					props.getSmsUserName(), props.getSmsPassword(), String.valueOf(mobileNumber), Text);
//			System.out.println(message);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void sendSms(String otp, long mobileNumber) {
		try {
			// Dear Customer, Thanks for choosing Chola Securities for your Investments.
			// Please click on the link {#var#} to complete your online account opening
			// process.
			// String Text = props.getSmsRefFisrtText()+" "+otp+"
			// "+props.getSmsRefSecondText();
			// [13:45] Pradeep Ravichandran

//			String Text = "Dear Customer, Thanks for choosing Sky for your Investments. Please click on the link {#var#} to complete your online account opening process.-NIDHI";
			String Text = "Dear Customer, Thanks for choosing Sky for your Investments. Please click on the link " + otp
					+ " to complete your online account opening process.-NIDHI";
			String message = iSmsRestService.SendSms(props.getSmsFeedId(), props.getSmsSenderId(),
					props.getSmsUserName(), props.getSmsPassword(), String.valueOf(mobileNumber), Text);
			System.out.println(message);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
