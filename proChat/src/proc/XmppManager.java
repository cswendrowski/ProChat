package proc;

import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;

public class XmppManager {
	
	private static final int packetReplyTimeout = 500; // millis
	
	private String server;
	private int port;
	
	private ConnectionConfiguration config;
	private XMPPConnection connection;
	
	private ChatManager chatManager;
	private MessageListener messageListener;
	
	public XmppManager(String server, int port) {
		this.server = server;
		this.port = port;
	}
	
	public void init() throws XMPPException {
		
		Log.l(String.format("Initializing connection to server %1$s port %2$d", server, port));

		SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);
		
		config = new ConnectionConfiguration(server, port);
		config.setSASLAuthenticationEnabled(false);
		config.setSecurityMode(SecurityMode.disabled);
		
		connection = new XMPPConnection(config);
		connection.connect();
		
		Log.l("Connected: " + connection.isConnected());
		
		chatManager = connection.getChatManager();
		messageListener = new MyMessageListener();
		
	}
	
	public void performLogin(String username, String password) throws XMPPException {
		if (connection!=null && connection.isConnected()) {
			connection.login(username, password);
		}
	}
	
	public Presence setStatus(boolean available, String status) {
		
		Presence.Type type = available? Type.available: Type.unavailable;
		Presence presence = new Presence(type);
		
		presence.setStatus(status);
		connection.sendPacket(presence);
		
		return presence;
	}
	
	public void setMode(boolean available, Mode mode) {
		Presence.Type type = available? Type.available: Type.unavailable;
		Presence p = new Presence(type);
		
		p.setMode(mode);
		connection.sendPacket(p);
	}
	
	public void destroy() {
		if (connection!=null && connection.isConnected()) {
			connection.disconnect();
		}
	}
	
	public void printRoster() throws Exception {
		Roster roster = connection.getRoster();
		Collection<RosterEntry> entries = roster.getEntries();		
		for (RosterEntry entry : entries) {
		    Log.l(String.format("Buddy:%1$s - Status:%2$s", 
		    		entry.getName(), entry.getStatus()));
		}
	}
	
	public XMPPConnection getConnection() {
		return connection;
	}
	
	public ChatManager getChatManager() {
		return chatManager;
	}
	
	public void sendMessage(String message, String buddyJID) throws XMPPException {
		Log.l(String.format("Sending mesage '%1$s' to user %2$s", message, buddyJID));
		Chat chat = chatManager.createChat(buddyJID, messageListener);
		chat.sendMessage(message);
	}
	
	public void createEntry(String user, String name) throws Exception {
		Log.l(String.format("Creating entry for buddy '%1$s' with name %2$s", user, name));
		Roster roster = connection.getRoster();
		roster.createEntry(user, name, null);
	}
	
	class MyMessageListener implements MessageListener {

		@Override
		public void processMessage(Chat chat, Message message) {
			String from = message.getFrom();
			String body = message.getBody();
			Log.l(String.format("Received message '%1$s' from %2$s", body, from));
		}
		
	}

	public void setPresence(boolean available, String status, Mode mode) {
		Presence.Type type = available? Type.available: Type.unavailable;
		Presence presence = new Presence(type);
		
		presence.setStatus(status);
		presence.setMode(mode);
		connection.sendPacket(presence);
		
	}
	
}
