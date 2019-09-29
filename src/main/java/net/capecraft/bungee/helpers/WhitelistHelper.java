package net.capecraft.bungee.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class WhitelistHelper {

	//Variables
	private static boolean whitelist_Enabled = false;
	private static List<UUID> whitelist_Players = new ArrayList<UUID>();
	
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
	}
	
	/**
	 * Check if a player is in the whitelist
	 * @param player The player to check
	 * @return The result
	 */
	public static boolean inWhitelist(ProxiedPlayer player) {
		return whitelist_Players.contains(player.getUniqueId());
	}
	
	/**
	 * Adds a player to whitelist
	 * @param player the player to add
	 */
	public static void addWhitelist(String username) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(username);
		if(player != null) {
			whitelist_Players.add(player.getUniqueId());			
		}
	}
	
	/**
	 * Removes a player from the whitelist
	 * @param player The player to remove
	 */
	public static void removeWhitelist(String username) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(username);
		if(player != null) {
			whitelist_Players.remove(player.getUniqueId());			
		}
	}
}
