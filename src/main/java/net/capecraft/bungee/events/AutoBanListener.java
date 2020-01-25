package net.capecraft.bungee.events;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import me.leoko.advancedban.bungee.event.PunishmentEvent;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.manager.TimeManager;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class AutoBanListener implements Listener {
	
	//List of recent bans inside of grace
	//IP, time, name
	private static HashMap<String, Entry<Long, String>> recentBans = new HashMap<>();
	
	/**
	 * On a ban event we need to get the IP banned
	 * @param event
	 */
	@EventHandler
	public static void onPunishment(PunishmentEvent event) {
		Punishment punish = event.getPunishment();
		if(punish.getType() == PunishmentType.BAN || punish.getType() == PunishmentType.TEMP_BAN) {
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(punish.getUuid());
			Entry<Long, String> playerInfo = new SimpleEntry<Long, String>(System.currentTimeMillis() / 1000, player.getName());
			recentBans.put(player.getAddress().getAddress().getHostAddress().toString(), playerInfo);
			
			for(ProxiedPlayer onlinePlayer : ProxyServer.getInstance().getPlayers()) {
				if(onlinePlayer.getAddress().getAddress().getHostAddress().equals(player.getAddress().getAddress().getHostAddress())) {
					onlinePlayer.disconnect();
				}
			}
		}
	}
	
	/**
	 * First thing, check if a players IP is banned. If so lets rid of them
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
    public static void onConnect(LoginEvent event) {
		event.getConnection().getUniqueId();
		String uuid = event.getConnection().getUniqueId().toString().replace("-", "");
		String ipAddr = event.getConnection().getAddress().getAddress().getHostAddress();
		
		if(!PunishmentManager.get().isBanned(uuid)) {			
			if(checkBan(ipAddr)) {
				String banReason = "Evading punishment of an alt (%banned_player%)".replace("%banned_player%", getBan(ipAddr).getValue());				
				new Punishment(event.getConnection().getName(), uuid, banReason, "CapeCraft", PunishmentType.BAN, TimeManager.getTime(), -1, null, -1).create();
				event.setCancelReason(new ComponentBuilder(banReason).create());
				event.setCancelled(true);
			}	
		}
	}
	
	/**
	 * Check if the IP provided has been previously
	 * @param ip
	 * @return
	 */
	private static boolean checkBan(String ip) {
		if(recentBans.containsKey(ip)) {
			if((recentBans.get(ip).getKey() + 600) > System.currentTimeMillis() / 1000) {
				return true;
			} else {
				recentBans.remove(ip);
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Check if the connecting player is in the ban list
	 * @param ip Connecting Players IP
	 * @return
	 */
	private static Entry<Long, String> getBan(String ip) {
		if(checkBan(ip)) {
			return recentBans.get(ip);
		}
		return null;		
	}
}
