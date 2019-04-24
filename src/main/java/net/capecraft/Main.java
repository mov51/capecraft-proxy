package net.capecraft;

public class Main extends Plugin {
	
    @Override
    public void onEnable() {
        getLogger().info("Plugin Loaded");              
        
        BungeeCord.getInstance().registerChannel("BungeeCord");
        
        getProxy().getPluginManager().registerListener(this, new JoinLeave(this));
    }
    
    @Override
    public void onDisable() {
    	getLogger().info("Plugin Unloaded");
    }
    
}
