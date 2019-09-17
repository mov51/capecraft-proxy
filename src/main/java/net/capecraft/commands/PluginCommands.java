package net.capecraft.commands;

import net.capecraft.Main;
import net.capecraft.utils.ConfigurationManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class PluginCommands extends Command {

	public PluginCommands() {
		super("capecraftproxy");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("capecraft.admin")) {
			if(args.length == 1 && args[0].toLowerCase().equals("reload")) {
				ConfigurationManager.reloadPluginConfig();
				sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("Plugin reloaded!").reset().create());
			}	
		}		
	}
	
}
