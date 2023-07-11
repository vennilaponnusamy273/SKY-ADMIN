package in.codifi.api.service;

import java.io.IOException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.codifi.api.helper.backOfficeHelper;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.IbackOfficeApiService;
import in.codifi.api.utilities.CommonMethods;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@ApplicationScoped
public class backOfficeApiService implements IbackOfficeApiService {

	private static final Logger logger = LogManager.getLogger(backOfficeApiService.class);

	@Inject
	CommonMethods commonMethods;

	@Inject
	backOfficeHelper ibackOfficeHelper;

	@Override
	public ResponseModel callBckOfficeAPI(long applicationId) {
		ResponseModel responseModel = new ResponseModel();
		try {
			String apiUrl = "https://bo.skybroking.com/shrdbms/dotnet/api/stansoft/{MODULE}";
			String key = "ezM0OTM4Q0Y1LUIyNUItNDhFMi1CNEU2LTRDQkY5MjhGQjE2M30=";

			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/xml");
			String xmlContent = ibackOfficeHelper.generateXMLContent(applicationId);
			RequestBody body = RequestBody.create(mediaType, xmlContent);
			Request request = new Request.Builder().url(apiUrl).post(body).addHeader("Content-Type", "application/xml")
					.addHeader("Authorization", "Bearer " + key).build();
			try (Response response = client.newCall(request).execute()) {
				if (response.isSuccessful()) {
					System.out.println("API request successful");
					String responseBody = response.body().string();
					responseModel.setResult(responseBody);
				} else {
					System.out.println("API request failed with status code: " + response.code());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			responseModel = commonMethods.constructFailedMsg(e.getMessage());
			logger.error("An error occurred: ", e);
			commonMethods.SaveLog(applicationId, "backOfficeApiService", "callBckOfficeAPI", e.getMessage());
			commonMethods.sendErrorMail(
					"An error occurred while processing your request. In backOfficeApiService callBckOfficeAPI for the Error: "
							+ e.getMessage(),
					"ERR-002");
		}
		return responseModel;
	}

};