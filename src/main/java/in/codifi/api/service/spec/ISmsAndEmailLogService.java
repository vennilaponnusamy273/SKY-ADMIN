package in.codifi.api.service.spec;

import javax.validation.constraints.NotNull;

import in.codifi.api.request.model.SmsEmailReqModel;
import in.codifi.api.response.model.ResponseModel;

public interface ISmsAndEmailLogService {

	ResponseModel getLog(@NotNull SmsEmailReqModel reqModel);

}
