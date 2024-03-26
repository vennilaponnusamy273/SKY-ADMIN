package in.codifi.api.service.spec;

public interface IBoApiService {

	Object getCustomerLedger(String ucc, String fromdate, String todate);

	Object getCustomerDls(String ucc);

	Object getTradebookDls(String fromdate, String todate, String ucc, String branch, String region_code, String zone,
			String segment, String symbol, String buysell);

	Object getBranchData(String branch_code);

	Object CustomerDP(String ucc);

	Object UpdateNomineeDetails(String ucc);

	Object positionsDetails(String ucc);

	Object holdingsDetails(String ucc);

}
