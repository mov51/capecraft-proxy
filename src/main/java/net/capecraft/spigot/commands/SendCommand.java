package net.capecraft.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.capecraft.Main;
import net.capecraft.spigot.SpigotMain;

public class SendCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(!sender.hasPermission(Main.Permissions.ADMIN)) {
			((Player) sender).sendMessage(Main.PREFIX.append("Invalid permissions").reset().create());
			return false;
		}
		
		if(args.length != 1) {
			((Player) sender).sendMessage(Main.PREFIX.append("Please choose a server to send all current players").reset().create());
			return false;
		}
		
		if(!args[0].equalsIgnoreCase(Main.Servers.CREATIVE) && !args[0].equalsIgnoreCase(Main.Servers.LOBBY) && !args[0].equalsIgnoreCase(Main.Servers.SURVIVAL)) {
			((Player) sender).sendMessage(Main.PREFIX.append("That server name is invalid").reset().create());
			return false;
		}
		
		Bukkit.getOnlinePlayers().forEach(player -> {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(args[0]);
			player.sendPluginMessage(SpigotMain.INSTANCE, Main.Channels.BUNGEECORD, out.toByteArray());
		});
		return true;
	}
}
