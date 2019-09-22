package net.capecraft.bungee.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.config.PluginConfig;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerQueueHelper {
		
	private static List<UUID> survivalQueue = new ArrayList<UUID>();
	private static List<UUID> creativeQueue = new ArrayList<UUID>();
	
	/**
	 * The first method calls by the task schedule
	 */
	public static void checkServerSlots() {		
		pollServer("survival");
		pollServer("creative");
	}
	
	/**
	 * Polls the server, checks the payer count and connects them to the server
	 * @param serverName Server name to poll
	 */
	public static void pollServer(String serverName) {
		ProxyServer.getInstance().getServerInfo(serverName).ping(new Callback<ServerPing>() {
			@Override
			public void done(ServerPing result, Throwable error) {
				if(error == null) {
					if(result.getPlayers().getOnline() < result.getPlayers().getMax()) {
						while(result.getPlayers().getOnline() < result.getPlayers().getMax()) {
							//Gets the next queued player
							ProxiedPlayer queuedPlayer = getQueuePoll(serverName);
							
							//If next player is null, break the loop
							if(queuedPlayer == null)
								break;
							
							//Send player to server
							ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);					
							queuedPlayer.connect(serverInfo);
							
							//Send queue message
							sendQueueMessages(serverName);
						}
					}
				}
			}				
		});
	}

	/**
	 * Gets all players in queue to send messages to
	 * @param serverName Server Name to send sueue message for
	 */
	public static void sendQueueMessages(String serverName) {
		//The starting queue pos
		int queuePlace = 1;
		
		if(serverName.equals("survival")) {
			//Loops through survivalQueue to send messages
			for(UUID uuid : survivalQueue) {
				sendQueueMessage(uuid, queuePlace);
				queuePlace++;
			}
		} else if(serverName.equals("creative")) {
			//Loops through creative queue to send messages
			for(UUID uuid : creativeQueue) {		
				sendQueueMessage(uuid, queuePlace);
				queuePlace++;
			}	
		}
	}
	
	/**
	 * Sends the final queue message
	 * @param uuid UUID Target
	 * @param pos Queue Position
	 */
	private static void sendQueueMessage(UUID uuid, int pos) {
		String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.QUEUE_STATUS);
		msgRaw = msgRaw.replace("%place%", String.valueOf(pos));			
		BaseComponent[] msg = new ComponentBuilder(Main.PREFIX)
				.append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE))
				.color(ChatColor.AQUA).create();
		ProxyServer.getInstance().getPlayer(uuid).sendMessage(msg);
	}
	
	/**
	 * Gets the first player in the queue
	 * @param serverName The server name
	 * @return The ProxiedPlayer object
	 */
	private static ProxiedPlayer getQueuePoll(String serverName) {
		if(serverName.equals("survival")) {		
			return ProxyServer.getInstance().getPlayer(survivalQueue.get(0));
		} else if(serverName.equals("creative")) {
			return ProxyServer.getInstance().getPlayer(creativeQueue.get(0));
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the servers queue size
	 * @param serverName The name of the server
	 */
	public static int getQueueSize(String serverName) {
		if(serverName.equals("survival")) {
			return survivalQueue.size();
		} else if(serverName.equals("creative")) {
			return creativeQueue.size();
		}
		return 0;
	}
	
	/**
	 * Adds a player to the queue
	 * @param serverName The server name to queue
	 * @param uuid The players uuid
	 */
	public static void addPlayer(String serverName, UUID uuid) {
		if(serverName.equals("survival")) {
			survivalQueue.add(uuid);
		} else if(serverName.equals("creative")) {
			creativeQueue.add(uuid);
		}
	}
	
	/**
	 * Removes a player from the queue 
	 * @param uuid The players uuid
	 */
	public static void removePlayer(UUID uuid) {
		
		int survivalIndex = survivalQueue.indexOf(uuid);
		for(int s = 0; s < survivalQueue.size(); s++) {
			if(s > survivalIndex) {
				//Send Message
			}
		}		
		
		int creativeIndex = creativeQueue.indexOf(uuid);
		
		
		
		survivalQueue.remove(uuid);
		creativeQueue.remove(uuid);
	}
}
