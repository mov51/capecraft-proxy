package net.capecraft.bungee.commands.server;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.ServerQueueHelper;
import net.capecraft.bungee.helpers.config.PluginConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SurvivalCommand extends Command {
	
	public SurvivalCommand() {
		super("survival");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {			
			ServerQueueHelper.addPlayer("survival", ((ProxiedPlayer) sender).getUniqueId());
			
			//Generate queue message
			String queuePos = String.valueOf(ServerQueueHelper.getQueueSize("survival"));			
			String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.QUEUE_MESSAGE);
			msgRaw = msgRaw.replace("%place%", queuePos);
			BaseComponent[] msg = new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE)).reset().create();			
			sender.sendMessage(msg);
		}
	}
	
}
