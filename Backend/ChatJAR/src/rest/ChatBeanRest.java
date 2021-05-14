package rest;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import messagemanager.MessageOptions;
import models.User;

@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/chat")
@LocalBean
@Remote(ChatRest.class)
public class ChatBeanRest implements ChatRest {
	
	
	@EJB
	MessageManagerRemote msm;
	

	
	
	@Override
	public void getLoggedInUsers(String sender) {
		AgentMessage agentMessage = new AgentMessage(sender, MessageOptions.GET_ALL_LOGGED_IN);
		msm.post(agentMessage);
	}
	
	@Override
	public void getRegisteredUsers(String sender) {
		AgentMessage agentMessage = new AgentMessage(sender, MessageOptions.GET_ALL);
		msm.post(agentMessage);
		
	}


	@Override
	public void logIn(User user, String sender) {
		AgentMessage agentMessage = new AgentMessage(sender, MessageOptions.LOG_IN);
		agentMessage.addAttribute("username", user.getUsername());
		agentMessage.addAttribute("password", user.getPassword());
		msm.post(agentMessage);
		
	}




	@Override
	public void logOut(String sender, String id) {
		AgentMessage agentMessage = new AgentMessage(sender, MessageOptions.LOG_OUT);
		agentMessage.addAttribute("id", id);
		msm.post(agentMessage);
		
	}	




	@Override
	public void register(User user, String sender) {
		AgentMessage agentMessage = new AgentMessage(sender, MessageOptions.REGISTER);
		agentMessage.addAttribute("username", user.getUsername());
		agentMessage.addAttribute("password", user.getPassword());
		msm.post(agentMessage);
		
	}
	@Override
	public void sendToAll(@PathParam("sender") String sender, @PathParam("subject") String subject, @PathParam("content") String content) {
		AgentMessage agentMessage = new AgentMessage(sender, MessageOptions.SEND_ALL);
		agentMessage.addAttribute("subject", subject);
		agentMessage.addAttribute("content", content);
		msm.post(agentMessage);
	}



	@Override
	public void sendToUser(String sender, String receiver, String subject, String content) {
		AgentMessage agentMessage = new AgentMessage(sender, MessageOptions.SEND_USER);
		agentMessage.addAttribute("receiver", receiver);
		agentMessage.addAttribute("subject", subject);
		agentMessage.addAttribute("content", content);
		msm.post(agentMessage);
		
	}


	@Override
	public void getMessages(String sender) {
		AgentMessage agentMessage = new AgentMessage(sender, MessageOptions.GET_ALL_MESSAGES);
		msm.post(agentMessage);
		
	}
	
	
}
