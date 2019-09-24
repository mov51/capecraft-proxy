package net.capecraft.spigot;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.capecraft.Main;
import net.capecraft.spigot.commands.server.CreativeCommand;
import net.capecraft.spigot.commands.server.LobbyCommand;
import net.capecraft.spigot.commands.server.SurvivalCommand;
import net.capecraft.spigot.events.messenger.CommandMessage;

public class SpigotMain extends JavaPlugin {
	
	public static Plugin INSTANCE;	
	
	@Override
	public void onEnable() {
		
		SpigotMain.INSTANCE = this;
		
	    getServer().getMessenger().registerOutgoingPluginChannel(this, Main.PLUGIN_COMMANDS);
	    getServer().getMessenger().registerIncomingPluginChannel(this, Main.PLUGIN_COMMANDS, new CommandMessage());
		
		getCommand("lobby").setExecutor(new LobbyCommand());
		getCommand("creative").setExecutor(new CreativeCommand());
		getCommand("survival").setExecutor(new SurvivalCommand());
	}
	
}
