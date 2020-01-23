package net.capecraft.bungee.events;

import me.leoko.advancedban.bungee.event.PunishmentEvent;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;
import net.md_5.bungee.api.plugin.Listener;

public class AutoBanListener implements Listener {

	
	
	public static void onPunishment(PunishmentEvent event) {
		Punishment punish = event.getPunishment();
		if(punish.getType() == PunishmentType.BAN || punish.getType() == PunishmentType.TEMP_BAN) {
			punish.getUuid();
		}
	}

}
