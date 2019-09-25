package net.capecraft.bungee.helpers;

import java.util.ArrayList;
import java.util.List;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.config.PlayerConfig;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;

public class ComSpyHelper implements Listener {
	
	//The ComListener List
    private static List<ProxiedPlayer> ComListener = new ArrayList<ProxiedPlayer>();

    /**
     * Get the list and return it
     * @return ComListener list
     */
	public static List<ProxiedPlayer> getListeners() {
		return ComListener;
	}
    
    /**
     * Add Player to comspy listener
     * @param player The Player object
     */
    public static void addComListener(ProxiedPlayer player) {
        //Adds player to the List
    	ComSpyHelper.ComListener.add(player);
    	//Updates config
		Configuration playerConfig = PlayerConfig.getPlayerConfig(player.getUniqueId());
		playerConfig.set(Main.PlayerConfigs.IS_SPYING, true);
		PlayerConfig.saveConfig(player.getUniqueId(), playerConfig);
    }

    /**
     * Remove player from company listener
     * @param player The Player object
     */
    public static void removeComListener(ProxiedPlayer player) {
        //Removes player from the List
    	ComSpyHelper.ComListener.remove(player);
    	//Updates config
		Configuration playerConfig = PlayerConfig.getPlayerConfig(player.getUniqueId());
		playerConfig.set(Main.PlayerConfigs.IS_SPYING, false);
		PlayerConfig.saveConfig(player.getUniqueId(), playerConfig);    	
    }
    
    /**
     * Is player spying on commands
     * @param player ProxiedPlayer object
     * @return is the player spying
     */
    public static boolean isPlayerSpying(ProxiedPlayer player) {
    	return ComSpyHelper.ComListener.contains(player);
    }
}
