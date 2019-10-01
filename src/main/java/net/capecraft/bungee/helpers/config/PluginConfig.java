package net.capecraft.bungee.helpers.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.capecraft.Main;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class PluginConfig {
	
	//Config variables
	public static final String VERSIONS = "versions";
	public static final String VERSIONS_NAME = VERSIONS + ".name";
	public static final String VERSIONS_SUPPORTED = VERSIONS + ".supportedVersions";
	
	public static final String WHITELIST = "whitelist";
	public static final String WHITELIST_MESSAGE = "whitelistMessage";
	
	public static final String FIRST_JOIN_MESSAGE = "firstJoinMessage";
	public static final String JOIN_MESSAGE = "joinMessage";
	public static final String LEAVE_MESSAGE = "leaveMessage";
	
	public static final String PLAYTIME_MESSAGE = "playtimeMessage";
	
	public static final String QUEUE_MESSAGE = "queueMessage";
	public static final String QUEUE_DONATOR_MESSAGE = "queueDonatorMessage";
	public static final String QUEUE_STATUS = "queueStatus";	
		
	public static final String AFK_MESSAGE = "afkMessage";
	public static final String UNAFK_MESSAGE = "unAfkMessage";
	public static final String ALT_AFK = "altAfk";
	public static final String NO_AFK = "noAfk";
	public static final String FAIL_AFK = "failAfk";
	public static final String FULL_AFK = "fullAfk";
	public static final String AFK_ACTIONBAR = "afkActionBar";
		
	public static final String KICK_AFK = "kickAfk";
	public static final String KICK_AFK_BROADCAST = "kickAfkBroadcast";	
	
	public static final String MOTD = "motd";
	
	private static File pluginFolder;
	private static Configuration pluginConfig;

	/**
	 * Initialise the config
	 * @param plugin The plugin instance
	 */
	public static void initConfig(Plugin plugin) {
		//Makes plugin folders if they don't exist
		pluginFolder = plugin.getDataFolder();
		if(!pluginFolder.exists()) {
			pluginFolder.mkdir();
        }
		
		//Copy default config if it doesn't exist
        File configFile = new File(pluginFolder, Main.Configs.BUNGEE_CONFIG);
		if (!configFile.exists()) {
            try (InputStream in = plugin.getResourceAsStream(Main.Configs.BUNGEE_CONFIG)) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
		}		
	}
	
	/**
	 * Loads the config file to memory
	 * @return return the config
	 */
	public static Configuration getPluginConfig() {		
		if(pluginConfig == null) {
			try {
				pluginConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(pluginFolder, Main.Configs.BUNGEE_CONFIG));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pluginConfig;
	}
	
	/**
	 * Save the plugin config
	 */
	public static void saveConfig(Configuration config) {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(pluginFolder, Main.Configs.BUNGEE_CONFIG));
			reloadPluginConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Reloads the configuration into memory
	 */
	public static void reloadPluginConfig() {
		try {
			pluginConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(pluginFolder, Main.Configs.BUNGEE_CONFIG));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}		
}
