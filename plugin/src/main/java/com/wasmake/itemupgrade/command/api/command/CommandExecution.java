package com.wasmake.itemupgrade.command.api.command;

import com.wasmake.itemupgrade.command.api.argument.CommandArgs;
import com.wasmake.itemupgrade.command.api.sender.ICommandSender;

import java.util.List;
public final class CommandExecution {
    private final AbstractCommandService<?> commandService;
    private final ICommandSender<?> sender;
    private final List<String> args;
    private final CommandArgs commandArgs;
    private final WrappedCommand command;
    private boolean canExecute = true;

    public CommandExecution(AbstractCommandService<?> commandService, ICommandSender<?> sender, List<String> args, CommandArgs commandArgs, WrappedCommand command) {
        this.commandService = commandService;
        this.sender = sender;
        this.args = args;
        this.commandArgs = commandArgs;
        this.command = command;
    }

    public boolean isCanExecute() {
        return canExecute;
    }
    public void preventExecution() {
        canExecute = false;
    }
}
