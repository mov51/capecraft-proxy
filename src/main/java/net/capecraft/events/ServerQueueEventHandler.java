package net.capecraft.events;

import net.capecraft.helpers.ServerQueueHelper;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerQueueEventHandler implements Listener {

	@EventHandler
	public void onLeaveEvent(PlayerDisconnectEvent event) {
		ServerQueueHelper.removePlayer(event.getPlayer().getUniqueId());
	}
	
}
