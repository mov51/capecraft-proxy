package net.capecraft.bungee.commands.help;

import net.capecraft.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class RulesCommand extends Command {

	public RulesCommand() {
		super("rules");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(new ComponentBuilder(Main.PREFIX)
				.append("https://capecraft.net/rules")
				.reset()
				.event(new ClickEvent(Action.OPEN_URL, "https://capecraft.net/rules"))
				.create());
	}

}
