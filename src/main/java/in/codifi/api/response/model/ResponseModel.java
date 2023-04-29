package in.codifi.api.response.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel {

	/**
	 * 
	 */
	private int stat;
	private String page;
	private String message;
	private String reason;
	private Object result;
}
