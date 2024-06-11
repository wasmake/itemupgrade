package com.wasmake.itemupgrade.command.api.sender;

import javax.annotation.Nonnull;

public interface ICommandSender<T> {
    @Nonnull
    String getName();

    void sendMessage(@Nonnull String message);

    boolean hasPermission(@Nonnull String permission);

    @Nonnull
    T getInstance();
}
