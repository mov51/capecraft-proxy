package net.capecraft.bungee.helpers;

import java.text.DecimalFormat;
import java.util.UUID;

import me.lucko.luckperms.api.User;
import net.capecraft.Main;
import net.capecraft.bungee.helpers.config.PlayerConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public class PlayTimeHelper {
	
	/**
	 * Update the users playtime
	 * @param uuid Player uuid
	 * @param playerConfig Player config
	 */
	public static void updatePlaytime(UUID uuid) {
		//Player config
		Configuration playerConfig = PlayerConfig.getPlayerConfig(uuid);
		
		//Playtime in minutes
		int playTimeMin = playerConfig.getInt(Main.PlayerConfigs.PLAY_TIME);
		//Join time unix
		int joinTimeUnix = playerConfig.getInt(Main.PlayerConfigs.JOIN_TIME);
		//difference in seconds
		int joinTimeDiff = (int) ((System.currentTimeMillis() / 1000) - joinTimeUnix);
		//convert to minutes
		joinTimeDiff = joinTimeDiff / 60;
		playTimeMin = playTimeMin + joinTimeDiff;

		playerConfig.set(Main.PlayerConfigs.JOIN_TIME, (System.currentTimeMillis() / 1000));
		playerConfig.set(Main.PlayerConfigs.PLAY_TIME, playTimeMin);
		PlayerConfig.saveConfig(uuid, playerConfig);
		
		//Rankup the player if applicable
		checkPlayerRank(ProxyServer.getInstance().getPlayer(uuid));
	} 
	
	/**
	 * Gets the players playtime in string
	 * @param uuid Player uuid
	 * @return Players playtime
	 */
	public static String getPlaytime(UUID uuid) {
		//Player config
		Configuration playerConfig = PlayerConfig.getPlayerConfig(uuid);
		
		double playtime = playerConfig.getInt("playtime");
		playtime = playtime / 60;
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(playtime);
	}

	/**
	 * Check the players rank to his playtime
	 * @param player The Player object
	 */
	public static void checkPlayerRank(ProxiedPlayer player) {		
		Configuration playerConfig = PlayerConfig.getPlayerConfig(player.getUniqueId());
		int playTimeMin = playerConfig.getInt(Main.PlayerConfigs.PLAY_TIME);		
		
		//25 hours regular
		if(playTimeMin >= 1500 && !player.hasPermission("group.regular")) {
			rankupPlayer(player, "group.default", "group.regular", "§7§lREGULAR");					
		}

		//100 hours player
		if(playTimeMin >= 6000 && !player.hasPermission("group.player")) {
			rankupPlayer(player, "group.regular", "group.player", "§f§lPLAYER");			
		}

		//200 hours member
		if(playTimeMin >= 12000 && !player.hasPermission("group.member")) {
			rankupPlayer(player, "group.player", "group.member", "§c§lMEMBER");			
		}

		//350hr elder
		if(playTimeMin >= 21000 && !player.hasPermission("group.elder")) {
			rankupPlayer(player, "group.member", "group.elder", "§c§lMEMBER");
		}
		
		//700h Veteran
		if(playTimeMin >= 42000 && !player.hasPermission("group.veteran")) {
			rankupPlayer(player, "group.elder", "group.veteran", "§5§lVETERAN");
		}

		//1000h Legend
		if(playTimeMin >= 60000 && !player.hasPermission("group.legend")) {
			rankupPlayer(player, "group.veteran", "group.legend", "§e§lLEGEND");
		}
	}
	
	/**
	 * Ranks up the player from one rank to the next
	 * @param player ProxiedPlayer instance
	 * @param oldGroup The old group eg group.member
	 * @param newGroup The new group eg group.legend
	 * @param rankText The rankup message!
	 */
	private static void rankupPlayer(ProxiedPlayer player, String oldGroup, String newGroup, String rankText) {
		User luckPermsUser = LuckPermsHelper.getUser(player.getUniqueId());
		LuckPermsHelper.addPermission(luckPermsUser, newGroup);
		LuckPermsHelper.removePermission(luckPermsUser, oldGroup);
		
		BaseComponent[] msg = new ComponentBuilder(Main.PREFIX)
				.append(player.getDisplayName()).color(ChatColor.GOLD)
				.append(" Has just earned the ").reset()
				.append(TextComponent.fromLegacyText(rankText, ChatColor.WHITE))
				.append(" rank!").reset().create();
		ProxyServer.getInstance().broadcast(msg);
	}
}
