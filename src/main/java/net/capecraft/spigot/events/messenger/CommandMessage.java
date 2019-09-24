package net.capecraft.spigot.events.messenger;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.capecraft.spigot.commands.BungeeTeleportCommand;

public class CommandMessage implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		
		System.out.println(player);
		System.out.println(message);
				
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String commandToRun = in.readUTF();
		System.out.println(commandToRun);
		
		//Check if command in IF and send to correct handler
		if(commandToRun.equalsIgnoreCase("teleport")) {
			UUID sender = UUID.fromString(in.readUTF());
			UUID target = UUID.fromString(in.readUTF());
			BungeeTeleportCommand.teleport(sender, target);
		}				
	}

}
