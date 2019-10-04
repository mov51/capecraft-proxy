package net.capecraft.bungee.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.MojangAPIHelper;
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
			//Get the player uuid
			UUID playerUUID;			
			
			//Try get online player
			ProxiedPlayer player = (args.length == 1) ? ProxyServer.getInstance().getPlayer(args[0]) : (ProxiedPlayer) sender;
			if(player != null) {
				playerUUID = player.getUniqueId();				
			} else {
				//Else try get uuid from api
				UUID uuid = MojangAPIHelper.getUUID(args[0]);
				if(uuid != null) {		
					playerUUID = uuid;
				} else {
					//Else display error
					sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("That player can't be found").reset().create());
					return;
				}
			}
			
			//Update players playtime
			PlayTimeHelper.updatePlaytime(playerUUID);
			
			//Get Player response
			String username = (player != null) ? player.getDisplayName() : args[0];			
			
			//Display playtime message
			String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.PLAYTIME_MESSAGE);
			msgRaw = msgRaw.replace("%player%", username);
			msgRaw = msgRaw.replace("%playtime%", PlayTimeHelper.getPlaytime(playerUUID));
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
