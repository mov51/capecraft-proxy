package net.capecraft.spigot.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.capecraft.Main;
import net.capecraft.spigot.SpigotMain;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class BungeeTeleportHandler {

	private static int teleportRetry = 0;

	public static void teleport(UUID senderUUID, UUID targetUUID) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Player sender = Bukkit.getPlayer(senderUUID);
				Player target = Bukkit.getPlayer(targetUUID);

				if(teleportRetry >= 5) {
					this.cancel();
				}

				if(target != null && sender != null) {
					sender.teleport(target);
					sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("You've been teleported!").reset().create());
					this.cancel();
				} else {
					teleportRetry++;
				}
			}
		}.runTaskTimer(SpigotMain.INSTANCE, 0, 20);
	}

}
