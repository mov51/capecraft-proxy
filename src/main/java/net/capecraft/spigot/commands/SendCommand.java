package net.capecraft.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.capecraft.Main;
import net.capecraft.spigot.SpigotMain;

public class SendCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//Checks sender has permission
		if(!sender.hasPermission(Main.Permissions.ADMIN)) {
			sender.sendMessage(Main.PREFIX.append("Invalid permissions").reset().create());
			return false;
		}
			
		//Checks a server is included
		if(args.length != 1) {
			sender.sendMessage(Main.PREFIX.append("Please choose a server to send all current players").reset().create());
			return false;
		}
		
		//Checks the server name is valid
		if(!args[0].equalsIgnoreCase(Main.Servers.CREATIVE) && !args[0].equalsIgnoreCase(Main.Servers.LOBBY) && !args[0].equalsIgnoreCase(Main.Servers.SURVIVAL)) {
			sender.sendMessage(Main.PREFIX.append("That server name is invalid").reset().create());
			return false;
		}
		
		//Sends players to that server
		Bukkit.getOnlinePlayers().forEach(player -> {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(args[0]);
			player.sendPluginMessage(SpigotMain.INSTANCE, Main.Channels.BUNGEECORD, out.toByteArray());
		});
		return true;
	}
}
