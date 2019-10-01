package net.capecraft.bungee.commands;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.PlayTimeHelper;
import net.capecraft.bungee.helpers.config.PluginConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class PlayTimeCommands extends Command implements TabExecutor {

	public PlayTimeCommands() {
		super("playtime");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			//Get the player
			ProxiedPlayer player = (args.length == 1) ? ProxyServer.getInstance().getPlayer(args[0]) : (ProxiedPlayer) sender;
			
			//Make sure supplied player isn't null
			if(player == null) {
				sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("That player can't be found or are they definitely online?").reset().create());
				return;
			}
			
			//Update players playtime
			PlayTimeHelper.updatePlaytime(player);
			
			//Display playtime message
			String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.PLAYTIME_MESSAGE);
			msgRaw = msgRaw.replace("%player%", player.getDisplayName());
			msgRaw = msgRaw.replace("%playtime%", PlayTimeHelper.getPlaytime(player.getUniqueId()));
			BaseComponent[] msg = new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE)).reset().create();
			sender.sendMessage(msg);
		}
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		//Make sure the argument is the first one
	    if (args.length != 1) {
	        return ImmutableSet.of();
	    }
	
	    //Get a set of players to tab complete with
	    Set<String> matches = new HashSet<>();
    	String search = args[0].toLowerCase();
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if(player.getName().toLowerCase().startsWith( search )) {
                matches.add( player.getName() );
            }
	    }
	    return matches;
	}
	
}
