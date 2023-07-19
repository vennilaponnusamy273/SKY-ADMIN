package in.codifi.api.request.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlShortnerModel {

	@JsonProperty("Url")
	private String url;
	@JsonProperty("Fdate")
	private String fdate;
	@JsonProperty("Tdate")
	private String tdate;
	@JsonProperty("Name")
	private String name;
}
