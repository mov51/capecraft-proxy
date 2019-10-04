package net.capecraft.spigot.helpers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.capecraft.Main;

public class AntiCheeseHelper {

	//Hashmap creation	
    private static HashMap<UUID, Long> damageEvent = new HashMap<>();
    
    /**
     * Sets the a player to be damaged
     * @param player The Player Object
     * @param combatInSeconds Seconds for the player to be in combat
     */
    public static void setDamageEvent(Player player) {
    	damageEvent.put(player.getUniqueId(), System.currentTimeMillis());
    }

    /**
     * Check if player is in combat
     * @param player The Player Object
     * @param combatInSeconds Seconds for the player to be in combat
     * @return whether the player is in combat
     */
	public static boolean isInCombat(Player player, long combatInSeconds) {
		//Get player uuid
		UUID uuid = player.getUniqueId();
		
		//Check if uuid is in damage hashmap
		if(damageEvent.containsKey(uuid)) {			
			//If it is, check the value is less than current time
			long combatTime = damageEvent.get(uuid) + (combatInSeconds * 1000);
			if(combatTime < System.currentTimeMillis()) {
				//Remove from hashmap as player not in combat
				damageEvent.remove(uuid);
				return false;
			}
			//Player is in combat
			player.sendMessage(Main.PREFIX + "You have taken damage, you need to be in a safe place to use this command!");
			return true;
		} else {
			return false;
		}
	}
}