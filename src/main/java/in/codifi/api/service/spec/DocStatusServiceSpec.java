package in.codifi.api.service.spec;

import in.codifi.api.entity.DocStatusEntity;
import in.codifi.api.response.model.ResponseModel;

public interface DocStatusServiceSpec {

	ResponseModel saveDoctatus(DocStatusEntity docStatusEntity);

	ResponseModel getdocDetails(long applicationId);

}
