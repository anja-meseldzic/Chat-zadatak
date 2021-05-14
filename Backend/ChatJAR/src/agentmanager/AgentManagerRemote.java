package agentmanager;

import java.io.Serializable;
import java.util.List;

import agents.Agent;

public interface AgentManagerRemote extends Serializable {
	
	public void startAgent(String id, String name);

	public Agent getAgentById(String agentId);

	public void stopAgent(String sessionId);

	public List<Agent> getRunningAgents();

}
