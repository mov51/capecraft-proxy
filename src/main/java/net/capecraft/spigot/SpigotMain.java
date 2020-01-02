package net.capecraft.spigot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.capecraft.Main;
import net.capecraft.spigot.commands.WildCommand;
import net.capecraft.spigot.commands.server.CreativeCommand;
import net.capecraft.spigot.commands.server.LobbyCommand;
import net.capecraft.spigot.commands.server.SurvivalCommand;
import net.capecraft.spigot.events.AntiCheeseEvent;
import net.capecraft.spigot.events.messenger.CommandMessage;
import net.capecraft.spigot.events.messenger.NicknameMessage;
import net.capecraft.spigot.events.protect.ArmorStandProtect;
import net.capecraft.spigot.events.protect.ItemFrameProtect;
import net.md_5.bungee.api.chat.TextComponent;

public class SpigotMain extends JavaPlugin {

	public static Plugin INSTANCE;

	@Override
	public void onEnable() {

		this.logLogo();
		SpigotMain.INSTANCE = this;

		//Register listener channels		
	    getServer().getMessenger().registerOutgoingPluginChannel(this, Main.Channels.COMMAND_CHANNEL);
	    getServer().getMessenger().registerOutgoingPluginChannel(this, Main.Channels.NICKNAME_CHANNEL);
	    getServer().getMessenger().registerIncomingPluginChannel(this, Main.Channels.COMMAND_CHANNEL, new CommandMessage());

	    //Server Commands
		getCommand("lobby").setExecutor(new LobbyCommand());
		getCommand("creative").setExecutor(new CreativeCommand());
		getCommand("survival").setExecutor(new SurvivalCommand());

		//Global Event Listeners
		getServer().getPluginManager().registerEvents(new NicknameMessage(), this);
		
		//Creates the folder
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		
		//Individual Server Commands
		if(Bukkit.getServer().getName().equalsIgnoreCase(Main.Servers.SURVIVAL)) {
			//Commands
			getCommand("wild").setExecutor(new WildCommand());

			//Event Listeners
			getServer().getPluginManager().registerEvents(new AntiCheeseEvent(), this);
			getServer().getPluginManager().registerEvents(new ArmorStandProtect(this), this);
			getServer().getPluginManager().registerEvents(new ItemFrameProtect(this), this);
		}
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
