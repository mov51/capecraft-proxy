package net.capecraft.bungee.events;

import net.capecraft.Main;
import net.capecraft.bungee.commands.ComSpyCommand;
import net.capecraft.bungee.helpers.ComSpyHelper;
import net.capecraft.bungee.helpers.config.PlayerConfig;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ComSpyEventHandler implements Listener {

    /**
     * Get all commands and send to comspy players
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public static void PlayerCommandPreprocessEvent(ChatEvent event) {
    	if(event.getSender() instanceof ProxiedPlayer && event.isCommand()) {
    		ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
    		String command = event.getMessage();
    		String name = sender.getName();
    		String server = sender.getServer().getInfo().getName();
    		//Send labeled player commands from all servers (except their own commands)
    		for (ProxiedPlayer target : ComSpyHelper.getListeners()){
    			if(sender != target) {
    				switch (server){
						case Main.Servers.CREATIVE:
							target.sendMessage(ComSpyHelper.buildCommandMessage("[C]", name, command));
							break;
						case Main.Servers.SURVIVAL:
							target.sendMessage(ComSpyHelper.buildCommandMessage("[S]", name, command));
							break;
						case Main.Servers.LOBBY:
							target.sendMessage(ComSpyHelper.buildCommandMessage("[H]", name, command));
							break;
					}

    			}
    		}
        }
    }

    /**
     * Sets the staff comspy status depending on last join
     * @param event
     */
    @EventHandler
    public static void onStaffJoin(PostLoginEvent event) {
    	//Creates proxied player
		ProxiedPlayer player = event.getPlayer();

    	//Check player is admin
		if(player.hasPermission(Main.Permissions.ADMIN)) {
			//Gets isSpying value
			Configuration playerConfig = PlayerConfig.getPlayerConfig(player.getUniqueId());
			boolean isSpying = playerConfig.getBoolean(Main.PlayerConfigs.IS_SPYING);
			//If isSpying is null
			if(isSpying) {
				player.sendMessage(ComSpyCommand.buildCommandMessage("ComSpy Enabled!"));
				ComSpyHelper.addComListener(player);
			}
		}

    }
}
