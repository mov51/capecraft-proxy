package net.capecraft.events;

import net.capecraft.Main;
import net.capecraft.helpers.PlayTimeHelper;
import net.capecraft.helpers.config.PlayerConfig;
import net.capecraft.helpers.config.PluginConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class PlaytimeEventHandler implements Listener {

	/*
	 * Runs when a player joins. Handles first join and fills config
	 */
	@EventHandler
	public void onJoinEvent(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();
		
		//If player config doesn't exist, show first join message
		if(!PlayerConfig.doesConfigExist(player.getUniqueId())) {
			String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.FIRST_JOIN_MESSAGE);
			msgRaw = msgRaw.replace("%player%", player.getDisplayName());			
			BaseComponent[] msg = new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE)).reset().create();
			ProxyServer.getInstance().broadcast(msg);	
		}
		
		//Fill config values of player
		Configuration playerConfig = PlayerConfig.getPlayerConfig(player.getUniqueId());
		playerConfig.set(PlayerConfig.USERNAME, player.getDisplayName());		
		playerConfig.set(PlayerConfig.JOIN_TIME, (System.currentTimeMillis() / 1000));
		playerConfig.set(PlayerConfig.IS_ALT, player.hasPermission("group.alt"));
		playerConfig.set(PlayerConfig.IS_AFK, false);
		playerConfig.set(PlayerConfig.IS_SPYING, false);
	}
	
	/*
	 * Runs when a players leaves and updates playtime
	 */
	@EventHandler
	public void onLeave(PlayerDisconnectEvent event) {
		ProxiedPlayer player = event.getPlayer();
		PlayTimeHelper.updatePlaytime(player.getUniqueId());
	}
}
