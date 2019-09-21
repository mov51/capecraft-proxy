package net.capecraft;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import net.capecraft.commands.PlayTimeCommands;
import net.capecraft.commands.PluginCommands;
import net.capecraft.commands.help.AfkRulesCommand;
import net.capecraft.commands.help.AltRulesCommand;
import net.capecraft.commands.help.CapeCommand;
import net.capecraft.commands.help.RulesCommand;
import net.capecraft.commands.server.CreativeCommand;
import net.capecraft.commands.server.LobbyCommand;
import net.capecraft.commands.server.SurvivalCommand;
import net.capecraft.events.JoinLeave;
import net.capecraft.events.PlaytimeEventHandler;
import net.capecraft.events.ServerQueueEventHandler;
import net.capecraft.helpers.ServerQueueHelper;
import net.capecraft.helpers.config.PlayerConfig;
import net.capecraft.helpers.config.PluginConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
	
	private static final char ARROW = '\u00BB';
	public static final ComponentBuilder PREFIX = new ComponentBuilder("CapeCraft")
			.color(ChatColor.RED)
			.bold(true)
			.append(" " + ARROW + " ")
			.bold(false)
			.color(ChatColor.BLUE); 
	
    @Override
    public void onEnable() {
    	getLogger().log(Level.INFO, "Loading CapeCraft Proxy");
    	
    	//Initialise Configuration Manager
    	PluginConfig.initConfig(this);
    	PlayerConfig.initConfig(this);
    	
    	//Load Events
        getProxy().getPluginManager().registerListener(this, new JoinLeave());
        getProxy().getPluginManager().registerListener(this, new PlaytimeEventHandler());
        getProxy().getPluginManager().registerListener(this, new ServerQueueEventHandler());
        
        //Commands
        getProxy().getPluginManager().registerCommand(this, new PluginCommands());
        getProxy().getPluginManager().registerCommand(this, new PlayTimeCommands());
        getProxy().getPluginManager().registerCommand(this, new LobbyCommand());
        getProxy().getPluginManager().registerCommand(this, new CreativeCommand());
        getProxy().getPluginManager().registerCommand(this, new SurvivalCommand());
        
        //Help Commands
        getProxy().getPluginManager().registerCommand(this, new RulesCommand());
        getProxy().getPluginManager().registerCommand(this, new AltRulesCommand());
        getProxy().getPluginManager().registerCommand(this, new AfkRulesCommand());
        getProxy().getPluginManager().registerCommand(this, new CapeCommand());
        
        //Scheduled Events        
        getProxy().getScheduler().schedule(this, new Runnable() {
			@Override
			public void run() {				
				ServerQueueHelper.checkServerSlots();	
			}        	
        }, 0, 5, TimeUnit.SECONDS);
        
        getProxy().getScheduler().schedule(this, new Runnable() {
			@Override
			public void run() {				
				ServerQueueHelper.sendQueueMessages();	
			}        	
        }, 0, 1, TimeUnit.SECONDS);        
        
        //Loaded Log
        getLogger().log(Level.INFO, "Loaded");
    }
    
    @Override
    public void onDisable() {
    	//Unloaded Log
    	getLogger().log(Level.INFO, "Unloaded");
    }
}
