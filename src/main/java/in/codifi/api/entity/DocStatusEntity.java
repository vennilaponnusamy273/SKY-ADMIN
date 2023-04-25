package in.codifi.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "tbl_docstatus_details")
@Getter
@Setter
public class DocStatusEntity extends CommonEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "application_id")
	private Long applicationId;

	@Column(name = "page_id")
	private Long pageId;
	
	@Column(name = "Type_Doc")
	private String typeDoc;

	
	@Column(name = "description")
	private String description;
	
	@Column(name = "isApproval")
	private int isApproval;
	
	@Column(name = "isRejected")
	private int isRejected;
	
	@Column(name = "rejectedReason")
	private String rejectedReason;
	
	@Column(name = "approved_By")
	private String  approvedBy;
	
	@Column(name = "rejected_By")
	private String rejectedBy;
}
