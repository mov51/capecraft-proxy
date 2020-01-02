package net.capecraft.spigot.events.messenger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.earth2me.essentials.Essentials;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.capecraft.Main;
import net.capecraft.spigot.SpigotMain;

public class NicknameMessage implements Listener {

	private static Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	
	@EventHandler
	public static void sendNickname(PlayerJoinEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Player player = event.getPlayer();
				String nickname = ess.getUserMap().getUser(player.getUniqueId())._getNickname();
				if(nickname != null) {					
					ByteArrayDataOutput out = ByteStreams.newDataOutput();		
					out.writeUTF(player.getUniqueId().toString());		
					out.writeUTF(nickname);
					player.sendPluginMessage(SpigotMain.INSTANCE, Main.Channels.NICKNAME_CHANNEL, out.toByteArray());	
				}				
			}
		}.runTaskLater(SpigotMain.INSTANCE, 1);
		
	}
	
}
