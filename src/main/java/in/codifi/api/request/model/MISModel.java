package in.codifi.api.request.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MISModel {

	public String date;
	public String totalAccOpen;
	public String totalAccEsign;
	public String totalAccBo;
	
	public long totalAccOpenCount;
	public long totalAccEsignCount;
	public long totalAccBoCount;
}
