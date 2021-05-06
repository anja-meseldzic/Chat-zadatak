package beans;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import data.Repository;
import model.User;
import ws.WSEndPoint;

@Stateless
@Path("/users")
@LocalBean
public class UserBean {

	@EJB
	WSEndPoint ws;
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response register(User user) {
		for(User u: Repository.getAllUsers()) {
			if(u.getUsername().equalsIgnoreCase(user.getUsername())) {
				System.out.println("User with that username already exists.");
				return Response.status(400).build();
			}
		}
		
		Repository.getAllUsers().add(user);
		System.out.println("User " + user.toString() + "have been successfully registered.");
		return Response.status(201).build();
	}
	
	
	@GET
	@Path("/registered")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> registered() {
		ArrayList<User> users = new ArrayList<>();
		
		if(Repository.getAllUsers().size() == 0) {
			System.out.println("There are no users!");
			return users;
		}
		
		for(User u : Repository.getAllUsers()) {
			users.add(u);
		}
		
		return users;
	}
	
}
