package in.codifi.api.restservice;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import in.codifi.api.config.ApplicationProperties;
import in.codifi.api.utilities.CommonMethods;

@ApplicationScoped
public class SmsRestService {
	@Inject
	@RestClient
	ISmsRestService iSmsRestService;
	@Inject
	ApplicationProperties props;
	@Inject
	CommonMethods commonMethods;
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
	/**
	 * Method to referral link to Mobile Number
	 * 
	 * @author Nila
	 * @param referral link	
	 * @param mobile Number
	 * @return
	 */
	public void sendSms(String link, long mobileNumber) {
		try {
			// Dear Customer, Thanks for choosing Chola Securities for your Investments.
			// Please click on the link {#var#} to complete your online account opening
			// process.
			// String Text = props.getSmsRefFisrtText()+" "+otp+"
			// "+props.getSmsRefSecondText();
			// [13:45] Pradeep Ravichandran

//			String Text = "Dear Customer, Thanks for choosing Sky for your Investments. Please click on the link {#var#} to complete your online account opening process.-NIDHI";
			String Text = "Dear Customer, Thanks for choosing Sky for your Investments. Please click on the link " + link
					+ " to complete your online account opening process.-NIDHI";
			String message = iSmsRestService.SendSms(props.getSmsFeedId(), props.getSmsSenderId(),
					props.getSmsUserName(), props.getSmsPassword(), String.valueOf(mobileNumber), Text);
			commonMethods.storeSmsLog(Text,message,"sendSmsToReferral",mobileNumber);
			System.out.println(message);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
