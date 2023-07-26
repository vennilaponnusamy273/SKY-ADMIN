package in.codifi.api.service;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import in.codifi.api.entity.EmailLogEntity;
import in.codifi.api.entity.SmsLogEntity;
import in.codifi.api.repository.EmailLogRepository;
import in.codifi.api.repository.SmsLogRepository;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.ISmsAndEmailLogService;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.MessageConstants;

@ApplicationScoped
public class SmsAndEmailLogService  implements ISmsAndEmailLogService{
	@Inject
	CommonMethods commonMethods;
	@Inject
	SmsLogRepository smsLogRepository;
	@Inject
	EmailLogRepository emailLogRepository;
	
	@Override
	public ResponseModel getLog(@NotNull long limit, @NotNull long offset, @NotNull String logType) {
	    ResponseModel response = new ResponseModel();
	    try {
	        if (!isValidLogType(logType)) {
	            response = commonMethods.constructFailedMsg("Invalid log type. Please use SMS or EMAIL.");
	        } else {
	            if (logType.equalsIgnoreCase("SMS")) {
	                response = getSmsLog(limit, offset);
	            } else if (logType.equalsIgnoreCase("EMAIL")) {
	                response = getEmailLog(limit, offset);
	            }
	        }
	    } catch (Exception e) {
	        response = commonMethods.constructFailedMsg(e.getMessage());
	        response.setMessage(EkycConstants.FAILED_MSG);
	        response.setStat(EkycConstants.FAILED_STATUS);
	    }
	    return response;
	}

	private boolean isValidLogType(String logType) {
	    return logType != null && (logType.equalsIgnoreCase("SMS") || logType.equalsIgnoreCase("EMAIL"));
	}

	private ResponseModel getSmsLog(@NotNull long limit, @NotNull long offset) {
	    ResponseModel response = new ResponseModel();
	    try {
	        long count = smsLogRepository.count();
	        if (offset >= count) {
	            response.setResult(MessageConstants.OFFSETEXIT);
	            return response;
	        }
	        long end = Math.min(count, offset + limit);
	        List<SmsLogEntity> smsLogPage = smsLogRepository.findByIdBetween(offset + 1, end);
	        response.setResult(smsLogPage);
	    } catch (Exception e) {
	        response = commonMethods.constructFailedMsg(e.getMessage());
	        response.setMessage(EkycConstants.FAILED_MSG);
	        response.setStat(EkycConstants.FAILED_STATUS);
	    }
	    return response;
	}

	private ResponseModel getEmailLog(@NotNull long limit, @NotNull long offset) {
	    ResponseModel response = new ResponseModel();
	    try {
	        long count = emailLogRepository.count();
	        if (offset >= count) {
	            response.setResult(MessageConstants.OFFSETEXIT);
	            return response;
	        }
	        long end = Math.min(count, offset + limit);
	        List<EmailLogEntity> emailLogs = emailLogRepository.findByIdBetween(offset + 1, end);
	        response.setResult(emailLogs);
	        response.setMessage("Email logs retrieved successfully.");
	        response.setStat(EkycConstants.SUCCESS_STATUS);
	    } catch (Exception e) {
	        response = commonMethods.constructFailedMsg(e.getMessage());
	        response.setMessage(EkycConstants.FAILED_MSG);
	        response.setStat(EkycConstants.FAILED_STATUS);
	    }
	    return response;
	}

}
