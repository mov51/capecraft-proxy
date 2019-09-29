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
		public static final String PLAYER_CONFIG = "playerconfig.yml";
		public static final String WHITELIST_CONFIG = "whitelist.json";
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
	
	//Permissions
	public class Permissions {
		public static final String FULL_JOIN = "capecraft.fulljoin";
		public static final String PlAY_AFK = "capecraft.playafk";
		public static final String ADMIN = "capecraft.admin";
	}
	
	public class Groups {
		//Playtime
		public static final String DEFAULT = "group.default";
		public static final String REGULAR = "group.regular";
		public static final String PLAYER = "group.player";
		public static final String MEMBER = "group.member";
		public static final String ELDER = "group.elder";
		public static final String VETERAN = "group.veteran";
		public static final String LEGEND = "group.legend";
		//Donator
		public static final String RESPECTED = "group.respected";
		public static final String PREMIUM = "group.premium";
		public static final String VIP = "group.vip";
		//Other
		public static final String ALT = "group.alt";
		public static final String AMBASSADOR = "group.ambassador";
		//Staff
		public static final String MODERATOR = "group.moderator";
		public static final String ADMINISTRATOR = "group.administrator";
		public static final String FOUNDER = "group.founder";
	}
}
