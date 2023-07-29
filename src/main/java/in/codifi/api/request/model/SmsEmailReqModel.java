package in.codifi.api.request.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsEmailReqModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int limit;
	private int offset;
	private String type;
	private String fromDate;
	private String toDate;
	private long  mobileNumber;
	private String  emailiD;
}
