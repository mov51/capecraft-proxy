package net.capecraft.bungee.commands.help;

import net.capecraft.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.plugin.Command;

public class DonateCommand extends Command {

	public DonateCommand() {
		super("donate");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(new ComponentBuilder(Main.PREFIX)
				.append("Donate for awesome perks and to support the server!")
				.reset()
				.append(" https://capecraft.net/donate")
				.event(new ClickEvent(Action.OPEN_URL, "https://capecraft.net/donate"))
				.create());
	}

}