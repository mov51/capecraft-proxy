package net.capecraft;

import java.util.logging.Level;

import net.capecraft.commands.PluginCommands;
import net.capecraft.events.JoinLeave;
import net.capecraft.utils.ConfigurationManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
	
	public static final ComponentBuilder PREFIX = new ComponentBuilder("CapeCraft").color(ChatColor.RED).bold(true).append(" » ").bold(false).color(ChatColor.BLUE); 
	
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
