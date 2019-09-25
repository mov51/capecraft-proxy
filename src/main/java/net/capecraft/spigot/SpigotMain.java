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
		
		//Register listener channels
	    getServer().getMessenger().registerOutgoingPluginChannel(this, Main.Channels.CONFIG_CHANNEL);
	    getServer().getMessenger().registerIncomingPluginChannel(this, Main.Channels.CONFIG_CHANNEL, new CommandMessage());
		
	    //Server Commands
		getCommand("lobby").setExecutor(new LobbyCommand());
		getCommand("creative").setExecutor(new CreativeCommand());
		getCommand("survival").setExecutor(new SurvivalCommand());
	}
	
}
