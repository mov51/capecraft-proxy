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
		pollServer(Main.SURVIVAL);
		pollServer(Main.CREATIVE);
	}
	
	/**
	 * Polls the server, checks the payer count and connects them to the server
	 * @param serverName Server name to poll
	 */
	private static void pollServer(String serverName) {
		//Check server has queued players
		if(getQueueSize(serverName) < 0) {
			return;
		}
		
		//Starts a server ping to serverName
		ProxyServer.getInstance().getServerInfo(serverName).ping(new Callback<ServerPing>() {
			@Override
			public void done(ServerPing result, Throwable error) {
				//Makes sure there isn't an error
				if(error != null) {
					return;
				}
				
				//Sets dynamic variables
				int playersOnline = result.getPlayers().getOnline();
				int playersMax = result.getPlayers().getMax();
				
				while(playersOnline < playersMax && getQueueSize(serverName) > 0) {
					//Gets the next queued player
					ProxiedPlayer queuedPlayer = getNextPlayer(serverName);					
					
					//Send player to server
					ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);					
					queuedPlayer.connect(serverInfo);										
					
					//Send queue message
					sendQueueMessages(serverName);
					
					//Increase online variable
					playersOnline++;
				}
			}			
		});
	}

	/**
	 * Gets all players in queue to send messages to
	 * @param serverName Server Name to send sueue message for
	 */
	private static void sendQueueMessages(String serverName) {
		//The starting queue pos
		int queuePlace = 1;
		
		if(serverName.equals(Main.SURVIVAL)) {
			//Loops through survivalQueue to send messages
			for(UUID uuid : survivalQueue) {
				sendQueueMessage(uuid, queuePlace);
				queuePlace++;
			}
		} else if(serverName.equals(Main.CREATIVE)) {
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
	 * Gets the first player in the queue and then removed them. Returns proxiedplayer to be handled
	 * @param serverName The server name
	 * @return The ProxiedPlayer object
	 */
	private static ProxiedPlayer getNextPlayer(String serverName) {
		if(serverName.equals(Main.SURVIVAL)) {
			UUID uuid = survivalQueue.get(0);
			survivalQueue.remove(uuid);
			return ProxyServer.getInstance().getPlayer(uuid);
		} else if(serverName.equals(Main.CREATIVE)) {
			UUID uuid = creativeQueue.get(0);
			creativeQueue.remove(uuid);
			return ProxyServer.getInstance().getPlayer(uuid);
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the servers queue size
	 * @param serverName The name of the server
	 */
	private static int getQueueSize(String serverName) {
		if(serverName.equals(Main.SURVIVAL)) {
			return survivalQueue.size();
		} else if(serverName.equals(Main.CREATIVE)) {
			return creativeQueue.size();
		}
		return 0;
	}
	
	/**
	 * Adds a player to the queue or connects them straight away if they have permissions
	 * @param serverName The server name to queue
	 * @param uuid The players uuid
	 */
	public static void addPlayer(String serverName, ProxiedPlayer player) {
		//If player has full join command then make them skip queue
		if(player.hasPermission("capecraft.fulljoin")) {
			ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(Main.CREATIVE);					
			player.connect(serverInfo);
			
			String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.QUEUE_DONATOR_MESSAGE);			
			BaseComponent[] msg = new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE)).reset().create();			
			player.sendMessage(msg);
			return;
		}

		//Generate queue message
		String queuePos = String.valueOf(ServerQueueHelper.getQueueSize(serverName));			
		String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.QUEUE_MESSAGE);
		msgRaw = msgRaw.replace("%place%", queuePos);
		BaseComponent[] msg = new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE)).reset().create();			
		player.sendMessage(msg);
		
		if(serverName.equals(Main.SURVIVAL)) {
			survivalQueue.add(player.getUniqueId());
		} else if(serverName.equals(Main.CREATIVE)) {
			creativeQueue.add(player.getUniqueId());
		}
	}
	
	/**
	 * Removes a player from the queue and updates other players
	 * @param uuid The players uuid
	 */
	public static void removePlayer(UUID uuid) {		
		int survivalIndex = survivalQueue.indexOf(uuid);
		survivalQueue.remove(uuid);
		for(int s = 0; s < survivalQueue.size(); s++) {
			if(s >= survivalIndex) {
				sendQueueMessage(survivalQueue.get(s), s+1);
			}
		}		
		
		int creativeIndex = creativeQueue.indexOf(uuid);
		creativeQueue.remove(uuid);
		for(int c = 0; c < survivalQueue.size(); c++) {
			if(c >= creativeIndex) {
				sendQueueMessage(survivalQueue.get(c), c+1);
			}
		}
	}
}
