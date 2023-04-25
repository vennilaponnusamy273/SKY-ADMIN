package in.codifi.api.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.codifi.api.entity.ApplicationUserEntity;
import in.codifi.api.repository.ApplicationUserRepository;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.GetUserServiceSpec;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.MessageConstants;

@ApplicationScoped
public class GetUserService implements GetUserServiceSpec {

	private static final Logger logger = LogManager.getLogger(GetUserService.class);
	@Inject
	ApplicationUserRepository applicationUserRepository;
	
	@Inject
	CommonMethods commonMethods;
	
	@Override
	public ResponseModel getUserDetails(long offset, long limit) {
	    ResponseModel response = new ResponseModel();
	    try {
	    long count = applicationUserRepository.count();
	    if (offset >= count) {
	        response.setStat(MessageConstants.SUCCESS_STATUS);
	        response.setMessage(MessageConstants.SUCCESS_MSG);
	        response.setResult(MessageConstants.OFFSETEXIT);
	        return response;
	    }
	    long end = Math.min(count, offset + limit);
	    List<ApplicationUserEntity> records = applicationUserRepository.findByIdBetween(offset+1, end);
	    response.setStat(MessageConstants.SUCCESS_STATUS);
	    response.setMessage(MessageConstants.SUCCESS_MSG);
	    response.setResult(records);
	    } catch (Exception e) {
			logger.error("An error occurred: " + e.getMessage());
			commonMethods.SaveLog(null,"GetUserService","getUserDetails",e.getMessage());
			commonMethods.sendErrorMail("An error occurred while processing your request. In EkycAdmin getUserDetails for the Error: " + e.getMessage(),"ERR-001");
			response = commonMethods.constructFailedMsg(e.getMessage());
		}
	    return response;
	}
}
