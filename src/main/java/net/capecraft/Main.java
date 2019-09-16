package net.capecraft;

import java.util.logging.Level;

import net.capecraft.commands.PluginCommands;
import net.capecraft.events.JoinLeave;
import net.capecraft.utils.ConfigurationManager;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
	
    @Override
    public void onEnable() {
    	getLogger().log(Level.INFO, "Loading CapeCraft Proxy");
    	
    	//Initialise Configuration Manager
    	ConfigurationManager.initConfig(this);
    	
    	//Load Events
        getProxy().getPluginManager().registerListener(this, new JoinLeave());
        
        //Commands
        getProxy().getPluginManager().registerCommand(this, new PluginCommands());
        
        getLogger().log(Level.INFO, "Loaded");
    }
    
    @Override
    public void onDisable() {    	    	
    	getLogger().log(Level.INFO, "Unloaded");
    }
}
