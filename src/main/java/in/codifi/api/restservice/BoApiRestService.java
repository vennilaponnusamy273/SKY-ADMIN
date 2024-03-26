package in.codifi.api.restservice;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import in.codifi.api.config.ApplicationProperties;
import in.codifi.api.utilities.CommonMethods;

@ApplicationScoped
public class BoApiRestService {
	@Inject
	CommonMethods commonMethods;
	
	@Inject
	@RestClient
	IBoApiRestService iboApiRestService;
	
	@Inject
	ApplicationProperties props;

	public Object getCustomerledger(String ucc, String fromdate, String todate) {
		Object responseModel = null;
		Response response = iboApiRestService.getCustomerLedger(props.getBoAuthToken(), ucc, fromdate, todate);
		int statusCode = response.getStatus();

		if (statusCode == 200) {
			responseModel = response.readEntity(String.class);
			System.out.println("The getCustomerLedger responseBody" + responseModel);
		}
		return responseModel;
	}

	public Object getCustomerDls(String ucc) {
		Object responseModel = null;
		Response response = iboApiRestService.getCustomerDls(props.getBoAuthToken(), ucc);
		int statusCode = response.getStatus();

		if (statusCode == 200) {
			responseModel = response.readEntity(String.class);
			System.out.println("The getCustomerDls responseBody" + responseModel);
		}
		return responseModel;
	}

	public Object getTradebookDls(String fromdate, String todate, String ucc, String branch, String region_code,
			String zone, String segment, String symbol, String buysell) {
		Object responseModel = null;
		Response response = iboApiRestService.getTradebookDls(props.getBoAuthToken(),fromdate,todate,ucc,branch,region_code,zone,segment,symbol,buysell);
		int statusCode = response.getStatus();

		if (statusCode == 200) {
			responseModel = response.readEntity(String.class);
			System.out.println("The getCustomerDls responseBody" + responseModel);
		}
		return responseModel;
	}

	public Object getBranchData(String branch_code) {
			Object responseModel = null;
			Response response = iboApiRestService.getBranchData(props.getBoAuthToken(), branch_code);
			int statusCode = response.getStatus();

			if (statusCode == 200) {
				responseModel = response.readEntity(String.class);
				System.out.println("The getCustomerDls responseBody" + responseModel);
			}
			return responseModel;
	}

	public Object CustomerDP(String ucc) {
		Object responseModel = null;
		Response response = iboApiRestService.CustomerDP(props.getBoAuthToken(), ucc);
		int statusCode = response.getStatus();

		if (statusCode == 200) {
			responseModel = response.readEntity(String.class);
			System.out.println("The getCustomerDls responseBody" + responseModel);
		}
		return responseModel;
}

	public Object UpdateNomineeDetails(String ucc) {
		Object responseModel = null;
		Response response = iboApiRestService.UpdateNomineeDetails(props.getBoAuthToken(), ucc);
		int statusCode = response.getStatus();

		if (statusCode == 200) {
			responseModel = response.readEntity(String.class);
			System.out.println("The getCustomerDls responseBody" + responseModel);
		}
		return responseModel;
}

	public Object positionsDetails(String ucc) {
		Object responseModel = null;
		Response response = iboApiRestService.positionsDetails(props.getBoAuthToken(), ucc);
		int statusCode = response.getStatus();

		if (statusCode == 200) {
			responseModel = response.readEntity(String.class);
			System.out.println("The getCustomerDls responseBody" + responseModel);
		}
		return responseModel;
}

	public Object holdingsDetails(String ucc) {
		Object responseModel = null;
		Response response = iboApiRestService.holdingsDetails(props.getBoAuthToken(), ucc);
		int statusCode = response.getStatus();

		if (statusCode == 200) {
			responseModel = response.readEntity(String.class);
			System.out.println("The getCustomerDls responseBody" + responseModel);
		}
		return responseModel;
}
}
