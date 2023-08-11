package in.codifi.api.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.api.entity.EmailLogEntity;
import in.codifi.api.entity.SmsLogEntity;
import in.codifi.api.repository.EmailLogRepository;
import in.codifi.api.repository.SmsLogRepository;
import in.codifi.api.request.model.SmsEmailReqModel;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.ISmsAndEmailLogService;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.MessageConstants;
import in.codifi.api.utilities.StringUtil;

@ApplicationScoped
public class SmsAndEmailLogService implements ISmsAndEmailLogService {
	@Inject
	CommonMethods commonMethods;
	@Inject
	SmsLogRepository smsLogRepository;
	@Inject
	EmailLogRepository emailLogRepository;

	@Override
	public ResponseModel getLog(SmsEmailReqModel reqModel) {
		ResponseModel response = new ResponseModel();
		try {
			if (reqModel.getType().equalsIgnoreCase("SMS")) {
				response = getSmsLog(reqModel);
			} else if (reqModel.getType().equalsIgnoreCase("EMAIL")) {
				response = getEmailLog(reqModel);
			}
		} catch (Exception e) {
			response = commonMethods.constructFailedMsg(e.getMessage());
			response.setMessage(EkycConstants.FAILED_MSG);
			response.setStat(EkycConstants.FAILED_STATUS);
		}
		return response;
	}

	private ResponseModel getSmsLog(SmsEmailReqModel reqModel) {
		ResponseModel response = new ResponseModel();
		try {
			long count = smsLogRepository.count();
			if (reqModel.getOffset() >= count) {
				response.setResult(MessageConstants.OFFSETEXIT);
				return response;
			}
			Date startDate;
			Date endDate;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (!isCorrectDateFormat(reqModel.getFromDate())) {
				startDate = dateFormat.parse(reqModel.getFromDate() + MessageConstants.START_TIME);
			} else {
				startDate = dateFormat.parse(reqModel.getFromDate());
			}

			if (!isCorrectDateFormat(reqModel.getToDate())) {
				endDate = dateFormat.parse(reqModel.getToDate() + MessageConstants.END_TIME);
			} else {
				endDate = dateFormat.parse(reqModel.getToDate());
			}
			List<SmsLogEntity> totalResult = null;
			if (StringUtil.isNotNullOrEmpty(reqModel.getValue())) {
				totalResult = smsLogRepository.findByCreatedOnBetweenAndMobileNo(startDate, endDate,
						Long.parseLong(reqModel.getValue()));
			} else {
				totalResult = smsLogRepository.findByCreatedOnBetween(startDate, endDate);
			}

			int startIndex = reqModel.getOffset();
			int endIndex = Math.min(reqModel.getOffset() + reqModel.getLimit(), totalResult.size());
			List<SmsLogEntity> resultList = totalResult.subList(startIndex, endIndex);
			response.setResult(resultList);
			response.setMessage("SMS logs retrieved successfully.");
			response.setStat(EkycConstants.SUCCESS_STATUS);
		} catch (Exception e) {
			response = commonMethods.constructFailedMsg(e.getMessage());
			response.setMessage(EkycConstants.FAILED_MSG);
			response.setStat(EkycConstants.FAILED_STATUS);
		}
		return response;
	}

	public boolean isCorrectDateFormat(String date) {
		// Regular expression to match "yyyy-MM-dd HH:mm:ss" format
		String correctDate = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
		return date.matches(correctDate);
	}

	private ResponseModel getEmailLog(SmsEmailReqModel reqModel) {
		ResponseModel response = new ResponseModel();
		try {
			long count = emailLogRepository.count();
			if (reqModel.getOffset() >= count) {
				response.setResult(MessageConstants.OFFSETEXIT);
				return response;
			}
			Date startDate;
			Date endDate;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (!isCorrectDateFormat(reqModel.getFromDate())) {
				startDate = dateFormat.parse(reqModel.getFromDate() + MessageConstants.START_TIME);
			} else {
				startDate = dateFormat.parse(reqModel.getFromDate());
			}
			if (!isCorrectDateFormat(reqModel.getToDate())) {
				endDate = dateFormat.parse(reqModel.getToDate() + MessageConstants.END_TIME);
			} else {
				endDate = dateFormat.parse(reqModel.getToDate());
			}
			List<EmailLogEntity> totalResult = null;
			if (StringUtil.isNotNullOrEmpty(reqModel.getValue())) {
				totalResult = emailLogRepository.findByCreatedOnBetweenAndEmailId(startDate, endDate,
						reqModel.getValue());
			} else {
				totalResult = emailLogRepository.findByCreatedOnBetween(startDate, endDate);
			}
			int startIndex = reqModel.getOffset();
			int endIndex = Math.min(reqModel.getOffset() + reqModel.getLimit(), totalResult.size());
			List<EmailLogEntity> resultList = totalResult.subList(startIndex, endIndex);
			response.setResult(resultList);
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
