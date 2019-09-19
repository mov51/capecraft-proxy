package net.capecraft.commands;

import net.capecraft.Main;
import net.capecraft.helpers.config.PluginConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class PluginCommands extends Command {

	public PluginCommands() {
		super("capecraftproxy", "capecraft.admin");
	}

	@Override	
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 1 && args[0].toLowerCase().equals("reload")) {
			PluginConfig.reloadPluginConfig();
			sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("Plugin reloaded!").reset().create());
		} else {
			sender.sendMessage(new ComponentBuilder("Usage: /capecraftproxy [reload]").color(ChatColor.RED).create());
		}
	}
	
}
