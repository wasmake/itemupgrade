package com.wasmake.itemupgrade.command.api.provider;

import com.wasmake.itemupgrade.command.api.argument.CommandArg;
import com.wasmake.itemupgrade.command.api.exception.CommandExitMessage;
import com.wasmake.itemupgrade.command.api.lang.Lang;
import com.wasmake.itemupgrade.command.api.parametric.CommandProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public class DoubleProvider extends CommandProvider<Double> {
    private final Lang lang;

    public DoubleProvider(@Nonnull Lang lang) {
        this.lang = lang;
    }

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean allowNullArgument() {
        return false;
    }

    @Nullable
    @Override
    public Double defaultNullValue() {
        return 0D;
    }

    @Override
    public Double provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String s = arg.get();
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            throw new CommandExitMessage(lang.get(Lang.Type.INVALID_DOUBLE, s));
        }
    }

    @Override
    public String argumentDescription() {
        return "decimal number";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return Collections.emptyList();
    }
}
