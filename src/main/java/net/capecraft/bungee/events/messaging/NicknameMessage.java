package net.capecraft.bungee.events.messaging;

import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.NicknameHelper;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class NicknameMessage implements Listener {

	@EventHandler
	public void onPluginMessageReceived(PluginMessageEvent event) {		
		//Checks channel is correct
		if (!event.getTag().equalsIgnoreCase(Main.Channels.NICKNAME_CHANNEL)) {
			return;
		}

		//Gets data from message
		ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
		String playerUUID = in.readUTF();
		String nickname = in.readUTF();

		//Add nickname to player uuid
		NicknameHelper.nicknames.put(UUID.fromString(playerUUID), nickname);
	}	
}
