package net.capecraft.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class PluginCommands extends Command {

	public PluginCommands() {
		super("capecraftproxy");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {		
		if(args.length == 1 && args[0].toLowerCase().equals("reload")) {
			sender.sendMessage("Reload Plugin");
		}
	}
	
}
