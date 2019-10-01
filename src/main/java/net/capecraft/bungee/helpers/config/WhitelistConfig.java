package net.capecraft.bungee.helpers.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.google.common.io.Files;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.capecraft.Main;
import net.md_5.bungee.api.plugin.Plugin;

public class WhitelistConfig {
	
	private static File pluginFolder;	
	private static JsonArray whitelistUsers;

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
        File whitelistFile = new File(pluginFolder, Main.Configs.WHITELIST_CONFIG);
		if (!whitelistFile.exists()) {
            try (InputStream in = plugin.getResourceAsStream(Main.Configs.WHITELIST_CONFIG)) {
            	whitelistFile.createNewFile();
            	getWhitelistUsers();
            	updateWhitelistFile();            	
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
	}
	
	/**
	 * Loads the whitelisted users into memory
	 * @return The users
	 */
	public static JsonArray getWhitelistUsers() {
		if(whitelistUsers == null) {
			try {
				File whitelistFile = new File(pluginFolder, Main.Configs.WHITELIST_CONFIG);
				String content = Files.toString(whitelistFile, StandardCharsets.US_ASCII);
				content = (!content.isEmpty()) ? content : new JsonArray().toString();
			    whitelistUsers = new JsonParser().parse(content).getAsJsonArray();			    
			} catch (IOException e) {
				e.printStackTrace();				
			}
		} else {
			return whitelistUsers;
		}
		return whitelistUsers;
	}
	
	/**
	 * Add the user to the whitelist
	 * @param uuid ProxiedPlayer to add
	 */
	public static void addUser(UUID uuid, String username) {
		//Checks player is in whitelist
		if(whitelistUsers != null) {
			for(int i = 0; i < getWhitelistUsers().size(); i++) {
				JsonObject user = getWhitelistUsers().get(i).getAsJsonObject();
				if(user.get("uuid").getAsString().equalsIgnoreCase(uuid.toString())) {
					return;
				}
			}
		}
		
		//Else adds user to playlist
		JsonObject user = new JsonObject();
		user.addProperty("uuid", uuid.toString());
		user.addProperty("username", username);
		getWhitelistUsers().add(user);
		updateWhitelistFile();
	}
	
	/**
	 * Removes a player from whitelist
	 * @param player ProxiedPlayer to remove
	 */
	public static void removeUser(UUID uuid) {
		//Loops through whitelist looking for player to remove
		for(int i = 0; i < getWhitelistUsers().size(); i++) {
			JsonObject user = getWhitelistUsers().get(i).getAsJsonObject();
			if(user.get("uuid").getAsString().equalsIgnoreCase(uuid.toString())) {				
				getWhitelistUsers().remove(i);
				updateWhitelistFile();
				break;
			}
		}		
	}
	
	/**
	 * Updates the whitelist IO File
	 */
	private static void updateWhitelistFile() {
		try {
			FileWriter file = new FileWriter(new File(pluginFolder, Main.Configs.WHITELIST_CONFIG));
			file.write(getWhitelistUsers().toString());
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
