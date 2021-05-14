package agents;

import java.io.Serializable;

import javax.ejb.Remote;
import javax.jms.Message;

import messagemanager.AgentMessage;

@Remote
public interface Agent extends Serializable {

	public void init(String id);

	public void handleMessage(AgentMessage message);

	public String getAgentId();
}
