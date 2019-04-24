package net.capecraft;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class JoinLeave implements Listener {

	Plugin instance;
	
	public JoinLeave(Plugin instance) {
		this.instance = instance;
	}
	
	@EventHandler
	public void onJoin(PostLoginEvent event) {
			
		//1.13.2 connection, will need changing when server upgrades!
		int protocolVersion = event.getPlayer().getPendingConnection().getVersion();
		if(protocolVersion != 404 && protocolVersion != 477) {
			return;
		}
		
		for(ServerInfo servers : instance.getProxy().getServersCopy().values()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			
			out.writeUTF("CapeCraftJL");			
			out.writeUTF(event.getPlayer().getName() + " joined the game");
			
			servers.sendData("BungeeCord", out.toByteArray());			
		}
	}
	
	@EventHandler
	public void onLeave(PlayerDisconnectEvent event) {
		
		//1.13.2 connection, will need changing when server upgrades!
		if(event.getPlayer().getPendingConnection().getVersion() != 404) {
			return;
		}
		
		for(ServerInfo servers : instance.getProxy().getServersCopy().values()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			
			out.writeUTF("CapeCraftJL");			
			out.writeUTF(event.getPlayer().getName() + " left the game");
			
			servers.sendData("BungeeCord", out.toByteArray());			
		}
	}
	
}
