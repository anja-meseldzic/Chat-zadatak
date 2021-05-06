package data;

import java.util.ArrayList;
import model.Host;
import model.Message;
import model.User;

public class Repository {

	private static ArrayList<User> allUsers = new ArrayList<User>();
	private static ArrayList<User> loggedInUsers = new ArrayList<User>();
	private static ArrayList<Host> hosts = new ArrayList<Host>();
	private static ArrayList<Message> messages = new ArrayList<Message>();
	
	static {
		allUsers.add(new User("anja", "anja", null));
		allUsers.add(new User("vuk", "vuk", null));
	}
	
	public static ArrayList<User> getAllUsers() {
		return allUsers;
	}
	public static void setAllUsers(ArrayList<User> allUsers) {
		Repository.allUsers = allUsers;
	}
	public static ArrayList<User> getLoggedInUsers() {
		return loggedInUsers;
	}
	public static void setLoggedInUsers(ArrayList<User> loggedInUsers) {
		Repository.loggedInUsers = loggedInUsers;
	}
	public static ArrayList<Host> getHosts() {
		return hosts;
	}
	public static void setHosts(ArrayList<Host> hosts) {
		Repository.hosts = hosts;
	}
	public static ArrayList<Message> getMessages() {
		return messages;
	}
	public static void setMessages(ArrayList<Message> messages) {
		Repository.messages = messages;
	}
	
}
