package com.wasmake.itemupgrade.items;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public record ItemsConfig(
    ItemStack fragItem,
    Map<Integer, List<ItemUpgradeConfig>> items
) { }
