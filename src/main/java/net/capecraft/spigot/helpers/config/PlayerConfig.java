package net.capecraft.spigot.helpers.config;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.capecraft.Main;
import net.capecraft.spigot.SpigotMain;

public class PlayerConfig {

	/**
	 * Updates player config by sending bungee some data across config channel
	 * @param player The players config to update
	 * @param line The line of config to update
	 * @param value The value to update
	 */
	public static void updateConfig(Player player, String line, Object value) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("update");
		out.writeUTF(player.getUniqueId().toString());
		out.writeUTF(line);
		out.writeUTF(value.toString());
		sendPacket(player, out);
	}
	
	private static void sendPacket(Player player, ByteArrayDataOutput out) {
		player.sendPluginMessage(SpigotMain.INSTANCE, Main.Channels.CONFIG_CHANNEL, out.toByteArray());	
	}
	
}
