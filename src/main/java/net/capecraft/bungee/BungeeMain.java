package net.capecraft.bungee;

import java.util.logging.Level;

import net.capecraft.Main;
import net.capecraft.bungee.commands.AfkCommand;
import net.capecraft.bungee.commands.BungeeTeleportCommand;
import net.capecraft.bungee.commands.ComSpyCommand;
import net.capecraft.bungee.commands.PlayTimeCommands;
import net.capecraft.bungee.commands.PluginCommands;
import net.capecraft.bungee.commands.WhitelistCommand;
import net.capecraft.bungee.commands.help.AfkRulesCommand;
import net.capecraft.bungee.commands.help.AltRulesCommand;
import net.capecraft.bungee.commands.help.CapeCommand;
import net.capecraft.bungee.commands.help.RulesCommand;
import net.capecraft.bungee.commands.server.CreativeCommand;
import net.capecraft.bungee.commands.server.LobbyCommand;
import net.capecraft.bungee.commands.server.SurvivalCommand;
import net.capecraft.bungee.events.AfkEventHandler;
import net.capecraft.bungee.events.ComSpyEventHandler;
import net.capecraft.bungee.events.JoinLeaveEventHandler;
import net.capecraft.bungee.events.PlaytimeEventHandler;
import net.capecraft.bungee.events.ServerQueueEventHandler;
import net.capecraft.bungee.events.messaging.CommandMessage;
import net.capecraft.bungee.helpers.AfkHelper;
import net.capecraft.bungee.helpers.ServerQueueHelper;
import net.capecraft.bungee.helpers.config.PlayerConfig;
import net.capecraft.bungee.helpers.config.PluginConfig;
import net.capecraft.bungee.helpers.config.WhitelistConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin {
	
	public static Plugin INSTANCE;
	
    @Override
    public void onEnable() {
    	this.logLogo();
    	getLogger().log(Level.INFO, "Loading CapeCraft Proxy");
    	
    	BungeeMain.INSTANCE = this;
    	
    	//Initialise Configuration Manager
    	PluginConfig.initConfig(this);
    	PlayerConfig.initConfig(this);
    	WhitelistConfig.initConfig(this);
    	
    	//Register Channel
    	getProxy().registerChannel(Main.Channels.CONFIG_CHANNEL);
    	
    	//Load Events
        getProxy().getPluginManager().registerListener(this, new JoinLeaveEventHandler());
        getProxy().getPluginManager().registerListener(this, new PlaytimeEventHandler());
        getProxy().getPluginManager().registerListener(this, new ServerQueueEventHandler());
        getProxy().getPluginManager().registerListener(this, new CommandMessage());
        getProxy().getPluginManager().registerListener(this, new ComSpyEventHandler());
        getProxy().getPluginManager().registerListener(this, new AfkEventHandler());
        
        //Play Commands
        getProxy().getPluginManager().registerCommand(this, new AfkCommand());
        getProxy().getPluginManager().registerCommand(this, new PlayTimeCommands());
        
        //Admin Commands
        getProxy().getPluginManager().registerCommand(this, new PluginCommands());
        getProxy().getPluginManager().registerCommand(this, new BungeeTeleportCommand());
        getProxy().getPluginManager().registerCommand(this, new WhitelistCommand());
        getProxy().getPluginManager().registerCommand(this, new ComSpyCommand());
        
        //Server Commands
        getProxy().getPluginManager().registerCommand(this, new LobbyCommand());
        getProxy().getPluginManager().registerCommand(this, new CreativeCommand());
        getProxy().getPluginManager().registerCommand(this, new SurvivalCommand());
        
        //Help Commands
        getProxy().getPluginManager().registerCommand(this, new RulesCommand());
        getProxy().getPluginManager().registerCommand(this, new AltRulesCommand());
        getProxy().getPluginManager().registerCommand(this, new AfkRulesCommand());
        getProxy().getPluginManager().registerCommand(this, new CapeCommand());
        
        //Scheduled Events
        ServerQueueHelper.scheduleServerPing();
        AfkHelper.scheduleAfkMessage();
        
        //Loaded Log
        getLogger().log(Level.INFO, "Loaded");
    }
    
    @Override
    public void onDisable() {
    	//Unloaded Log
    	getLogger().log(Level.INFO, "Unloaded");
    }
    
    /*
     * Show our pretty logo     
     */
    private void logLogo() {
    	CommandSender sender = getProxy().getConsole();
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
