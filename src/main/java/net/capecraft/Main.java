package net.capecraft;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class Main {
	
	private static final char ARROW = '\u00BB';
	public static final ComponentBuilder PREFIX = new ComponentBuilder("CapeCraft")
			.color(ChatColor.RED)
			.bold(true)
			.append(" " + ARROW + " ")
			.bold(false)
			.color(ChatColor.BLUE); 
}
