package com.wasmake.itemupgrade.command;

import com.wasmake.itemupgrade.ItemUpgrade;

public class AbstractCommand {
    private String name;
    private String[] aliases;

    private final ItemUpgrade tooLevel = ItemUpgrade.getInstance();

    public AbstractCommand(String name, String... aliases){
        this.name = name;
        this.aliases = aliases;
        tooLevel.getCommandManager().getCommands().add(this);
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

}