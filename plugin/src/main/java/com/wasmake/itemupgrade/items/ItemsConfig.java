package com.wasmake.itemupgrade.items;

import com.wasmake.itemupgrade.config.AbstractConfiguration;
import com.wasmake.itemupgrade.config.annotations.Config;
import com.wasmake.itemupgrade.config.annotations.comment.ConfigComment;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Config(fileName = "items")
public class ItemsConfig extends AbstractConfiguration {
    @ConfigComment({"Frag item to use", "    Use /itemupgrade setfrag with item in hand to set this"})
    private ItemStack fragItem;

    @ConfigComment({"Array of the items with their progressions", "    Use /itemupgrade add for this"})
    private Map<Integer, List<ItemUpgradeConfig>> items = new HashMap<>();
}
