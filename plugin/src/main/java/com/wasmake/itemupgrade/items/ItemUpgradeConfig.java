package com.wasmake.itemupgrade.items;

import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public record ItemUpgradeConfig(
    int level,
    int cost,
    ItemStack item
) { }
