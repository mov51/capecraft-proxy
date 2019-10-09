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
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public class PlayTimeHelper {

	/**
	 * Update the users playtime
	 * @param uuid Player uuid
	 * @param playerConfig Player config
	 */
	public static void updatePlaytime(UUID playerUUID) {
		if(!PlayerConfig.doesConfigExist(playerUUID))
			return;

		//Player config
		Configuration playerConfig = PlayerConfig.getPlayerConfig(playerUUID);

		//Check if player is AFK and prevent playtime updates
		if(AfkHelper.isAfk(playerUUID)) {
			playerConfig.set(Main.PlayerConfigs.JOIN_TIME, (System.currentTimeMillis() / 1000));
			PlayerConfig.saveConfig(playerUUID, playerConfig);
			return;
		}

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
		PlayerConfig.saveConfig(playerUUID, playerConfig);

		//Rankup the player if applicable
		checkPlayerRank(playerUUID);
	}

	/**
	 * Gets the players playtime in string
	 * @param uuid Player uuid
	 * @return Players playtime
	 */
	public static String getPlaytime(UUID uuid) {
		//Check config
		if(!PlayerConfig.doesConfigExist(uuid))
			return "no";

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
	public static void checkPlayerRank(UUID playerUUID) {
		//Convert UUID to ProxiedPlayer, return if null
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
		if(player == null || player.hasPermission(Main.Groups.ALT))
			return;

		Configuration playerConfig = PlayerConfig.getPlayerConfig(player.getUniqueId());
		int playTimeMin = playerConfig.getInt(Main.PlayerConfigs.PLAY_TIME);

		//25 hours regular
		if(playTimeMin >= 1500 && !player.hasPermission(Main.Groups.REGULAR)) {
			rankupPlayer(player, Main.Groups.DEFAULT, Main.Groups.REGULAR, new ComponentBuilder("REGULAR").bold(true).color(ChatColor.GRAY));
		}

		//100 hours player
		if(playTimeMin >= 6000 && !player.hasPermission(Main.Groups.PLAYER)) {
			rankupPlayer(player, Main.Groups.REGULAR, Main.Groups.PLAYER, new ComponentBuilder("PLAYER").bold(true).color(ChatColor.WHITE));
		}

		//200 hours member
		if(playTimeMin >= 12000 && !player.hasPermission(Main.Groups.MEMBER)) {
			rankupPlayer(player, Main.Groups.PLAYER, Main.Groups.MEMBER, new ComponentBuilder("MEMBER").bold(true).color(ChatColor.RED));
		}

		//350hr elder
		if(playTimeMin >= 21000 && !player.hasPermission(Main.Groups.ELDER)) {
			rankupPlayer(player, Main.Groups.MEMBER, Main.Groups.ELDER, new ComponentBuilder("ELDER").bold(true).color(ChatColor.DARK_RED));
		}

		//700h Veteran
		if(playTimeMin >= 42000 && !player.hasPermission(Main.Groups.VETERAN)) {
			rankupPlayer(player, Main.Groups.ELDER, Main.Groups.VETERAN, new ComponentBuilder("VETERAN").bold(true).color(ChatColor.DARK_PURPLE));
		}

		//1000h Legend
		if(playTimeMin >= 60000 && !player.hasPermission(Main.Groups.LEGEND)) {
			rankupPlayer(player, Main.Groups.VETERAN, Main.Groups.LEGEND, new ComponentBuilder("LEGEND").bold(true).color(ChatColor.YELLOW));
		}
	}

	/**
	 * Ranks up the player from one rank to the next
	 * @param player ProxiedPlayer instance
	 * @param oldGroup The old group eg group.member
	 * @param newGroup The new group eg group.legend
	 * @param componentBuilder The rankup message!
	 */
	private static void rankupPlayer(ProxiedPlayer player, String oldGroup, String newGroup, ComponentBuilder componentBuilder) {
		User luckPermsUser = LuckPermsHelper.getUser(player.getUniqueId());
		LuckPermsHelper.addPermission(luckPermsUser, newGroup);
		LuckPermsHelper.removePermission(luckPermsUser, oldGroup);

		BaseComponent[] msg = new ComponentBuilder(Main.PREFIX)
				.append(player.getDisplayName()).color(ChatColor.GOLD)
				.append(" Has just earned the ").reset()
				.append(componentBuilder.create())
				.append(" rank!").reset().create();
		ProxyServer.getInstance().broadcast(msg);
	}
}
