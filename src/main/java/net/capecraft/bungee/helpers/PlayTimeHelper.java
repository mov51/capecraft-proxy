package net.capecraft.bungee.helpers;

import java.text.DecimalFormat;
import java.util.UUID;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.config.PlayerConfig;
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

}
