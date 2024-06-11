package com.wasmake.itemupgrade.command.spigot.sender;

import com.wasmake.itemupgrade.command.api.sender.ICommandSender;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public final class SpigotCommandSender implements ICommandSender<CommandSender> {
    private final CommandSender commandSender;

    public SpigotCommandSender(@Nonnull CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @Nonnull
    @Override
    public String getName() {
        return commandSender.getName();
    }

    @Override
    public void sendMessage(@Nonnull String message) {
        commandSender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(@Nonnull String permission) {
        return commandSender.hasPermission(permission);
    }

    @Nonnull
    @Override
    public CommandSender getInstance() {
        return commandSender;
    }
}
