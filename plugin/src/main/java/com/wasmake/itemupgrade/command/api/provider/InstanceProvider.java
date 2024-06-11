package com.wasmake.itemupgrade.command.api.provider;

import com.wasmake.itemupgrade.command.api.argument.CommandArg;
import com.wasmake.itemupgrade.command.api.parametric.CommandProvider;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class InstanceProvider<T> extends CommandProvider<T> {

    private final T instance;

    public InstanceProvider(T instance) {
        this.instance = instance;
    }

    @Override
    public boolean doesConsumeArgument() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public T provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) {
        return instance;
    }

    @Override
    public String argumentDescription() {
        return instance.getClass().getSimpleName() + " (provided)";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return Collections.emptyList();
    }
}
