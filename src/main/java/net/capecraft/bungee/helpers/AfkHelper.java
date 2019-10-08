package net.capecraft.bungee.helpers;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.lucko.luckperms.api.User;
import net.capecraft.Main;
import net.capecraft.bungee.BungeeMain;
import net.capecraft.bungee.helpers.config.PluginConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.config.Configuration;

public class AfkHelper {

	//The AFK Queue
	private static Queue<ProxiedPlayer> afkSurvivalQueueList = new LinkedList<ProxiedPlayer>();
	private static Queue<ProxiedPlayer> afkCreativeQueueList = new LinkedList<ProxiedPlayer>();
	
	/**
	 * Loops through the AFK List and show an action bar message
	 */
	public static void scheduleAfkMessage() {
		ProxyServer.getInstance().getScheduler().schedule(BungeeMain.INSTANCE, new Runnable() {
			@Override
			public void run() {
				showAfk(Main.Servers.SURVIVAL);
				showAfk(Main.Servers.CREATIVE);
			}        	
			
			/**
			 * Show all AFK users a play time warning
			 * @param serverName The server to iterate through
			 */
			public void showAfk(String serverName) {
				getQueueList(serverName).forEach(player -> {
		            Configuration pluginConfig = PluginConfig.getPluginConfig();		            
					player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(pluginConfig.getString(PluginConfig.AFK_ACTIONBAR)));
				});	
			}
        }, 0, 1, TimeUnit.SECONDS);   
	}
	
	/**
	 * Checks the server name is valid
	 * @param serverName Server name supplied
	 * @return whether server name is valid
	 */
	public static boolean isValidServerName(String serverName) {
		return (Main.Servers.CREATIVE.equalsIgnoreCase(serverName) || Main.Servers.SURVIVAL.equalsIgnoreCase(serverName));
	}
	
	/**
	 * Get the afk queue for specific server
	 * @param serverName The server name
	 * @return The Servers AFK Queue
	 */
	private static Queue<ProxiedPlayer> getQueueList(String serverName) {			
		if(serverName.equalsIgnoreCase(Main.Servers.SURVIVAL)) {
			return afkSurvivalQueueList;
		} else if(serverName.equalsIgnoreCase(Main.Servers.CREATIVE)) {
			return afkCreativeQueueList;
		}
		return null;
	}

	/**
	 * Gets the next player and removes from queue
	 * @return Next ProxiedPlayer
	 */
	public static ProxiedPlayer getNextPlayer(String serverName) {
		if(!isValidServerName(serverName)) {
			return null;
		}
		
		return getQueueList(serverName).poll();
	}
	
	/**
	 * Check if player is in the AFK list by proxied player
	 * @param player Player to check
	 * @return is player in list
	 */
	public static boolean isAfk(ProxiedPlayer player) {
		String serverName = player.getServer().getInfo().getName();
		if(!isValidServerName(serverName)) {
			return false;
		}		
		return getQueueList(serverName).contains(player);
	}
	
	/**
	 * Check if player is in the AFK list by uuid
	 * @param player Player to check
	 * @return is player in list
	 */
	public static boolean isAfk(UUID uuid) {		
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
		if(player == null) {
			return false;
		} else {
			if(player.getServer() == null) {
				return false;
			}
			
			String serverName = player.getServer().getInfo().getName();		
			if(!isValidServerName(serverName)) {
				return false;
			}				
			return getQueueList(serverName).contains(player);
		}
	}	
	
	/**
	 * Adds a player to the AFK list
	 * @param player ProxiedPlayer to add
	 */
	public static void addPlayer(ProxiedPlayer player) {
		String serverName = player.getServer().getInfo().getName();		
		if(!isValidServerName(serverName)) {
			return;
		}
		
		//Do a playtime update to prevent people gaining AFK playtime		
		PlayTimeHelper.updatePlaytime(player.getUniqueId());
		
		//Send config message
		Configuration pluginConfig = PluginConfig.getPluginConfig();
		player.sendMessage(new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(pluginConfig.getString(PluginConfig.AFK_MESSAGE))).reset().create());
	
		//Add afk permission
		User user = LuckPermsHelper.getUser(player.getUniqueId());
		LuckPermsHelper.addPermission(user, "essentials.afk.kickexempt");
		
		//Add to queue
		getQueueList(serverName).add(player);
	}
	
	/**
	 * Removes a player from the AFK list
	 * @param player ProxiedPlayer to remove
	 */
	public static void removePlayer(ProxiedPlayer player) {		
		String serverName = player.getServer().getInfo().getName();		
		if(!isValidServerName(serverName)) {
			return;
		}
		
		//Do a playtime update to prevent people gaining AFK playtime
		PlayTimeHelper.updatePlaytime(player.getUniqueId());
		
		//Send config message
		Configuration pluginConfig = PluginConfig.getPluginConfig();
		player.sendMessage(new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(pluginConfig.getString(PluginConfig.UNAFK_MESSAGE))).reset().create());
		
		//remove AFK permission
		User user = LuckPermsHelper.getUser(player.getUniqueId());
		LuckPermsHelper.removePermission(user, "essentials.afk.kickexempt");
		
		//Remove from queue
		getQueueList(serverName).remove(player);
	}

	/**
	 * Add Alt to AFK List
	 * @param player Player to add
	 */
	public static void addAltPlayer(ProxiedPlayer player, Server server) {		
		String serverName = server.getInfo().getName();
		if(isValidServerName(serverName)) {
			getQueueList(serverName).add(player);
		}			
	}

	/**
	 * Purge all AFK queues of this player
	 * @param player Player to remove
	 */
	public static void purgePlayer(ProxiedPlayer player) {
		afkCreativeQueueList.remove(player);
		afkSurvivalQueueList.remove(player);
	}
}
