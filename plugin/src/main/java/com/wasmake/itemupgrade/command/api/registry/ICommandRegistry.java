package com.wasmake.itemupgrade.command.api.registry;

import com.wasmake.itemupgrade.command.api.command.CommandContainer;
import com.wasmake.itemupgrade.command.api.exception.CommandRegistrationException;

import javax.annotation.Nonnull;

public interface ICommandRegistry<T extends CommandContainer> {
    boolean register(@Nonnull T container, boolean unregisterExisting) throws CommandRegistrationException;
}
