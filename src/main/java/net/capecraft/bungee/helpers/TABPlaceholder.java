package net.capecraft.bungee.helpers;

import me.neznamy.tab.shared.ITabPlayer;
import me.neznamy.tab.shared.placeholders.Placeholders;
import me.neznamy.tab.shared.placeholders.PlayerPlaceholder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TABPlaceholder {

	/**
	 * Adds the placeholders to TAB AFK
	 */
	public static void addPlaceholders() {
        //Hook into TAB
        Placeholders.playerPlaceholders.add(new PlayerPlaceholder("%capecraft_afk%", 2000) {
        	public String get(ITabPlayer p) {
        		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(p.getUniqueId());
        		return (AfkHelper.isAfk(player)) ? " &4&lAFK" : "";
        	}
        });
        
        Placeholders.playerPlaceholders.add(new PlayerPlaceholder("%capecraft_nick%", 2000) {
        	public String get(ITabPlayer p) {
        		return (NicknameHelper.nicknames.containsKey(p.getUniqueId())) ? NicknameHelper.nicknames.get(p.getUniqueId()) : p.getName();
        	}
        });
	}
	
}
