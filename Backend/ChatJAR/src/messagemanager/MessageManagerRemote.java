package messagemanager;

import javax.ejb.Remote;
import javax.jms.MessageConsumer;
import javax.jms.Session;

@Remote
public interface MessageManagerRemote {
	
	public void post(AgentMessage msg);
}
