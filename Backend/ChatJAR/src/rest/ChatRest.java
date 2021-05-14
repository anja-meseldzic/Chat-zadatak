package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import models.User;

public interface ChatRest {

	@GET 
	@Path("/users/loggedIn/{sender}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void getLoggedInUsers(@PathParam("sender") String sender);
	
	@GET 
	@Path("/users/registered/{sender}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void getRegisteredUsers(@PathParam("sender") String sender);
	
	@POST
	@Path("/users/register/{sender}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void register(User user,@PathParam("sender") String sender);
	
	@POST
	@Path("/users/logIn/{sender}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void logIn(User user,@PathParam("sender") String sender);
	
	@DELETE
	@Path("/users/loggedIn/{sender}/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void logOut(@PathParam("sender") String sender, @PathParam("id") String id);
	
	@POST
	@Path("/messages/all/{sender}/{subject}/{content}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void sendToAll(@PathParam("sender") String sender, @PathParam("subject") String subject, @PathParam("content") String content);
	
	@POST
	@Path("/messages/user/{sender}/{receiver}/{subject}/{content}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void sendToUser(@PathParam("sender") String sender, @PathParam("receiver") String receiver, @PathParam("subject") String subject, @PathParam("content") String content);
	
	@GET
	@Path("/messages/{sender}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void getMessages(@PathParam("sender") String sender);
}
