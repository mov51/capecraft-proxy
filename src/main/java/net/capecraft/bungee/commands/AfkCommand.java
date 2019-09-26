package net.capecraft.bungee.commands;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.AfkHelper;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AfkCommand extends Command {

	public AfkCommand() {
		super("afk");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) sender;
			//Make sure player isn't an alt
			if(player.hasPermission(Main.Groups.ALT)) {				
				player.sendMessage(new ComponentBuilder(Main.PREFIX).append("Your an alt! You are already AFK silly!").reset().create());
				return;
			}
			
			//Make sure player has AFK permission
			if(!player.hasPermission(Main.Permissions.PlAY_AFK)) {				
				player.sendMessage(new ComponentBuilder(Main.PREFIX).append("You cant AFK! If you think this is an error contact a staff member!").reset().create());
				return;
			}
						
			//Pings the sub server for checks
			player.getServer().getInfo().ping(new Callback<ServerPing>() {

				@Override
				public void done(ServerPing result, Throwable error) {
					//Checks if server is full else do AFK check
					if(result.getPlayers().getOnline() == result.getPlayers().getMax()) {						
						player.sendMessage(new ComponentBuilder(Main.PREFIX).append("You cant AFK! The server is too full!").reset().create());
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