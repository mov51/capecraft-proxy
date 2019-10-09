package net.capecraft.bungee.commands;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.AfkHelper;
import net.capecraft.bungee.helpers.config.PluginConfig;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class AfkCommand extends Command {

	public AfkCommand() {
		super("afk");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) sender;
			Configuration pluginConfig = PluginConfig.getPluginConfig();
			//Make sure player isn't an alt
			if(player.hasPermission(Main.Groups.ALT)) {
				player.sendMessage(new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(pluginConfig.getString(PluginConfig.ALT_AFK))).reset().create());
				return;
			}

			//Make sure player has AFK permission
			if(!player.hasPermission(Main.Permissions.PlAY_AFK)) {
				player.sendMessage(new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(pluginConfig.getString(PluginConfig.FAIL_AFK))).reset().create());
				return;
			}

			//Checks player can AFK in this server
			if(!AfkHelper.isValidServerName(player.getServer().getInfo().getName())) {
				player.sendMessage(new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(pluginConfig.getString(PluginConfig.NO_AFK))).reset().create());
				return;
			}

			//Pings the sub server for checks
			player.getServer().getInfo().ping(new Callback<ServerPing>() {
				@Override
				public void done(ServerPing result, Throwable error) {
					//Checks if server is full else do AFK check
					if(result.getPlayers().getOnline() == result.getPlayers().getMax()) {
						player.sendMessage(new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(pluginConfig.getString(PluginConfig.FULL_AFK))).reset().create());
						return;
					} else {
						if(!AfkHelper.isAfk(player)) {
							AfkHelper.addPlayer(player);
						} else {
							AfkHelper.removePlayer(player);
						}
					}
				}
			});
		}
	}
}