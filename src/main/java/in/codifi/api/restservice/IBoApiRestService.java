package in.codifi.api.restservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "config-boup")
@RegisterClientHeaders
public interface IBoApiRestService {

	@GET
	@Path("/method/cs_bo.custom_api.customer_ledger.get_customer_ledger_records")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getCustomerLedger(@HeaderParam("Authorization") String authorization, @QueryParam("ucc") String ucc,
			@QueryParam("from_date") String fromdate, @QueryParam("to_date") String todate);

	@GET
	@Path("/resource/Customer/{ucc}")
	@Produces("application/json")
	@Consumes("application/json")
	Response getCustomerDls(@HeaderParam("Authorization") String authorization,@PathParam("ucc") String ucc);

	@GET
	@Path("/method/cs_bo.custom_api.pp_tradebook_details.get_tradebook_details")
	@Produces("application/json")
	@Consumes("application/json")
	Response getTradebookDls(@HeaderParam("Authorization") String authorization,
			@QueryParam("from_date") String fromdate, @QueryParam("to_date") String todate,
			@QueryParam("ucc") String ucc, @QueryParam("branch'") String branch,
			@QueryParam("region_code") String region_code, @QueryParam("zone") String zone,
			@QueryParam("segment") String segment, @QueryParam("symbol") String symbol,
			@QueryParam("buysell") String buysell);

	@GET
	@Path("/method/cs_bo.custom_api.cf_get_branch.get_branch_id")
	@Produces("application/json")
	@Consumes("application/json")
	Response getBranchData(@HeaderParam("Authorization") String authorization,
			@QueryParam("branch_code") String branch_code);

	@GET
	@Path("/method/cs_bo.custom_api.cf_customer_details.get_Dp_Id")
	@Produces("application/json")
	@Consumes("application/json")
	Response CustomerDP(@HeaderParam("Authorization") String authorization, @QueryParam("ucc_code") String ucc);

	@GET
	@Path("/method/cs_bo.custom_api.cus_nominee_details.get_customer_nominee_details")
	@Produces("application/json")
	@Consumes("application/json")
	Response UpdateNomineeDetails(@HeaderParam("Authorization") String authorization, @QueryParam("ucc_code") String ucc);

	@GET
	@Path("/method/cs_bo.custom_api.positions.get_positions")
	@Produces("application/json")
	@Consumes("application/json")
	Response positionsDetails(@HeaderParam("Authorization") String authorization, @QueryParam("ucc_code") String ucc);

	@GET
	@Path("/method/cs_bo.custom_api.holding.get_holdings")
	@Produces("application/json")
	@Consumes("application/json")
	Response holdingsDetails(@HeaderParam("Authorization") String authorization, @QueryParam("ucc") String ucc);

}
