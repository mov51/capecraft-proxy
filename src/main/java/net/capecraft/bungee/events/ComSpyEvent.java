package net.capecraft.bungee.events;

import net.capecraft.bungee.helpers.ComSpyHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ComSpyEvent implements Listener {

    /**
     * Get all commands and send to comspy players 
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public static void PlayerCommandPreprocessEvent(ChatEvent event) {
    	if(event.getSender() instanceof ProxiedPlayer && event.isCommand()) {
    		ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
    		String command = event.getMessage();
    		String name = sender.getName();

    		//Send players command to staff if in same server (except their own commands)    		    	
    		for (ProxiedPlayer target : ComSpyHelper.getListeners()){
    			if(sender != target && target.getServer().getInfo().equals(sender.getServer().getInfo())) {
    				target.sendMessage(buildMessage(name, command));
    			}
    		}
        }
    }
    
    /**
     * Build the message component
     * @param name Player who initiated the command
     * @param command The command
     * @return The built message
     */
    private static BaseComponent[] buildMessage(String name, String command) {
    	return new ComponentBuilder("[CC]").color(ChatColor.RED).bold(true).append( name + ": " + command).color(ChatColor.YELLOW).bold(false).create();
    }
}
