package net.capecraft.spigot.events.messenger;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.capecraft.spigot.commands.BungeeTeleportHandler;

public class CommandMessage implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {		
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String commandToRun = in.readUTF();		
		
		//Check if command in IF and send to correct handler
		if(commandToRun.equalsIgnoreCase("teleport")) {
			UUID sender = UUID.fromString(in.readUTF());
			UUID target = UUID.fromString(in.readUTF());
			BungeeTeleportHandler.teleport(sender, target);
		}				
	}

}
