package com.wasmake.itemupgrade.command.api.authorizer;

import com.wasmake.itemupgrade.command.api.command.WrappedCommand;
import com.wasmake.itemupgrade.command.api.sender.ICommandSender;

import javax.annotation.Nonnull;

public interface IAuthorizer<T> {
    boolean isAuthorized(@Nonnull ICommandSender<T> sender, @Nonnull WrappedCommand command);
}
