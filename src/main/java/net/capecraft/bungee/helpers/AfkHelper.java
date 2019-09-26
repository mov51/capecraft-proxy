package net.capecraft.bungee.helpers;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import net.capecraft.Main;
import net.capecraft.bungee.BungeeMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AfkHelper {

	private static Queue<ProxiedPlayer> afkQueueList = new LinkedList<ProxiedPlayer>();
	
	public static void scheduleAfkMessage() {
		ProxyServer.getInstance().getScheduler().schedule(BungeeMain.INSTANCE, new Runnable() {
			@Override
			public void run() {				
				afkQueueList.forEach(player -> {
		            BaseComponent[] msg = new ComponentBuilder("You're AFK! ").color(ChatColor.RED).bold( true ).append("You don't gain playtime while AFK ").color(ChatColor.GREEN).bold( true ).append(":'( ").bold( true ).color(ChatColor.AQUA).append("Use ./afk to play normally!").color(ChatColor.GREEN).bold( true ).create();
					player.sendMessage(ChatMessageType.ACTION_BAR, msg);
				});	
			}        	
        }, 0, 1, TimeUnit.SECONDS);   
	}

	/**
	 * Gets the next player and removes from queue
	 * @return Next ProxiedPlayer
	 */
	public static ProxiedPlayer getNextPlayer() {
		return afkQueueList.poll();
	}
	
	/**
	 * Gets the next player but doesn't remove them from the list
	 * @return Next ProxiedPlayer
	 */
	public static ProxiedPlayer queryNextPlayer() {
		return afkQueueList.peek();
	}
	
	/**
	 * Check if player is in the AFK list
	 * @param player Player to check
	 * @return is player in list
	 */
	public static boolean isAfk(ProxiedPlayer player) {
		return afkQueueList.contains(player);
	}
	
	/**
	 * Adds a player to the AFK list
	 * @param player ProxiedPlayer to add
	 */
	public static void addPlayer(ProxiedPlayer player) {
		player.sendMessage(new ComponentBuilder(Main.PREFIX).append("You're now AFK!").reset().create());
		afkQueueList.add(player);
	}
	
	/**
	 * Removes a player from the AFK list
	 * @param player ProxiedPlayer to remove
	 */
	public static void removePlayer(ProxiedPlayer player) {
		player.sendMessage(new ComponentBuilder(Main.PREFIX).append("You're no longer AFK!").reset().create());
		afkQueueList.remove(player);
	}
}
