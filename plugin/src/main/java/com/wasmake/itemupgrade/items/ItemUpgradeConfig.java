package com.wasmake.itemupgrade.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class ItemUpgradeConfig {
    private int level;
    private int cost;
    private ItemStack item;

    public ItemUpgradeConfig(int level, int cost, ItemStack item) {
        this.level = level;
        this.cost = cost;
        this.item = item;
    }

    public ItemUpgradeConfig() {
    }
}
