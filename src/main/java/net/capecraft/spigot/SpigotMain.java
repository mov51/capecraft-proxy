package net.capecraft.spigot;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.capecraft.Main;
import net.capecraft.spigot.commands.server.CreativeCommand;
import net.capecraft.spigot.commands.server.LobbyCommand;
import net.capecraft.spigot.commands.server.SurvivalCommand;
import net.capecraft.spigot.events.messenger.CommandMessage;
import net.md_5.bungee.api.chat.TextComponent;

public class SpigotMain extends JavaPlugin {
	
	public static Plugin INSTANCE;	
	
	@Override
	public void onEnable() {
		
		this.logLogo();
		SpigotMain.INSTANCE = this;
		
		//Register listener channels
	    getServer().getMessenger().registerOutgoingPluginChannel(this, Main.Channels.CONFIG_CHANNEL);
	    getServer().getMessenger().registerIncomingPluginChannel(this, Main.Channels.CONFIG_CHANNEL, new CommandMessage());
		
	    //Server Commands
		getCommand("lobby").setExecutor(new LobbyCommand());
		getCommand("creative").setExecutor(new CreativeCommand());
		getCommand("survival").setExecutor(new SurvivalCommand());		
	}
	
    /*
     * Show our pretty logo     
     */
    private void logLogo() {
    	ConsoleCommandSender sender = getServer().getConsoleSender();    	
    	String lime = ChatColor.COLOR_CHAR + "a";
    	String aqua = ChatColor.COLOR_CHAR + "b";
    	
    	sender.sendMessage(TextComponent.fromLegacyText(lime + " _____                  _____            __ _   "));
    	sender.sendMessage(TextComponent.fromLegacyText(lime + "/  __ \\                /  __ \\          / _| |  "));
    	sender.sendMessage(TextComponent.fromLegacyText(lime + "| /  \\/ __ _ _ __   ___| /  \\/_ __ __ _| |_| |_ "));
    	sender.sendMessage(TextComponent.fromLegacyText(lime + "| |    / _` | '_ \\ / _ \\ |   | '__/ _` |  _| __|"));
    	sender.sendMessage(TextComponent.fromLegacyText(lime + "| \\__/\\ (_| | |_) |  __/ \\__/\\ | | (_| | | | |_ "));
    	sender.sendMessage(TextComponent.fromLegacyText(lime + " \\____/\\__,_| .__/ \\___|\\____/_|  \\__,_|_|  \\__|"));
    	sender.sendMessage(TextComponent.fromLegacyText(lime + "            | |                                 "));
    	sender.sendMessage(TextComponent.fromLegacyText(lime + "            |_|   "+aqua+"      https://capecraft.net          "));
    	sender.sendMessage(TextComponent.fromLegacyText(""));
      }
}
