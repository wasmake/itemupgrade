package com.wasmake.itemupgrade.command.spigot.provider;

import com.wasmake.itemupgrade.command.api.argument.CommandArg;
import com.wasmake.itemupgrade.command.api.exception.CommandExitMessage;
import com.wasmake.itemupgrade.command.api.parametric.CommandProvider;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class CommandSenderProvider extends CommandProvider<CommandSender> {
    @Override
    public boolean doesConsumeArgument() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean allowNullArgument() {
        return true;
    }

    @Nonnull
    @Override
    public CommandSender provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        return (CommandSender) arg.getSender().getInstance();
    }

    @Override
    public String argumentDescription() {
        return "sender";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return Collections.emptyList();
    }
}
