package net.capecraft.events;

import net.capecraft.Main;
import net.capecraft.utils.ConfigurationManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.ProtocolConstants;

public class JoinLeave implements Listener {
	
	@EventHandler
	public void onPingEvent(ProxyPingEvent event) {
		ServerPing response = event.getResponse();
		response.getVersion().setName("CapeCraft 1.14.4");
		response.getVersion().setProtocol(ProtocolConstants.MINECRAFT_1_14_4);
		event.setResponse(response);
		
	}
	
	@EventHandler
	public void onJoinEvent(PostLoginEvent event) {
		
		if(event.getPlayer().getPendingConnection().getVersion() != ProtocolConstants.MINECRAFT_1_14_4) {
			event.getPlayer().disconnect(new ComponentBuilder(Main.PREFIX).append("Please join using version 1.14.4").reset().create());
		}
		
		String msgRaw = ConfigurationManager.getPluginConfig().getString("joinMessage");
		broadcastMessage(msgRaw, event.getPlayer());
	}
	
	@EventHandler
	public void onLeaveEvent(PlayerDisconnectEvent event) {
		String msgRaw = ConfigurationManager.getPluginConfig().getString("leaveMessage");
		broadcastMessage(msgRaw, event.getPlayer());
	}
	
	private void broadcastMessage(String msgRaw, ProxiedPlayer player) {
		if(player.getPendingConnection().getVersion() == ProtocolConstants.MINECRAFT_1_14_4) {
			msgRaw = msgRaw.replace("%player%", player.getDisplayName());
			BaseComponent[] msg = TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE);
			ProxyServer.getInstance().broadcast(msg);
		}
	}
	
}
