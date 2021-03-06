package com.vm.rest.auth.service.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

@Service
@Path("/private")
public interface DealsService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deal/{id}")
	String list(@PathParam("id") String id);

}
