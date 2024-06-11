package com.wasmake.itemupgrade.command.api.help;

import lombok.Getter;
import lombok.Setter;
import com.wasmake.itemupgrade.command.api.command.AbstractCommandService;
import com.wasmake.itemupgrade.command.api.command.CommandContainer;
import com.wasmake.itemupgrade.command.api.command.WrappedCommand;
import com.wasmake.itemupgrade.command.api.sender.ICommandSender;

@Getter
@Setter
public final class HelpService {
    private final AbstractCommandService<?> commandService;
    private IHelpFormatter helpFormatter;

    public HelpService(AbstractCommandService<?> commandService) {
        this.commandService = commandService;
        this.helpFormatter = (sender, container) -> {
            sender.sendMessage("--------------------------------");
            sender.sendMessage("Help - " + container.getName());
            for (WrappedCommand c : container.getCommands().values()) {
                sender.sendMessage("/" + container.getName() + (c.getName().length() > 0 ? " " + c.getName() : "") + " " + c.getMostApplicableUsage() + " - " + c.getShortDescription());
            }
            sender.sendMessage("--------------------------------");
        };
    }

    public void sendHelpFor(ICommandSender<?> sender, CommandContainer container) {
        this.helpFormatter.sendHelpFor(sender, container);
    }

    public void sendUsageMessage(ICommandSender<?> sender, CommandContainer container, WrappedCommand command) {
        sender.sendMessage(getUsageMessage(container, command));
    }

    public String getUsageMessage(CommandContainer container, WrappedCommand command) {
        String usage = "Command Usage: /" + container.getName() + " ";
        if (command.getName().length() > 0) {
            usage += command.getName() + " ";
        }
        if (command.getUsage() != null && command.getUsage().length() > 0) {
            usage += command.getUsage();
        } else {
            usage += command.getGeneratedUsage();
        }
        return usage;
    }
}
