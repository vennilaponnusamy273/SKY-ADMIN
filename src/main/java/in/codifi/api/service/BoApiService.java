package in.codifi.api.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.codifi.api.restservice.BoApiRestService;
import in.codifi.api.service.spec.IBoApiService;
import in.codifi.api.utilities.CommonMethods;

@ApplicationScoped
public class BoApiService implements IBoApiService {

	private static final Logger logger = LogManager.getLogger(BoApiService.class);
	
	@Inject
	BoApiRestService boApiRestService;

	@Inject
	CommonMethods commonMethods;

	

	public Object getCustomerLedger(String ucc, String fromdate, String todate) {
		Object responseModel = null;
		try {
			responseModel = boApiRestService.getCustomerledger(ucc, fromdate, todate);
		} catch (Exception e) {
			//logger.error("An error occurred: " + e.getMessage());
//			commonMethods.sendErrorMail(
//					"An error occurred while processing your request, In getCustomerLedger for the Error: "
//							+ e.getMessage(),
//					"ERR-002");
			e.printStackTrace();
		}
		return responseModel;

	}

	@Override
	public Object getCustomerDls(String ucc) {
		Object responseModel = null;
		try {
			responseModel = boApiRestService.getCustomerDls(ucc);
		} catch (Exception e) {
			logger.error("An error occurred: " + e.getMessage());
			commonMethods
					.sendErrorMail("An error occurred while processing your request, In getCustomerDls for the Error: "
							+ e.getMessage(), "ERR-002");
			e.printStackTrace();
		}
		return responseModel;

	}

	@Override
	public Object getTradebookDls(String fromdate, String todate, String ucc, String branch, String region_code,
			String zone, String segment, String symbol, String buysell) {
		Object responseModel = null;
		try {
			responseModel = boApiRestService.getTradebookDls(fromdate, todate, ucc, branch, region_code, zone, segment,
					symbol, buysell);
		} catch (Exception e) {
			logger.error("An error occurred: " + e.getMessage());
			commonMethods
					.sendErrorMail("An error occurred while processing your request, In getTradebookDls for the Error: "
							+ e.getMessage(), "ERR-002");
			e.printStackTrace();
		}
		return responseModel;

	}

	@Override
	public Object getBranchData(String branch_code) {
		Object responseModel = null;
		try {
			responseModel = boApiRestService.getBranchData(branch_code);
		} catch (Exception e) {
			logger.error("An error occurred: " + e.getMessage());
			commonMethods
					.sendErrorMail("An error occurred while processing your request, In getBranchData for the Error: "
							+ e.getMessage(), "ERR-002");
			e.printStackTrace();
		}
		return responseModel;

	}

	@Override
	public Object CustomerDP(String ucc) {
		Object responseModel = null;
		try {
			responseModel = boApiRestService.CustomerDP(ucc);
		} catch (Exception e) {
//			logger.error("An error occurred: " + e.getMessage());
//			commonMethods.sendErrorMail(
//					"An error occurred while processing your request, In CustomerDP for the Error: " + e.getMessage(),
//					"ERR-002");
			e.printStackTrace();
		}
		return responseModel;

	}

	@Override
	public Object UpdateNomineeDetails(String ucc) {
		Object responseModel = null;
		try {
			responseModel = boApiRestService.UpdateNomineeDetails(ucc);
		} catch (Exception e) {
			logger.error("An error occurred: " + e.getMessage());
			commonMethods.sendErrorMail(
					"An error occurred while processing your request, In UpdateNomineeDetails for the Error: "
							+ e.getMessage(),
					"ERR-002");
			e.printStackTrace();
		}
		return responseModel;
	}

	@Override
	public Object positionsDetails(String ucc) {
		Object responseModel = null;
		try {
			responseModel = boApiRestService.positionsDetails(ucc);
		} catch (Exception e) {
			logger.error("An error occurred: " + e.getMessage());
			commonMethods.sendErrorMail(
					"An error occurred while processing your request, In positionsDetails for the Error: "
							+ e.getMessage(),
					"ERR-002");
			e.printStackTrace();
		}
		return responseModel;

	}

	@Override
	public Object holdingsDetails(String ucc) {
		Object responseModel = null;
		try {
			responseModel = boApiRestService.holdingsDetails(ucc);
		} catch (Exception e) {
			logger.error("An error occurred: " + e.getMessage());
			commonMethods
					.sendErrorMail("An error occurred while processing your request, In holdingsDetails for the Error: "
							+ e.getMessage(), "ERR-002");
			e.printStackTrace();
		}
		return responseModel;

	}

}
