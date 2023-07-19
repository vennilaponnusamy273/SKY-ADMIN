package in.codifi.api.restservice;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONObject;

import in.codifi.api.config.ApplicationProperties;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.EkycConstants;


@ApplicationScoped
public class UrlShortnerRestService {
	private static final Logger logger = LogManager.getLogger(UrlShortnerRestService.class);
	@Inject
	CommonMethods commonMethods;
	@Inject
	ApplicationProperties properties;
	@Inject
	@RestClient
	IUrlShortnerRestService iUrlShortnerRestService;

	
	public String shortenUrl(String longUrl) {
		String response = null;
		try {
			String shortUrl = iUrlShortnerRestService.getUrlShortner(properties.getIvrUrlShortnerToken(),
					URLEncoder.encode(longUrl, StandardCharsets.UTF_8));
			JSONObject responseJson = new JSONObject(shortUrl);
			shortUrl = responseJson.getJSONObject(EkycConstants.URL).getString(EkycConstants.SHORT_URL);
			response= shortUrl;
		} catch (Exception e) {
			
			logger.error("An error occurred: " + e.getMessage());
			ResponseModel responseModel = new ResponseModel();
			responseModel = commonMethods.constructFailedMsg(e.getMessage());
			response = responseModel.toString();
		}
		return response;
	}
}
