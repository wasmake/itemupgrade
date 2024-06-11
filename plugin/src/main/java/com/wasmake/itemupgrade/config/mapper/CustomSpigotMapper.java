package com.wasmake.itemupgrade.config.mapper;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wasmake.itemupgrade.utils.ItemStackSerializer;
import org.bukkit.inventory.ItemStack;

public class CustomSpigotMapper extends SimpleModule {
    public CustomSpigotMapper() {
        addSerializer(ItemStack.class, new ItemStackSerializer());
        addDeserializer(ItemStack.class, new ItemStackSerializer.ItemStackDeserializer());
    }
}
