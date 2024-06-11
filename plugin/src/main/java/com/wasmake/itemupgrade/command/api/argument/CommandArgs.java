package com.wasmake.itemupgrade.command.api.argument;

import com.google.common.base.Preconditions;
import com.wasmake.itemupgrade.command.api.command.AbstractCommandService;
import com.wasmake.itemupgrade.command.api.flag.CommandFlag;
import com.wasmake.itemupgrade.command.api.sender.ICommandSender;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public final class CommandArgs {
    private final AbstractCommandService<?> commandService;
    private final ICommandSender<?> sender;
    private final List<String> args;
    private final String label;
    private final Map<Character, CommandFlag> flags;
    private final ReentrantLock lock = new ReentrantLock();
    private int index = 0;

    public CommandArgs(@Nonnull AbstractCommandService<?> commandService, @Nonnull ICommandSender<?> sender, @Nonnull String label, @Nonnull List<String> args,
                       @Nonnull Map<Character, CommandFlag> flags) {
        Preconditions.checkNotNull(commandService, "CommandService cannot be null");
        Preconditions.checkNotNull(sender, "CommandSender cannot be null");
        Preconditions.checkNotNull(label, "Label cannot be null");
        Preconditions.checkNotNull(args, "Command args cannot be null");
        this.commandService = commandService;
        this.sender = sender;
        this.label = label;
        this.args = new ArrayList<>(args);
        this.flags = flags;
    }

    public List<String> getArgs() {
        return args;
    }

    public Map<Character, CommandFlag> getFlags() {
        return flags;
    }

    public ICommandSender<?> getSender() {
        return sender;
    }

    public String getLabel() {
        return label;
    }

    public boolean hasNext() {
        lock.lock();
        try {
            return args.size() > index;
        } finally {
            lock.unlock();
        }
    }

    public String next() {
        lock.lock();
        try {
            return args.get(index++);
        } finally {
            lock.unlock();
        }
    }
}
