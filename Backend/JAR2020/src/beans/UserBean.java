package beans;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import data.Repository;
import model.User;
import ws.WSEndPoint;

@Stateless
@Path("/users")
@LocalBean
public class UserBean {

	@EJB
	WSEndPoint ws;
	
	
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
