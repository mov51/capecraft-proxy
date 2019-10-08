package net.capecraft.bungee.events;

import java.util.concurrent.TimeUnit;

import net.capecraft.bungee.BungeeMain;
import net.capecraft.bungee.helpers.ServerQueueHelper;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerQueueEventHandler implements Listener {

	@EventHandler
	public void onLeaveEvent(ServerDisconnectEvent event) {
		System.out.println(event.getPlayer().getName() + " has just left " + event.getTarget().getName());
		//Remove player from queue
		ServerQueueHelper.removePlayer(event.getPlayer().getUniqueId());

		//Wait 1 second then poll the server
		ProxyServer.getInstance().getScheduler().schedule(BungeeMain.INSTANCE, new Runnable() {
			@Override
			public void run() {
				ServerQueueHelper.pollServer(event.getTarget().getName());
			}        	
        }, 1, TimeUnit.SECONDS);			
	}
	
}
