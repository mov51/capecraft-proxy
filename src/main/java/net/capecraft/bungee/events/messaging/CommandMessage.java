package net.capecraft.bungee.events.messaging;

import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.capecraft.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CommandMessage implements Listener {

	@EventHandler
	public void onPluginMessageReceived(PluginMessageEvent event) {
		//Checks channel is correct
		if (!event.getTag().equalsIgnoreCase(Main.Channels.CONFIG_CHANNEL)) {
			return;
		}
		
		//Gets data from message
		ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());    	   
		String playerUUID = in.readUTF();
		String commandToRun = in.readUTF();
		
		//converts uuid to player
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(UUID.fromString(playerUUID));
		if(player != null) {
			//If player not null, do command
			ProxyServer.getInstance().getPluginManager().dispatchCommand(player, commandToRun);
		}	    
	}

}
