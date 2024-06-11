package com.wasmake.itemupgrade.listener;

import com.wasmake.itemupgrade.ItemUpgrade;
import com.wasmake.itemupgrade.items.ItemUpgradeConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class ItemListener implements Listener {
    @EventHandler
    public void onAnvilRename(PrepareAnvilEvent event) {
        ItemStack item = event.getInventory().getItem(0);
        if (item != null) {
            Map<Integer, List<ItemUpgradeConfig>> items = ItemUpgrade.getInstance().getItemsConfig().items();
            for (List<ItemUpgradeConfig> itemUpgradeConfigs : items.values()) {
                for (ItemUpgradeConfig itemUpgradeConfig : itemUpgradeConfigs) {
                    if (item.isSimilar(itemUpgradeConfig.item())) {
                        event.setResult(null);
                        return;
                    }
                }
            }
        }
    }
}
