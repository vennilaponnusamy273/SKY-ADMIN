package in.codifi.api.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import in.codifi.api.entity.ApplicationUserEntity;
import in.codifi.api.repository.ApplicationUserRepository;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.AdminServiceSpec;
import in.codifi.api.utilities.MessageConstants;

@ApplicationScoped
public class AdminService implements AdminServiceSpec {

	
	@Inject
	ApplicationUserRepository applicationUserRepository;
	
	@Override
	public ResponseModel getUserDetails(long offset, long limit) {
	    ResponseModel response = new ResponseModel();
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
	    return response;
	}

}

	
	
   
