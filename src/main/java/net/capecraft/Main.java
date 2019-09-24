package net.capecraft;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class Main {
	
	//Chat Prefixes
	private static final char ARROW = '\u00BB';
	public static final ComponentBuilder PREFIX = new ComponentBuilder("CapeCraft")
			.color(ChatColor.RED)
			.bold(true)
			.append(" " + ARROW + " ")
			.bold(false)
			.color(ChatColor.BLUE);
	
	//Plugin Messaging
	private static final String MAIN_CHANNEL = "capecraft";
	public static final String PLUGIN_COMMANDS = MAIN_CHANNEL + ":commands";
	
	//Server Names
	public static final String LOBBY = "lobby";
	public static final String CREATIVE = "creative";
	public static final String SURVIVAL = "survival";
	
	//Config files
	public static final String BUNGEE_CONFIG = "bungeeconfig.yml";
	public static final String SPIGOT_CONFIG = "spigotconfig.yml";
	
	public static final String PLAYER_CONFIG = "playerconfig.yml";		
}
