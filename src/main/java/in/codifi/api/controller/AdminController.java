package in.codifi.api.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import in.codifi.api.controller.spec.AdminControllerSpec;

@Path("/admin")
public class AdminController implements AdminControllerSpec {

	@Path("/test")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String test() {
		String test = "Hello Vennila";
		return test;
	}

}
