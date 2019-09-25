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
	public class Channels {
		private static final String MAIN_CHANNEL = "capecraft";
		public static final String COMMANDS_CHANNEL = MAIN_CHANNEL + ":commands";
		public static final String CONFIG_CHANNEL = MAIN_CHANNEL + ":config";
	}
	
	//Server Names
	public class Servers {
		public static final String LOBBY = "lobby";
		public static final String CREATIVE = "creative";
		public static final String SURVIVAL = "survival";
	}
	
	//Config files
	public class Configs {	
		public static final String BUNGEE_CONFIG = "bungeeconfig.yml";
		public static final String SPIGOT_CONFIG = "spigotconfig.yml";	
		public static final String PLAYER_CONFIG = "playerconfig.yml";
	}
	
	//Player config
	public class PlayerConfigs {
		public static final String USERNAME = "username";
		public static final String JOIN_TIME = "jointime";
		public static final String PLAY_TIME = "playtime";		
		public static final String IS_ALT = "alt";
		public static final String IS_AFK = "afk";
		public static final String IS_SPYING = "isSpying";
	}
}
