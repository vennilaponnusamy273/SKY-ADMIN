package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import in.codifi.api.controller.spec.IBoApiController;
import in.codifi.api.service.spec.IBoApiService;

@Path("/boupdate")
public class BoApiController  implements IBoApiController{

	@Inject
	IBoApiService iBoApiService;
	
	@Override
	public Object getCustomerLedger(String ucc, String fromdate, String todate) {
		Object response = null;
		response=iBoApiService.getCustomerLedger(ucc,fromdate,todate);
		return response;
	}


	@Override
	public Object getCustomerDls(String ucc) {
		Object response = null;
		response=iBoApiService.getCustomerDls(ucc);
		return response;
	}


	@Override
	public Object getTradebookDls(String fromdate, String todate, String ucc, String branch, String region_code,
			String zone, String segment, String symbol, String buysell) {
		Object response = null;
		response=iBoApiService.getTradebookDls(fromdate,todate,ucc,branch,region_code,zone,segment,symbol,buysell);
		return response;
	}


	@Override
	public Object getBranchData(String branch_code) {
		Object response = null;
		response=iBoApiService.getBranchData(branch_code);
		return response;
	}


	@Override
	public Object CustomerDP(String ucc) {
		Object response = null;
		response=iBoApiService.CustomerDP(ucc);
		return response;
	}


	@Override
	public Object UpdateNomineeDetails(String ucc) {
		Object response = null;
		response=iBoApiService.UpdateNomineeDetails(ucc);
		return response;
	}


	@Override
	public Object positionsDetails(String ucc) {
		Object response = null;
		response=iBoApiService.positionsDetails(ucc);
		return response;
	}


	@Override
	public Object holdingsDetails(String ucc) {
		Object response = null;
		response=iBoApiService.holdingsDetails(ucc);
		return response;
	}


}
