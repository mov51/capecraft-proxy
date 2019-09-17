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
		//Shows a custom ping if player isn't on 1.14.4
		ServerPing response = event.getResponse();
		response.getVersion().setName("Requires MC 1.14.4");
		response.getVersion().setProtocol(ProtocolConstants.MINECRAFT_1_14_4);
		event.setResponse(response);
		
	}
	
	@EventHandler
	public void onJoinEvent(PostLoginEvent event) {
		
		//Kicks player if version not 1.14.4
		if(event.getPlayer().getPendingConnection().getVersion() != ProtocolConstants.MINECRAFT_1_14_4) {
			event.getPlayer().disconnect(new ComponentBuilder(Main.PREFIX).append("Please join using version 1.14.4").reset().create());
		}
		
		//Gets msg from config and sends join/leave message		
		String msgRaw = ConfigurationManager.getPluginConfig().getString("joinMessage");
		broadcastJoinLeaveMessage(msgRaw, event.getPlayer());
	}
	
	@EventHandler
	public void onLeaveEvent(PlayerDisconnectEvent event) {
		//Gets msg from config and sends join/leave message
		String msgRaw = ConfigurationManager.getPluginConfig().getString("leaveMessage");
		broadcastJoinLeaveMessage(msgRaw, event.getPlayer());
	}
	
	/**
	 * Broadcast join/leave to player
	 * @param msgRaw The join/leave message
	 * @param player The ProxiedPlayer object
	 */
	private void broadcastJoinLeaveMessage(String msgRaw, ProxiedPlayer player) {
		//Checks player is on 1.14.4
		if(player.getPendingConnection().getVersion() == ProtocolConstants.MINECRAFT_1_14_4) {
			//Formats the message and sends it
			msgRaw = msgRaw.replace("%player%", player.getDisplayName());
			BaseComponent[] msg = TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE);
			ProxyServer.getInstance().broadcast(msg);
		}
	}
	
}
