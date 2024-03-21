package in.codifi.api.request.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JourneyDetails {
	private Date createdOn; // Journey Start
	private Date signedDate; // Journey END
	private String name;
	private String pan;
	private String uccCode;
	private Date backOfficePushDate;
	private String mode;
	private String referralName;
	private String mobileNumber;
	private String email;
}
