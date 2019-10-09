package net.capecraft.bungee.commands;

import net.capecraft.Main;
import net.capecraft.bungee.helpers.ComSpyHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ComSpyCommand extends Command {

	public ComSpyCommand() {
		super("comspy");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) sender;
			if(player.hasPermission(Main.Permissions.ADMIN)) {
				if(!ComSpyHelper.isPlayerSpying(player)) {
					ComSpyHelper.addComListener(player);
					player.sendMessage(buildCommandMessage("ComSpy Enabled!"));
					return;
				} else {
					ComSpyHelper.removeComListener(player);
					player.sendMessage(buildCommandMessage("ComSpy Enabled!"));
					return;
				}
			}
		}
        sender.sendMessage(buildCommandMessage("No permission"));
	}

	private static BaseComponent[] buildCommandMessage(String message) {
    	return new ComponentBuilder("[CC] ").color(ChatColor.RED).bold(true).append(message).color(ChatColor.GREEN).bold(false).create();
    }
}
