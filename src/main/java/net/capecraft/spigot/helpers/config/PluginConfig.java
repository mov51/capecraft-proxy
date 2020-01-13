package net.capecraft.spigot.helpers.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import net.capecraft.Main;

public class PluginConfig {
	
	private static File pluginFolder;
	private static YamlConfiguration pluginConfig;

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
        File configFile = new File(pluginFolder, Main.Configs.PAPER_CONFIG);
		if (!configFile.exists()) {
            try (InputStream in = plugin.getResource(Main.Configs.PAPER_CONFIG)) {
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
	public static YamlConfiguration getPluginConfig() {
		if(pluginConfig == null) {
			pluginConfig = YamlConfiguration.loadConfiguration(new File(pluginFolder, Main.Configs.PAPER_CONFIG));
		}
		return pluginConfig;
	}
	
}
