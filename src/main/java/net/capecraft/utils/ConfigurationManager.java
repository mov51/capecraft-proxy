package net.capecraft.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigurationManager {
	
	private static File pluginFolder;
	private static File memberFolder;
	private static Configuration pluginConfig;
	private static HashMap<UUID, Configuration> playerConfigs = new HashMap<UUID, Configuration>();	

	public static void initConfig(Plugin plugin) {
		//Makes plugin folders if they don't exist
		pluginFolder = plugin.getDataFolder();
		if(!pluginFolder.exists()) {
			pluginFolder.mkdir();
        }
		
		//Copy default config if it doesn't exist
        File configFile = new File(pluginFolder, "config.yml");
		if (!configFile.exists()) {
            try (InputStream in = plugin.getResourceAsStream("config.yml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
		}

		//Creates the users folder
		memberFolder = new File(pluginFolder + "/users/");
		if(!memberFolder.exists()) {
			memberFolder.mkdir();
		}
	}
	
	/**
	 * Loads the config file to memory
	 * @return return the config
	 */
	public static Configuration getPluginConfig() {		
		if(pluginConfig == null) {
			try {
				pluginConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(pluginFolder, "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pluginConfig;
	}
	
	/**
	 * Reloads the configuration into memory
	 */
	public static void reloadPluginConfig() {
		try {
			pluginConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(pluginFolder, "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
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
