package net.capecraft.bungee.commands.help;

import net.capecraft.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.plugin.Command;

public class VoteCommand extends Command {

	public VoteCommand() {
		super("vote");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("Vote for awesome rewards!").reset().create());
		sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("https://CapeCraft.net/Vote1").reset().event(new ClickEvent(Action.OPEN_URL, "https://CapeCraft.net/Vote1")).create());
		sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("https://CapeCraft.net/Vote2").reset().event(new ClickEvent(Action.OPEN_URL, "https://CapeCraft.net/Vote2")).create());
		sender.sendMessage(new ComponentBuilder(Main.PREFIX).append("https://CapeCraft.net/Vote3").reset().event(new ClickEvent(Action.OPEN_URL, "https://CapeCraft.net/Vote3")).create());
	} 

}
