package net.capecraft.bungee.commands;

import java.util.TreeMap;

import org.bukkit.Bukkit;

import net.capecraft.Main;
import net.capecraft.spigot.SpigotMain;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class PlayTimeTopCommand extends Command {
	
	private long playtimeLastChecked = 0;
	private TreeMap<Integer, String> playtimeCache = new TreeMap<Integer, String>();

	public PlayTimeTopCommand() {
		super("playtimetop");
	}

	/**
	 * Command handler
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("Generating Top Playtime, please wait...").reset().create());
		//Current time take away 10 mins is cache age
		long cacheAge = (System.currentTimeMillis() / 1000) - 600;
		if(cacheAge > playtimeLastChecked) {
			Bukkit.getScheduler().runTaskAsynchronously(SpigotMain.INSTANCE, new GeneratePlaytime());
		} else {
			displayPlaytime(sender, playtimeCache);
		}
	}
	
	/**
	 * Format and display the playtime
	 * @param sender The command sender
	 * @param playtime Playtime TreeMap
	 */
	private void displayPlaytime(CommandSender sender, TreeMap<Integer, String> playtime) {
		
	}
	
	private class GeneratePlaytime implements Runnable {

		@Override
		public void run() {
			//https://github.com/james090500/capecraft-plugin/blob/1.14/src/main/java/net/capecraft/commands/MemberCommands.java
		}
		
	}

}
