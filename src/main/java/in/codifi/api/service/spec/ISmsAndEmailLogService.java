package in.codifi.api.service.spec;

import javax.validation.constraints.NotNull;

import in.codifi.api.response.model.ResponseModel;


public interface ISmsAndEmailLogService {
	

	ResponseModel getLog(@NotNull long limit, @NotNull long offset, @NotNull String logType);

}
