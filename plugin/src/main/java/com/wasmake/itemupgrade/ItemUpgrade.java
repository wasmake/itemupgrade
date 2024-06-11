package com.wasmake.itemupgrade;

import com.wasmake.itemupgrade.cmd.ItemUpgradeCmd;
import com.wasmake.itemupgrade.command.CommandManager;
import com.wasmake.itemupgrade.serializer.ComponentSerializer;
import com.wasmake.itemupgrade.serializer.ItemStackSerializer;
import com.wasmake.itemupgrade.items.ItemsConfig;
import com.wasmake.itemupgrade.listener.ItemListener;
import com.wasmake.itemupgrade.timedevent.TimedEventManager;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class ItemUpgrade extends JavaPlugin {

    private static ItemUpgrade instance;
    private CommandManager commandManager;
    private YamlConfigurationLoader configurationLoader;

    private CommentedConfigurationNode config;
    private ItemsConfig itemsConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        final var file = new File(getDataFolder(), "items.yml");

        try {
            this.configurationLoader = YamlConfigurationLoader.builder()
                .file(file)
                .defaultOptions(
                    ConfigurationOptions
                        .defaults()
                        .serializers(
                            TypeSerializerCollection
                                .builder()
                                .registerAll(TypeSerializerCollection.defaults())
                                .register(Component.class, new ComponentSerializer())
                                .register(ItemStack.class, new ItemStackSerializer())
                                .build()
                        )
                )
                .build();

            this.config = this.configurationLoader.load();
            this.itemsConfig = this.config.get(ItemsConfig.class);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }

        this.commandManager = new CommandManager(this);

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

    public void updateConfig(final @NotNull Consumer<CommentedConfigurationNode> consumer) {
        consumer.accept(this.config);
        try {
            this.configurationLoader.save(this.config);
            this.itemsConfig = this.config.get(ItemsConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
