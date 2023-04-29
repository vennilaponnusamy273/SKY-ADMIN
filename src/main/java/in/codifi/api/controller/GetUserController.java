package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import in.codifi.api.controller.spec.GetUserControllerSpec;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.GetUserServiceSpec;

@Path("/admin")
public class GetUserController  implements GetUserControllerSpec{

	
	@Inject 
	GetUserServiceSpec getUserServiceSpec;
	
	@Override
	public ResponseModel getUserDetails(long offset, long limit) {
		ResponseModel response = new ResponseModel();
		response=getUserServiceSpec.getUserDetails(offset,limit);
		return response;
	}
}
