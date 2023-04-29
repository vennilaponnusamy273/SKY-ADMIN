package in.codifi.api.service.spec;

import in.codifi.api.response.model.ResponseModel;

public interface GetUserServiceSpec {

	ResponseModel getUserDetails(long offset, long limit);
}
