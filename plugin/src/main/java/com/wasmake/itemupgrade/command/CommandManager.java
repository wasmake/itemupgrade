package com.wasmake.itemupgrade.command;

import com.wasmake.itemupgrade.command.spigot.SpigotCommandService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private List<AbstractCommand> commands;
    private SpigotCommandService commandService;

    public CommandManager(JavaPlugin plugin){
        commandService = new SpigotCommandService(plugin);
        commands = new ArrayList<>();
    }

    public List<AbstractCommand> getCommands() {
        return commands;
    }

    public void addClassCommand(AbstractCommand command){
        commands.add(command);
    }

    public void registerCommands(){
        commands.forEach(drinkCommand -> {
            commandService.register(drinkCommand, drinkCommand.getName(), drinkCommand.getAliases()).setOverrideExistingCommands(false);
        });
        commandService.registerCommands();
    }

}