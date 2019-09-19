package net.capecraft;

import java.util.logging.Level;

import net.capecraft.commands.PluginCommands;
import net.capecraft.commands.help.AfkRulesCommand;
import net.capecraft.commands.help.AltRulesCommand;
import net.capecraft.commands.help.CapeCommand;
import net.capecraft.commands.help.RulesCommand;
import net.capecraft.events.JoinLeave;
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
        
        //Commands
        getProxy().getPluginManager().registerCommand(this, new PluginCommands());
        
        //Help Commands
        getProxy().getPluginManager().registerCommand(this, new RulesCommand());
        getProxy().getPluginManager().registerCommand(this, new AltRulesCommand());
        getProxy().getPluginManager().registerCommand(this, new AfkRulesCommand());
        getProxy().getPluginManager().registerCommand(this, new CapeCommand());
        
        getLogger().log(Level.INFO, "Loaded");
    }
    
    @Override
    public void onDisable() {    	    	
    	getLogger().log(Level.INFO, "Unloaded");
    }
}
