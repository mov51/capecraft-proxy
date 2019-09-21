package net.capecraft.spigot.commands.server;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.capecraft.spigot.SpigotMain;

public class SurvivalCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Message");
			out.writeUTF("ALL");
			out.writeUTF("/survival");
			((Player) sender).sendPluginMessage(SpigotMain.INSTANCE, "BungeeCord", out.toByteArray());			
			return true;
		}		
		return false;
	}
}
