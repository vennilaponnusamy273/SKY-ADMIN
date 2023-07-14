package in.codifi.api.service.spec;
import in.codifi.api.entity.notifyEntity;
import in.codifi.api.response.model.ResponseModel;

public interface InotifyService {

	/**
	 * Method to nodify to MEssage and Email 
	 * 
	 * @author VennilA
	 * @param notifyEntity
	 * @return
	 */
	ResponseModel nodifyUser(notifyEntity NotifyEntity);
}
