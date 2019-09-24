package net.capecraft.bungee.commands;

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

public class BungeeTeleportCommand extends Command {

	public BungeeTeleportCommand() {
		super("btp");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			if(args.length == 1) {
				//checks player name
				ProxiedPlayer player = (ProxiedPlayer) sender;
				ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
				
				if(!player.hasPermission("capecraft.admin"))
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
							target.getServer().getInfo().sendData(Main.PLUGIN_COMMANDS, out.toByteArray());
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
	
	
}
