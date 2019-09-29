package net.capecraft.bungee.helpers.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

import net.capecraft.Main;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class PlayerConfig {
	
	private static Plugin plugin;	
	private static File pluginFolder;
	private static File memberFolder;
	private static HashMap<UUID, Configuration> playerConfigs = new HashMap<UUID, Configuration>();
	
	/**
	 * Initialise the player config
	 * @param pluginInstance The plugin instance
	 */
	public static void initConfig(Plugin pluginInstance) {
		plugin = pluginInstance;
		
		//Makes plugin folders if they don't exist
		pluginFolder = plugin.getDataFolder();
		if(!pluginFolder.exists()) {
			pluginFolder.mkdir();
        }

		//Creates the users folder
		memberFolder = new File(pluginFolder + "/users/");
		if(!memberFolder.exists()) {
			memberFolder.mkdir();
		}				
	}
	
	/**
	 * Gets the players config and loads in memory
	 * @param uuid Player UUID
	 * @return The Configuration
	 */
	public static Configuration getPlayerConfig(UUID uuid) {		
		if(playerConfigs.get(uuid) == null) {
			try {
				Configuration playerConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(memberFolder, uuid.toString() + ".yml"));
				playerConfigs.put(uuid, playerConfig);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		return playerConfigs.get(uuid);
	}
	
	/**
	 * Checks if the player config exists. If it doesn't creates it
	 * @param uuid The player uuid
	 * @return Config exists
	 */
	public static boolean doesConfigExist(UUID uuid) {
		File playerFile = new File(memberFolder, uuid.toString() + ".yml");		
		if(!playerFile.exists()) {
			try (InputStream in = plugin.getResourceAsStream(Main.Configs.PLAYER_CONFIG)) {
                Files.copy(in, playerFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
			
			return false;
		}
		return true;
	}
	
	/**
	 * Saves the players config and unloads from memory
	 * @param uuid Player UUID
	 * @param playerConfig New Config
	 */
	public static void saveConfig(UUID uuid, Configuration playerConfig) {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(playerConfig, new File(memberFolder, uuid.toString() + ".yml"));			
		} catch (IOException e) {
			e.printStackTrace();
		}			
		
		if(playerConfigs.get(uuid) != null) {
			playerConfigs.remove(uuid);
		}		
	}
}
