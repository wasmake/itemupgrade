package com.wasmake.itemupgrade.command.api.modifier;

import com.wasmake.itemupgrade.command.api.command.CommandExecution;
import com.wasmake.itemupgrade.command.api.exception.CommandExitMessage;
import com.wasmake.itemupgrade.command.api.parametric.CommandParameter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public interface ICommandModifier<T> {
    Optional<T> modify(@Nonnull CommandExecution execution, @Nonnull CommandParameter commandParameter, @Nullable T argument) throws CommandExitMessage;
}
