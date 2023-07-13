package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;
import in.codifi.api.controller.spec.InotifyController;
import in.codifi.api.entity.notifyEntity;
import in.codifi.api.response.model.ResponseModel;
import in.codifi.api.service.spec.InotifyService;
import in.codifi.api.utilities.CommonMethods;

@Path("/nodifyUser")
public class notifyController implements InotifyController{
	@Inject
	CommonMethods commonMethods;
	
	@Inject
	InotifyService inotifyService;
	
	@Override
	public ResponseModel nodifyUser(notifyEntity NotifyEntity) {
		ResponseModel responseModel = null;
		try {
		responseModel = inotifyService.nodifyUser(NotifyEntity);
		} catch (Exception e) {
			responseModel = commonMethods.constructFailedMsg(e.getMessage());
		}
		return responseModel;
	}

}
