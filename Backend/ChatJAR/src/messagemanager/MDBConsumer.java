package messagemanager;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import agentmanager.AgentManagerRemote;
import agents.Agent;

/**
 * Message-Driven Bean implementation class for: MDBConsumer
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/chat-queue") })
public class MDBConsumer implements MessageListener {

	@EJB
	AgentManagerRemote agentManager;
	

	public void onMessage(Message message) {
		try {
			AgentMessage agentMessage = (AgentMessage) ((ObjectMessage) message).getObject();
			Agent agent = agentManager.getAgentById(agentMessage.getSender());
			System.out.println("agent: " + agent);
			System.out.println("id: " + agentMessage.getSender());
			if (agent != null)
				agent.handleMessage(agentMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}
