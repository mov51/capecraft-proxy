package net.capecraft.bungee.commands;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.TreeMap;

import net.capecraft.Main;
import net.capecraft.bungee.BungeeMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class PlayTimeTopCommand extends Command {

	private static long playtimeLastChecked = 0;
	private static TreeMap<Integer, String> playtimeCache = new TreeMap<>(Collections.reverseOrder());

	/**
	 * Get command sent
	 */
	public PlayTimeTopCommand() {
		super("playtimetop");
	}

	/**
	 * Command handler
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("Generating Top Playtime, please wait...").reset().create());
		displayPlaytime(sender);
	}

	/**
	 * Format and display the playtime
	 * 
	 * @param sender The command sender
	 */
	private static void displayPlaytime(CommandSender sender) {
		// Current time take away 10 mins is cache age
		long cacheAge = (System.currentTimeMillis() / 1000) - 600;
		if (cacheAge > playtimeLastChecked) {
			ProxyServer.getInstance().getScheduler().runAsync(BungeeMain.INSTANCE, new GeneratePlaytime(sender));
		} else {
			//First message
			sender.sendMessage(new ComponentBuilder("⎯⎯⎯⎯⎯").color(ChatColor.YELLOW).append(" Top 10 users with playtime ").reset().append("⎯⎯⎯⎯⎯").color(ChatColor.YELLOW).create());
			
			//Loop through TreeSet 10 times and output values
			playtimeCache.entrySet().stream().limit(10).forEach(value -> {
				double playtime = value.getKey();
				playtime = playtime / 60;
				DecimalFormat df = new DecimalFormat("#.##");
				
				sender.sendMessage(new ComponentBuilder(" " + df.format(playtime) + "hrs " + value.getValue()).create());
			});
			
			//Final message
			sender.sendMessage(new ComponentBuilder("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯").color(ChatColor.YELLOW).create());
		}
	}

	/**
	 * Update the playtime cache class
	 * 
	 * @param localPlaytimeCache Cache just generated
	 */
	private static void updateCache(TreeMap<Integer, String> localPlaytimeCache) {
		playtimeCache.putAll(localPlaytimeCache);
		playtimeLastChecked = System.currentTimeMillis() / 1000;
	}

	/**
	 * Generates all playtime from all users
	 */
	private static class GeneratePlaytime implements Runnable {

		private CommandSender sender;

		public GeneratePlaytime(CommandSender sender) {
			this.sender = sender;
		}

		// https://github.com/james090500/capecraft-plugin/blob/1.14/src/main/java/net/capecraft/commands/MemberCommands.java
		@Override
		public void run() {
			// Keep a local cache so we don't interfere with the current one
			TreeMap<Integer, String> playtimeCache = new TreeMap<Integer, String>();

			// Load all files from config and store playtimes in a hashmap
			for (File file : new File(BungeeMain.INSTANCE.getDataFolder() + "/users/").listFiles()) {
				if (file.getName().contains(".yml")) {
					try {
						Configuration playerConfig = ConfigurationProvider.getProvider(YamlConfiguration.class)
								.load(file);
						if (!playerConfig.getBoolean("alt")) {
							int playtime = playerConfig.getInt("playtime") / 60;
							playtimeCache.put(playtime, playerConfig.getString("username"));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			// Reverse Hashmap and put it in the main tree
			PlayTimeTopCommand.updateCache(playtimeCache);
			PlayTimeTopCommand.displayPlaytime(sender);
		}

	}

}
