package net.capecraft.bungee;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import net.capecraft.bungee.commands.PlayTimeCommands;
import net.capecraft.bungee.commands.PluginCommands;
import net.capecraft.bungee.commands.help.AfkRulesCommand;
import net.capecraft.bungee.commands.help.AltRulesCommand;
import net.capecraft.bungee.commands.help.CapeCommand;
import net.capecraft.bungee.commands.help.RulesCommand;
import net.capecraft.bungee.commands.server.CreativeCommand;
import net.capecraft.bungee.commands.server.LobbyCommand;
import net.capecraft.bungee.commands.server.SurvivalCommand;
import net.capecraft.bungee.events.JoinLeave;
import net.capecraft.bungee.events.PlaytimeEventHandler;
import net.capecraft.bungee.events.ServerQueueEventHandler;
import net.capecraft.bungee.helpers.ServerQueueHelper;
import net.capecraft.bungee.helpers.config.PlayerConfig;
import net.capecraft.bungee.helpers.config.PluginConfig;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin {
	
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
        
        //Loaded Log
        getLogger().log(Level.INFO, "Loaded");
    }
    
    @Override
    public void onDisable() {
    	//Unloaded Log
    	getLogger().log(Level.INFO, "Unloaded");
    }
}
