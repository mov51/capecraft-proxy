package net.capecraft.events;

import net.capecraft.utils.ConfigurationManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinLeave implements Listener {
	
	@EventHandler
	public void onJoinEvent(PostLoginEvent event) {		
		String msgRaw = ConfigurationManager.getPluginConfig().getString("joinMessage");
		broadcastMessage(msgRaw, event.getPlayer());
	}
	
	@EventHandler
	public void onLeaveEvent(PlayerDisconnectEvent event) {
		String msgRaw = ConfigurationManager.getPluginConfig().getString("leaveMessage");
		broadcastMessage(msgRaw, event.getPlayer());
	}
	
	private void broadcastMessage(String msgRaw, ProxiedPlayer player) {
		msgRaw = msgRaw.replace("%player%", player.getDisplayName());
		BaseComponent[] msg = TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE);
		ProxyServer.getInstance().broadcast(msg);
	}
	
}
