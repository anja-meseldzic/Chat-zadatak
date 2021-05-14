package messagemanager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AgentMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4750922547689000321L;
	
	private String sender;
	private MessageOptions options;
	private Map<String, String> attributes = new HashMap<String, String>();
	
	public AgentMessage() {}
	
	public AgentMessage(String sender, MessageOptions options) {
		super();
		this.sender = sender;
		this.options = options;
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public MessageOptions getOptions() {
		return options;
	}


	public void setOptions(MessageOptions options) {
		this.options = options;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public void addAttribute(String key, String value) {
		attributes.put(key, value);
	}


}
