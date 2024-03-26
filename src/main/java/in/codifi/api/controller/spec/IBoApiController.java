package in.codifi.api.controller.spec;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.wildfly.common.annotation.NotNull;

public interface IBoApiController {

	/**
	 * Method to get getCustomerLedger
	 * 
	 * @author VENNILA
	 * @param
	 * @return
	 */

	@Path("/getCustomerLeg")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to get USer records")
	Object getCustomerLedger(@NotNull @QueryParam("ucc") String ucc, @NotNull @QueryParam("fromdate") String fromdate,
			@NotNull @QueryParam("todate") String todate);

	/**
	 * Method to get getCustomerDls
	 * 
	 * @author VENNILA
	 * @param
	 * @return
	 */

	@Path("/getCustomerDls")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to get USer records")
	Object getCustomerDls(@NotNull @QueryParam("ucc") String ucc);

	/**
	 * Method to get getTradebookDls
	 * 
	 * @author VENNILA
	 * @param
	 * @return
	 */

	@Path("/getTradebookDls")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to get USer records")
	Object getTradebookDls(@NotNull @QueryParam("from_date") String fromdate,
			@NotNull @QueryParam("to_date") String todate, @NotNull @QueryParam("ucc") String ucc,
			@NotNull @QueryParam("branch") String branch, @NotNull @QueryParam("region_code") String region_code,
			@NotNull @QueryParam("zone") String zone, @NotNull @QueryParam("segment") String segment,
			@NotNull @QueryParam("symbol") String symbol, @NotNull @QueryParam("buysell") String buysell);

	/**
	 * Method to getBranchData
	 * 
	 * @author VENNILA
	 * @param
	 * @return
	 */

	@Path("/getBranchData")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to get Branch Data")
	Object getBranchData(@NotNull @QueryParam("branch_code") String branch_code);

	@Path("/CustomerDP")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to get CustomerDP")
	Object CustomerDP(@NotNull @QueryParam("ucc") String ucc);
	
	@Path("/UpdateNomineeDetails")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to get UpdateNomineeDetails")
	Object UpdateNomineeDetails(@NotNull @QueryParam("ucc") String ucc);
	

	

	@Path("/positionsDetails")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to get CustomerDP")
	Object positionsDetails(@NotNull @QueryParam("ucc") String ucc);

	@Path("/holdingsDetails")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to get CustomerDP")
	Object holdingsDetails(@NotNull @QueryParam("ucc") String ucc);
}
