package net.capecraft.spigot.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.capecraft.Main;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class BungeeTeleportCommand {

	public static void teleport(UUID senderUUID, UUID targetUUID) {
		//Teleport player to target
		Player sender = Bukkit.getPlayer(senderUUID);
		Player target = Bukkit.getPlayer(targetUUID);
		sender.teleport(target);
		sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("You've been teleported!").reset().create());
	}

}
