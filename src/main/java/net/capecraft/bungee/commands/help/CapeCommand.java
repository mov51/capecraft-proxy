package net.capecraft.bungee.commands.help;

import net.capecraft.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class CapeCommand extends Command {

	public CapeCommand() {
		super("cape");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(new ComponentBuilder(Main.PREFIX)				
				.append("To get a cape visit ")
				.reset()
				.append("https://minecraftcapes.co.uk/discord")				
				.event(new ClickEvent(Action.OPEN_URL, "https://minecraftcapes.co.uk/discord"))
				.create());
	}

}