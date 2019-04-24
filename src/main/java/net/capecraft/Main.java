package net.capecraft;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
	
    @Override
    public void onEnable() {
        getLogger().info("Plugin Loaded");              
        
        ProxyServer.getInstance().registerChannel("BungeeCord");
        
        getProxy().getPluginManager().registerListener(this, new JoinLeave(this));
    }
    
    @Override
    public void onDisable() {
    	getLogger().info("Plugin Unloaded");
    }
    
}
