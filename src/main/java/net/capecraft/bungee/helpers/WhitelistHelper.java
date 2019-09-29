package net.capecraft.bungee.helpers;

import java.util.UUID;

import com.google.gson.JsonArray;

import net.capecraft.bungee.helpers.config.PluginConfig;
import net.capecraft.bungee.helpers.config.WhitelistConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public class WhitelistHelper {

	//Variables
	private static boolean whitelist_Enabled = PluginConfig.getPluginConfig().getBoolean(PluginConfig.WHITELIST);
	
	/**
	 * Check if whitelist is enabled
	 * @return Whether whitelist is on or off
	 */
	public static boolean isWhitelist() {
		return whitelist_Enabled;
	}
		
	/**
	 * Sets the whitelist to on our off
	 * @param bool value of whitelist status
	 */
	public static void setWhitelist(boolean value) {
		whitelist_Enabled = value;
		Configuration pluginConfig = PluginConfig.getPluginConfig();
		pluginConfig.set(PluginConfig.WHITELIST, value);
		PluginConfig.saveConfig(pluginConfig);
	}
	
	/**
	 * Check if a player is in the whitelist
	 * @param player The player to check
	 * @return 
	 * @return The result
	 */
	public static boolean inWhitelist(UUID uuid) {
		JsonArray whitelistUsers = WhitelistConfig.getWhitelistUsers();
		for(int i = 0; i < whitelistUsers.size(); i++) {
			if(whitelistUsers.get(i).getAsJsonObject().get("uuid") != null) {
				if(whitelistUsers.get(i).getAsJsonObject().get("uuid").getAsString().equalsIgnoreCase(uuid.toString())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Adds a player to whitelist
	 * @param player the player to add
	 */
	public static void addWhitelist(String username) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(username);
		if(player != null) {
			WhitelistConfig.addUser(player.getUniqueId(), player.getDisplayName());		
		} else {
			WhitelistConfig.addUser(MojangAPIHelper.getUUID(username), username);
		}
	}
	
	/**
	 * Removes a player from the whitelist
	 * @param player The player to remove
	 */
	public static void removeWhitelist(String username) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(username);
		if(player != null) {
			WhitelistConfig.removeUser(player.getUniqueId());		
		} else {
			WhitelistConfig.removeUser(MojangAPIHelper.getUUID(username));
		}
	}
}
