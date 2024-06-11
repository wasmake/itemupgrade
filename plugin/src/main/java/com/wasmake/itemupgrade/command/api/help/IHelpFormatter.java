package com.wasmake.itemupgrade.command.api.help;

import com.wasmake.itemupgrade.command.api.command.CommandContainer;
import com.wasmake.itemupgrade.command.api.sender.ICommandSender;

import javax.annotation.Nonnull;

public interface IHelpFormatter {
    void sendHelpFor(@Nonnull ICommandSender<?> sender, @Nonnull CommandContainer container);
}
