package net.capecraft.bungee.commands.server;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCommand extends Command {

	public LobbyCommand() {
		super("lobby", null, "hub");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ServerInfo lobby = ProxyServer.getInstance().getServerInfo("lobby");
			((ProxiedPlayer) sender).connect(lobby);
		}
	}

}
