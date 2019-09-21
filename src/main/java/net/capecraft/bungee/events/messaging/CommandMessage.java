package net.capecraft.bungee.events.messaging;

import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CommandMessage implements Listener {

	@EventHandler
	public void onPluginMessageReceived(PluginMessageEvent event) {
		if (!event.getTag().equalsIgnoreCase("CapeCraft")) {
			return;
		}
		
		ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());	    
    	String subCommand = in.readUTF();
    	if(subCommand.equals("command")) {
    		String playerInstance = in.readUTF();
    		String commandToRun = in.readUTF();
    		
    		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(UUID.fromString(playerInstance));
    		if(player != null) {
    			ProxyServer.getInstance().getPluginManager().dispatchCommand(player, commandToRun);
    		}
	    }
	}

}
