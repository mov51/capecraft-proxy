package net.capecraft.bungee.events;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.AfkHelper;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AfkEventHandler implements Listener {

	@EventHandler
	public void onServerConnect(ServerConnectedEvent event) {		
		if(event.getPlayer().hasPermission(Main.Groups.ALT)) {
			AfkHelper.addAltPlayer(event.getPlayer(), event.getServer());
		}
	}
	
	@EventHandler
	public void onSwitchServer(ServerSwitchEvent event) {		
		AfkHelper.purgePlayer(event.getPlayer());
	}
		
	@EventHandler
	public void onLeave(PlayerDisconnectEvent event) {		
		AfkHelper.purgePlayer(event.getPlayer());
	}
	
}
