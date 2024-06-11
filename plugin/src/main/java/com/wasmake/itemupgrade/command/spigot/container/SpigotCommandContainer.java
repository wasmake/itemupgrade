package com.wasmake.itemupgrade.command.spigot.container;
;
import com.wasmake.itemupgrade.command.api.command.AbstractCommandService;
import com.wasmake.itemupgrade.command.api.command.CommandContainer;
import com.wasmake.itemupgrade.command.api.command.WrappedCommand;
import com.wasmake.itemupgrade.command.spigot.SpigotCommandService;
import com.wasmake.itemupgrade.command.spigot.executor.SpigotCommandExecutor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SpigotCommandContainer extends CommandContainer {
    public SpigotCommandContainer(AbstractCommandService<?> commandService, Object object, String name, Set<String> aliases, Map<String, WrappedCommand> commands) {
        super(commandService, object, name, aliases, commands);
    }

    public final class SpigotCommand extends Command implements PluginIdentifiableCommand {
        private final SpigotCommandService commandService;
        private final CommandExecutor commandExecutor;

        public SpigotCommand(@Nonnull SpigotCommandService commandService) {
            super(name, "", "/" + name, new ArrayList<>(aliases));
            this.commandService = commandService;
            commandExecutor = new SpigotCommandExecutor(commandService, SpigotCommandContainer.this);
            if (defaultCommand != null) {
                setUsage("/" + name + " " + defaultCommand.getGeneratedUsage());
                setDescription(defaultCommand.getDescription());
                setPermission(defaultCommand.getPermission());
            }
        }

        @Override
        public boolean execute(CommandSender commandSender, String s, String[] strings) {
            return commandExecutor.onCommand(commandSender, this, s, strings);
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
            return tabCompleter.onTabComplete(getName(), args);
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
            return tabCompleter.onTabComplete(getName(), args);
        }

        @Override
        public Plugin getPlugin() {
            return commandService.getPlugin();
        }
    }
}
