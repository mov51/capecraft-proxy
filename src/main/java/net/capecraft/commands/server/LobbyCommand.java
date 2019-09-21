package net.capecraft.commands.server;

import net.capecraft.helpers.ServerPinger;
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
			ServerPinger sp = new ServerPinger("creative");
			ProxyServer.getInstance().broadcast(sp.getPlayers() + "/" + sp.getMaxPlayers() + " Online");
			ServerInfo lobby = ProxyServer.getInstance().getServerInfo("lobby");
			((ProxiedPlayer) sender).connect(lobby);
		}
	}

}
