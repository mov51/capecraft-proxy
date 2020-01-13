package net.capecraft.bungee.events;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.PlayTimeHelper;
import net.capecraft.bungee.helpers.config.PlayerConfig;
import net.capecraft.bungee.helpers.config.PluginConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
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
		if(!PlayerConfig.doesConfigExistElseCreate(player.getUniqueId())) {
			String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.FIRST_JOIN_MESSAGE);
			msgRaw = msgRaw.replace("%player%", player.getDisplayName());
			BaseComponent[] msg = new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE)).reset().create();
			ProxyServer.getInstance().broadcast(msg);
		}

		//Fill config values of player
		Configuration playerConfig = PlayerConfig.getPlayerConfig(player.getUniqueId());
		playerConfig.set(Main.PlayerConfigs.USERNAME, player.getDisplayName());
		playerConfig.set(Main.PlayerConfigs.JOIN_TIME, (System.currentTimeMillis() / 1000));
		playerConfig.set(Main.PlayerConfigs.IS_ALT, player.hasPermission(Main.Groups.ALT));
		playerConfig.set(Main.PlayerConfigs.IS_AFK, false);
		PlayerConfig.saveConfig(player.getUniqueId(), playerConfig);

		PlayTimeHelper.checkPlayerRank(player.getUniqueId());
	}

	/*
	 * Runs when a players leaves and updates playtime
	 */
	@EventHandler
	public void onLeave(ServerDisconnectEvent event) {
		ProxiedPlayer player = event.getPlayer();
		PlayTimeHelper.updatePlaytime(player.getUniqueId());
	}
}
