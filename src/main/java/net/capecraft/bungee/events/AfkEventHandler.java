package net.capecraft.bungee.events;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.AfkHelper;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AfkEventHandler implements Listener {

//	private String[] blockedMessages = {"/t", "/w", "/msg", "/tell", "/whisper", "/m"};
//	private String[] blockedCommands = {"/warp"};
//	private String[] blockedMovement = {"/tpa", "/tpask"};
	
	@EventHandler
	public void onServerConnect(ServerConnectedEvent event) {
		AfkHelper.purgePlayer(event.getPlayer());
		if(event.getPlayer().hasPermission(Main.Groups.ALT)) {
			AfkHelper.addAltPlayer(event.getPlayer(), event.getServer());
		}
	}
	
	@EventHandler
	public void onServerDisconnect(ServerDisconnectEvent event) {
		AfkHelper.purgePlayer(event.getPlayer());
	}

	@EventHandler
	public void onLeave(PlayerDisconnectEvent event) {
		AfkHelper.purgePlayer(event.getPlayer());
	}
	
//	@EventHandler
//	public void onPlayerCommand(ChatEvent event) {
//		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
//		if(AfkHelper.isAfk(player)) {
//			if(event.isCommand()) {
//				String command = event.getMessage().split(" ")[0].toLowerCase();
//				BaseComponent[] msg = null;
//				if(ArrayUtils.contains(blockedMessages, command)) {
//					msg = Main.PREFIX.append("You cant message an alt! They are muted!").bold(false).create();
//				} else if (ArrayUtils.contains(blockedCommands, command)) {
//					msg = Main.PREFIX.append("Alts are not allowed to AFK at public farms!").bold(false).create();
//				} else if (ArrayUtils.contains(blockedMovement, command)) {
//					msg = Main.PREFIX.append("You can not tp to afk players!").bold(false).create();
//
//				}
//				
//				event.setCancelled(true);
//				player.sendMessage(msg);
//			}
//		}
//	}

}
