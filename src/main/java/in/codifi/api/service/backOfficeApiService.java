package in.codifi.api.service;
import java.io.IOException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.slf4j.LoggerFactory;
import in.codifi.api.config.ApplicationProperties;
import in.codifi.api.controller.spec.IAdminController;
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

	@Inject
	CommonMethods commonMethods;

	@Inject
	backOfficeHelper ibackOfficeHelper;
	
	@Inject
	IAdminController iAdminController;

	@Inject
	ApplicationProperties props;
	
	@Override
	public ResponseModel callBckOfficeAPI(long applicationId) {
	    ResponseModel responseModel = new ResponseModel();
	    org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

	    try {
	        String apiUrl =props.getBackofficeApi();
	        OkHttpClient client = new OkHttpClient();
	        MediaType mediaType = MediaType.parse("application/json");
	        String jsonContent = ibackOfficeHelper.generateJsonContenet(applicationId);
	        System.out.println("the jsonContent"+jsonContent);
	        logger.debug("JSON Content: {}", jsonContent);

	        RequestBody body = RequestBody.create(mediaType, jsonContent);

	        Request request = new Request.Builder()
	                .url(apiUrl)
	                .post(body)
	                .addHeader("Content-Type", "application/json")
	                .build();

	        try (Response response = client.newCall(request).execute()) {
	            if (response.isSuccessful()) {
	                String responseBody = response.body().string();
	                iAdminController.sendRiskDoc(applicationId);
	                logger.debug("Response Body: {}", responseBody);
	                responseModel.setResult(responseBody);
	            } else {
	                logger.error("API request failed with status code: {}", response.code());
	                // Handle the error and set an appropriate response in responseModel
	                responseModel.setResult ("API request failed with status code: " + response.code());
	            }
	        } catch (IOException e) {
	            logger.error("An error occurred while making the API request.", e);
	            // Handle the exception and set an appropriate response in responseModel
	            responseModel.setResult("An error occurred while making the API request: " + e.getMessage());
	        }
	    } catch (Exception e) {
	        logger.error("An error occurred.", e);
	        // Handle the exception and set an appropriate response in responseModel
	        responseModel.setResult("An error occurred: " + e.getMessage());
	    }

	    return responseModel;
	}
};