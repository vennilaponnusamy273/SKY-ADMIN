package in.codifi.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tbl_email_Log_details")
public class EmailLogEntity extends CommonEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "reqLog", length = 1000)
	private String reqLog;
	
	@Column(name = "reqLogSub", length = 1000)
	private String reqLogSub;
	
	@Column(name = "responseLog")
	private String responseLog;
	
	@Column(name = "logMethod")
	private String logMethod;
}
