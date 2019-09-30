package net.capecraft.bungee.events;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.capecraft.Main;
import net.capecraft.bungee.BungeeMain;
import net.capecraft.bungee.helpers.WhitelistHelper;
import net.capecraft.bungee.helpers.config.PluginConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinLeaveEventHandler implements Listener {
	
	//Gets version msg and supported protocols from config
	private String getVersionMsg = PluginConfig.getPluginConfig().getString(PluginConfig.VERSIONS_NAME);	
	
	@EventHandler
	public void onPingEvent(ProxyPingEvent event) {
		//Get connecting clients protocol version
		int getProtocolVersion = getClientValidVersion(event.getConnection().getVersion());
		
		//Shows a custom ping if player isn't on 1.14.4
		ServerPing response = event.getResponse();
		response.getVersion().setName("Requires MC " + getVersionMsg);
		response.getVersion().setProtocol(getProtocolVersion);
		event.setResponse(response);
	}
	
	@EventHandler
	public void onPreJoinEvent(PreLoginEvent event) {
		//Get connecting clients protocol version
		int getProtocolVersion = getClientValidVersion(event.getConnection().getVersion());
		
		//Kicks player if version not correct Before they even get to PostLogin
		if(event.getConnection().getVersion() != getProtocolVersion) {
			event.getConnection().disconnect(new ComponentBuilder(Main.PREFIX).append("Please join using version ").reset().append(getVersionMsg).create());
		}
	}
	
	@EventHandler
	public void onLoginEvent(LoginEvent event) {
		//Checks whitelist isn't on		
		if(WhitelistHelper.isWhitelist() && !WhitelistHelper.inWhitelist(event.getConnection().getUniqueId())) {
			event.getConnection().disconnect(TextComponent.fromLegacyText("Whitelist on"));
		}
	}
	
	@EventHandler
	public void onJoinEvent(PostLoginEvent event) {		
		//Gets msg from config and sends join/leave message		
		String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.JOIN_MESSAGE);
		broadcastJoinLeaveMessage(msgRaw, event.getPlayer());
		sendMotd(event.getPlayer());
	}
	
	@EventHandler
	public void onLeaveEvent(PlayerDisconnectEvent event) {
		//Gets msg from config and sends join/leave message
		String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.LEAVE_MESSAGE);
		broadcastJoinLeaveMessage(msgRaw, event.getPlayer());
	}
	
	/**
	 * Broadcast join/leave to player
	 * @param msgRaw The join/leave message
	 * @param player The ProxiedPlayer object
	 */
	private void broadcastJoinLeaveMessage(String msgRaw, ProxiedPlayer player) {
		//Formats the message and sends it
		msgRaw = msgRaw.replace("%player%", player.getDisplayName());
		BaseComponent[] msg = TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE);
		ProxyServer.getInstance().broadcast(msg);
	}
	
	/**
	 * Get the support client versions else response with the support version
	 * @param clientVersion The clients version
	 * @return The supported version
	 */
	private int getClientValidVersion(int clientVersion) {
		List<?> versionList = PluginConfig.getPluginConfig().getList(PluginConfig.VERSIONS_SUPPORTED);
		for(Object supportedVersion : versionList ) {
			if(Integer.parseInt(supportedVersion.toString()) == clientVersion) {
				return clientVersion;
			}
		}
		return Integer.parseInt(versionList.get(0).toString());
	}
	
	/**
	 * Sends the MOTD to the player a couple ticks after logging in
	 * Use getConfig as its the only way to get max players cleanly
	 * <img src="https://i.imgur.com/APO1Eka.png">
	 * @param player ProxiedPlayer
	 */
	private void sendMotd(ProxiedPlayer player) {		
		ProxyServer.getInstance().getScheduler().schedule(BungeeMain.INSTANCE, new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				List<?> motd = PluginConfig.getPluginConfig().getList(PluginConfig.MOTD);				
				for(Object oLine : motd) {
					String line = oLine.toString();
					
					line = line.replace("%player%", player.getDisplayName());
					line = line.replace("%online%", String.valueOf(ProxyServer.getInstance().getOnlineCount()));
					line = line.replace("%max%", String.valueOf(ProxyServer.getInstance().getConfig().getPlayerLimit()));
					
					player.sendMessage(TextComponent.fromLegacyText(line.toString()));
				}
			}			
		}, 1, TimeUnit.SECONDS);
	}
	
}
