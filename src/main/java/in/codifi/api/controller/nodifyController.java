package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import in.codifi.api.controller.spec.InodifyController;
import in.codifi.api.entity.notifyEntity;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.InodifyService;
import in.codifi.api.utilities.CommonMethods;

@Path("/nodifyUser")
public class nodifyController implements InodifyController{
	@Inject
	CommonMethods commonMethods;
	
	@Inject
	InodifyService inodifyService;
	
	@Override
	public ResponseModel nodifyUser(notifyEntity NotifyEntity) {
		ResponseModel responseModel = null;
		try {
		responseModel = inodifyService.nodifyUser(NotifyEntity);
		} catch (Exception e) {
			responseModel = commonMethods.constructFailedMsg(e.getMessage());
		}
		return responseModel;
	}

}
