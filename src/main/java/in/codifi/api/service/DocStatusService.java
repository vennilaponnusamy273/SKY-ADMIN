package in.codifi.api.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.codifi.api.entity.DocStatusEntity;
import in.codifi.api.entity.DocumentEntity;
import in.codifi.api.repository.DocStatusRepository;
import in.codifi.api.repository.DocumentEntityRepository;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.DocStatusServiceSpec;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.MessageConstants;

@ApplicationScoped
public class DocStatusService implements DocStatusServiceSpec {

	
	private static final Logger logger = LogManager.getLogger(DocStatusService.class);
	
	@Inject
	DocStatusRepository docStatusRepository;
	
	@Inject 
	DocumentEntityRepository documentEntityRepository; 
	
	@Inject
	CommonMethods commonMethods;
	
	@Override
	public ResponseModel saveDoctatus(DocStatusEntity docStatusEntity) {
		ResponseModel response=new ResponseModel();
		try {
		if (docStatusEntity.getIsApproval() == 1)
		{
			docStatusEntity.setIsRejected(0);
		}
		else if(docStatusEntity.getIsRejected()==1)
		{
			docStatusEntity.setIsApproval(0);;
		}
		   
		DocStatusEntity DocEntity=new DocStatusEntity();
		DocumentEntity documents = documentEntityRepository.findByApplicationIdAndDocumentType(docStatusEntity.getApplicationId(), docStatusEntity.getTypeDoc());
		if(documents!=null) {
		DocStatusEntity checkExit=docStatusRepository.findByApplicationIdAndTypeDoc(docStatusEntity.getApplicationId(),docStatusEntity.getTypeDoc());
		if (checkExit!=null)
		{
			docStatusEntity.setId(checkExit.getId());
			DocEntity=docStatusRepository.save(docStatusEntity);
		}
		else
		{
			DocEntity=docStatusRepository.save(docStatusEntity);
		}
		if (DocEntity != null && DocEntity.getId() > 0) {
			response.setMessage(MessageConstants.SUCCESS_MSG);
			response.setStat(MessageConstants.SUCCESS_STATUS);
			response.setResult(DocEntity);
		}
		}
		else
		{
			response.setStat(MessageConstants.FAILED_STATUS);
			response.setMessage(MessageConstants.FAILED_MSG);
			response.setResult(MessageConstants.NOT_DOC_AVAILABLE);
		}
		} catch (Exception e) {
				logger.error("An error occurred: " + e.getMessage());
				commonMethods.SaveLog(docStatusEntity.getApplicationId(),"DocStatusService","saveDoctatus",e.getMessage());
				commonMethods.sendErrorMail("An error occurred while processing your request. In EkycAdmin saveDoctatus for the Error: " + e.getMessage(),"ERR-001");
				response = commonMethods.constructFailedMsg(e.getMessage());
		}
		return response;
	}

	@Override
	public ResponseModel getdocDetails(long applicationId) {
	    ResponseModel responseModel = new ResponseModel();
	    try {
	        List<DocStatusEntity> docStatusEntities = docStatusRepository.findByApplicationId(applicationId);
	        if (!docStatusEntities.isEmpty()) {
	            responseModel.setMessage(MessageConstants.SUCCESS_MSG);
	            responseModel.setStat(MessageConstants.SUCCESS_STATUS);
	            responseModel.setResult(docStatusEntities);
	        } else {
	            responseModel.setMessage("Document details are not available");
	            responseModel.setStat(MessageConstants.SUCCESS_STATUS);
	            responseModel.setResult(null);    
	        }
	    } catch (Exception e) {
	    	responseModel = commonMethods.constructFailedMsg(e.getMessage());
	        logger.error("An error occurred: ", e);
	        commonMethods.SaveLog(applicationId, "DocStatusService", "getDocDetails", e.getMessage());
	        commonMethods.sendErrorMail("An error occurred while processing your request. In EkycAdmin getDocDetails for the Error: " + e.getMessage(),"ERR-001");
	    }
	    return responseModel;
	}
}
