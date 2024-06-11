package com.wasmake.itemupgrade.items;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class ItemsConfig {
    public ItemStack fragItem;
    public Map<Integer, List<ItemUpgradeConfig>> items;

    public ItemsConfig() {
    }

    public ItemStack fragItem() {
        return fragItem;
    }

    public Map<Integer, List<ItemUpgradeConfig>> items() {
        return items == null ? Map.of() : items;
    }

}
