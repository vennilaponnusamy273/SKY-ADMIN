package in.codifi.api.service;

import java.io.File;
import java.net.URLConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

import in.codifi.api.config.ApplicationProperties;
import in.codifi.api.entity.DocumentEntity;
import in.codifi.api.entity.IvrEntity;
import in.codifi.api.entity.TxnDetailsEntity;
import in.codifi.api.repository.DocumentEntityRepository;
import in.codifi.api.repository.IvrRepository;
import in.codifi.api.repository.TxnDetailsRepository;
import in.codifi.api.service.spec.IDocDownloadService;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.MessageConstants;
import in.codifi.api.utilities.StringUtil;

@ApplicationScoped
public class DocDownloadService implements IDocDownloadService {

	private static String OS = System.getProperty("os.name").toLowerCase();
	

	@Inject 
	DocumentEntityRepository docrepository;
	
	@Inject
	ApplicationProperties props;
	
	@Inject
	CommonMethods commonMethods;
	
	@Inject
	IvrRepository ivrRepository;
	@Inject
	TxnDetailsRepository txnDetailsRepository;
	/**
	 * Method to download file
	 */
	
	@Override
	public Response downloadFile(@NotNull long applicationId, @NotNull String type) {
	    try {
	        String attachmentType = null;
	        String slash = EkycConstants.UBUNTU_FILE_SEPERATOR;
	        
	        if (OS.contains(EkycConstants.OS_WINDOWS)) {
	            slash = EkycConstants.WINDOWS_FILE_SEPERATOR;
	        }
	        
	        if (type.equalsIgnoreCase(EkycConstants.DOC_IVR)) {
	            IvrEntity ivrEntity = ivrRepository.findByApplicationIdAndDocumentType(applicationId, type);
	            if (ivrEntity == null) {
	                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                        .entity(MessageConstants.FILE_NOT_FOUND)
	                        .build();
	            }
	            attachmentType = ivrEntity.getAttachement();
	        } else if(type.equalsIgnoreCase(EkycConstants.DOC_ESIGN)) {
	        	String TxnDetailsEntityFile=txnDetailsRepository.findsinglefilefolder(applicationId);
	        	if(TxnDetailsEntityFile!=null) {
	        	  return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                        .entity(MessageConstants.FILE_NOT_FOUND)
	                        .build();
	        	}
	        	int lastIndex = TxnDetailsEntityFile.lastIndexOf("\\");
	        	 attachmentType = TxnDetailsEntityFile.substring(lastIndex + 1);
	        	
	        }else {
	        	DocumentEntity document = docrepository.findByApplicationIdAndDocumentType(applicationId, type);
	            if (document == null) {
	                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                        .entity(MessageConstants.FILE_NOT_FOUND)
	                        .build();
	            }
	            attachmentType = document.getAttachement();
	        }
	        
	        if (StringUtil.isNotNullOrEmpty(attachmentType)) {
	            String path = props.getFileBasePath() + applicationId + slash + attachmentType;
	            System.out.println("the path: " + path);
	            
	            File file = new File(path);
	            String contentType = URLConnection.guessContentTypeFromName(attachmentType);
	            
	            return Response.ok(file)
	                    .type(contentType)
	                    .header("Content-Disposition", "attachment; filename=" + file.getName())
	                    .build();
	        } else {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity(MessageConstants.FILE_NOT_FOUND)
	                    .build();
	        }
	    } catch (Exception e) {
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Failed to download file: " + e.getMessage())
	                .build();
	    }
	}

}
