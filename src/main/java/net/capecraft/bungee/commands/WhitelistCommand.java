package net.capecraft.bungee.commands;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.WhitelistHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class WhitelistCommand extends Command implements TabExecutor {

	public WhitelistCommand() {
		super("bwhitelist", Main.Permissions.ADMIN);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		//Turn whitelist on
		if(args.length == 1 && args[0].toLowerCase().equals("on")) {
			WhitelistHelper.setWhitelist(true);
			sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("Whitelist on").reset().create());
		//Turn whitelist off
		} else if(args.length == 1 && args[0].toLowerCase().equals("off")) {
			WhitelistHelper.setWhitelist(false);
			sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("Whitelist off").reset().create());
		//Add player to whitelist
		} else if(args.length == 2 && args[0].toLowerCase().equals("add")) {			
			WhitelistHelper.addWhitelist(args[1]);
			sender.sendMessage(new ComponentBuilder(Main.PREFIX).append(args[1] + " added to whitelist").reset().create());
		//Remove player from whitelist
		} else if(args.length == 2 && args[0].toLowerCase().equals("remove")) {
			WhitelistHelper.removeWhitelist(args[1]);
			sender.sendMessage(new ComponentBuilder(Main.PREFIX).append(args[1] + " removed from whitelist").reset().create());
		//Display valid commands
		} else {
			sender.sendMessage(new ComponentBuilder("Usage: /whitelist [on|of|add|remove]").color(ChatColor.RED).create());
		}
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		//Check if first arg to show help tabs
		if(args.length == 1) {
			Set<String> matches = new HashSet<>();
			matches.add("on");
			matches.add("off");
			matches.add("add");
			matches.add("remove");
			return matches;	
		//If second arg then loop through players
		} else if(args.length == 2) {
		    //Get a set of players to tab complete with
		    Set<String> matches = new HashSet<>();
	    	String search = args[1].toLowerCase();
	        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
	            if(player.getName().toLowerCase().startsWith( search )) {
	                matches.add( player.getName() );
	            }
		    }
		    return matches;
		//Else return nothing
	    } else {	    	
	    	return ImmutableSet.of();
	    }
	}

}
