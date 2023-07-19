package in.codifi.api.response.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlShortnerResponseModel {
	@JsonProperty("URL")
	private String url;
}
