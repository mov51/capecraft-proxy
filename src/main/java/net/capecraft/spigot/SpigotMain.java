package net.capecraft.spigot;

import org.bukkit.plugin.java.JavaPlugin;

import net.capecraft.spigot.commands.server.LobbyCommand;

public class SpigotMain extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
	    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		getCommand("hub").setExecutor(new LobbyCommand());
		getCommand("lobby").setExecutor(new LobbyCommand());
	}
	
}
