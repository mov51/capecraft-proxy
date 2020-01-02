package net.capecraft.bungee.commands;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.capecraft.Main;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class BungeeTeleportCommand extends Command implements TabExecutor {

	public BungeeTeleportCommand() {
		super("btp", Main.Permissions.ADMIN);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			if(args.length == 1) {
				//checks player name
				ProxiedPlayer player = (ProxiedPlayer) sender;
				ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

				if(!player.hasPermission(Main.Permissions.ADMIN))
					return;

				//Make sure target exists
				if(target != null && target.isConnected()) {
					//Connect player to the target server and a callback is used to wait for connection
					player.connect(target.getServer().getInfo(), new Callback<Boolean>() {

						@Override
						public void done(Boolean isPlayerConnected, Throwable error) {
							if(!isPlayerConnected) {
								player.sendMessage(new ComponentBuilder(Main.PREFIX).append("Error connecting to server").reset().create());
								return;
							}

							//Output teleport command
							ByteArrayDataOutput out = ByteStreams.newDataOutput();
							out.writeUTF("teleport");
							out.writeUTF(player.getUniqueId().toString());
							out.writeUTF(target.getUniqueId().toString());
							target.getServer().getInfo().sendData(Main.Channels.COMMAND_CHANNEL, out.toByteArray());
						}
					});
				} else {
					player.sendMessage(new ComponentBuilder(Main.PREFIX).append("That player isn't found").reset().create());
				}
			} else {
				sender.sendMessage(new ComponentBuilder("Usage: /btp {player}").color(ChatColor.RED).create());
			}
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
