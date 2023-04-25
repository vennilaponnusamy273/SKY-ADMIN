package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import in.codifi.api.controller.spec.DocStatusControllerSpec;
import in.codifi.api.entity.DocStatusEntity;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.DocStatusServiceSpec;
import in.codifi.api.utilities.MessageConstants;

@Path("/admin")
public class DocStatusController implements DocStatusControllerSpec{

	
	@Inject
	DocStatusServiceSpec docStatusServiceSpec;
	
	@Override
	public ResponseModel saveDocStatus(DocStatusEntity docStatusEntity) {
		ResponseModel response=new ResponseModel();
		if (docStatusEntity.getApplicationId()>0 &&docStatusEntity!=null) {
		response=docStatusServiceSpec.saveDoctatus(docStatusEntity);
		}
		else {
			response.setResult(MessageConstants.PARAMETER_NULL);
	    	response.setMessage(MessageConstants.FAILED_MSG);
	    	response.setReason(MessageConstants.DOC_ERROR);
	    }
		return response;
	}

	@Override
	public ResponseModel getDocDetails(long applicationId) {
		ResponseModel response=new ResponseModel();
		if (applicationId>0)
		{
			response=docStatusServiceSpec.getdocDetails(applicationId);
		}
		else {
			response.setResult(MessageConstants.PARAMETER_NULL);
	    	response.setMessage(MessageConstants.FAILED_MSG);
	    	response.setReason(MessageConstants.DOC_ERROR);
	    }
		return response;
	}

}
