package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import in.codifi.api.controller.spec.AdminControllerSpec;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.AdminServiceSpec;

@Path("/admin")
public class AdminController implements AdminControllerSpec {

	
	@Inject 
	AdminServiceSpec adminServiceSpec;
	
	@Path("/test")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String test() {
		String test = "Hello Vennila";
		return test;
	}

	@Override
	public ResponseModel getUserDetails(long offset, long limit) {
		ResponseModel response = new ResponseModel();
		response=adminServiceSpec.getUserDetails(offset,limit);
		return response;
	}

}
