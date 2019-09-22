package net.capecraft.spigot.commands.server;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.capecraft.Main;
import net.capecraft.spigot.SpigotMain;

public class LobbyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF(((Player) sender).getUniqueId().toString());
			out.writeUTF("lobby");
			((Player) sender).sendPluginMessage(SpigotMain.INSTANCE, Main.PLUGIN_COMMANDS, out.toByteArray());			
			return true;
		}		
		return false;
	}

}
