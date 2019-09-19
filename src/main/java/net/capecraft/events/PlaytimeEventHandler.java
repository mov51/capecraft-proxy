package net.capecraft.events;

import net.capecraft.helpers.config.PlayerConfig;
import net.capecraft.helpers.config.PluginConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class PlaytimeEventHandler implements Listener {

	@EventHandler
	public void onJoinEvent(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();
		if(PlayerConfig.doesConfigExist(player.getUniqueId())) {
			String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.FIRST_JOIN_MESSAGE);
			msgRaw = msgRaw.replace("%player%", player.getDisplayName());
			BaseComponent[] msg = TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE);
			ProxyServer.getInstance().broadcast(msg);	
		}
		
		Configuration playerConfig = PlayerConfig.getPlayerConfig(player.getUniqueId());
		playerConfig.set(PlayerConfig.USERNAME, player.getDisplayName());		
		playerConfig.set(PlayerConfig.JOIN_TIME, (System.currentTimeMillis() / 1000));		
		playerConfig.set(PlayerConfig.IS_AFK, false);
	}
	
}
