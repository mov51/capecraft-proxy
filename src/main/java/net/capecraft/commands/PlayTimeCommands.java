package net.capecraft.commands;

import net.capecraft.Main;
import net.capecraft.helpers.PlayTimeHelper;
import net.capecraft.helpers.config.PluginConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PlayTimeCommands extends Command {

	public PlayTimeCommands() {
		super("playtime");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {			
			//Get ProxiedPlayer instance
			ProxiedPlayer player = (ProxiedPlayer) sender;			
			
			//Update players playtime
			PlayTimeHelper.updatePlaytime(player.getUniqueId());
			
			//Display playtime message
			String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.PLAYTIME_MESSAGE);
			msgRaw = msgRaw.replace("%player%", player.getDisplayName());
			msgRaw = msgRaw.replace("%playtime%", PlayTimeHelper.getPlaytime(player.getUniqueId()));
			BaseComponent[] msg = new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE)).reset().create();
			player.sendMessage(msg);
		}
	}
	
	
}
