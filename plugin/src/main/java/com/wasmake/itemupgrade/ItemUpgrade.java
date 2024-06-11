package com.wasmake.itemupgrade;

import com.wasmake.itemupgrade.cmd.ItemUpgradeCmd;
import com.wasmake.itemupgrade.command.CommandManager;
import com.wasmake.itemupgrade.config.ConfigurationLoader;
import com.wasmake.itemupgrade.items.ItemsConfig;
import com.wasmake.itemupgrade.listener.ItemListener;
import com.wasmake.itemupgrade.timedevent.TimedEventManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemUpgrade extends JavaPlugin {

    private static ItemUpgrade instance;
    private CommandManager commandManager;

    private ItemsConfig itemsConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ConfigurationLoader configurationLoader = new ConfigurationLoader();
        this.itemsConfig = configurationLoader.loadOrCreate(ItemsConfig.class, getDataFolder().getPath());

        this.commandManager = new CommandManager(this);

        this.instance = this;

        this.commandManager.addClassCommand(new ItemUpgradeCmd());
        this.commandManager.registerCommands();

        new TimedEventManager();

        // Register event listener
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ItemUpgrade getInstance() {
        return instance;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ItemsConfig getItemsConfig() {
        return itemsConfig;
    }
}
