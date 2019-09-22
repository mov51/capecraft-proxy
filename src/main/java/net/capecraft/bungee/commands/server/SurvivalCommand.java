package net.capecraft.bungee.commands.server;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.ServerQueueHelper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SurvivalCommand extends Command {
	
	public SurvivalCommand() {
		super("survival");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ServerQueueHelper.addPlayer(Main.SURVIVAL, (ProxiedPlayer) sender);
		}
	}
	
}
