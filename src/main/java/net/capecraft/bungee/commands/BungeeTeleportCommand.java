package net.capecraft.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BungeeTeleportCommand extends Command {

	public BungeeTeleportCommand() {
		super("btp");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			if(args.length == 1) {
				//checks player name
				ProxiedPlayer player = (ProxiedPlayer) sender;
				ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
				
				player.connect(target.getServer().getInfo());
				//Teleport to player
			}
		}
	}
	
	
}
