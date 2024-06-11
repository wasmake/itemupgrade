package com.wasmake.itemupgrade.cmd;

import com.wasmake.itemupgrade.ItemUpgrade;
import com.wasmake.itemupgrade.command.AbstractCommand;
import com.wasmake.itemupgrade.command.api.annotation.Command;
import com.wasmake.itemupgrade.command.api.annotation.OptArg;
import com.wasmake.itemupgrade.command.api.annotation.Sender;
import com.wasmake.itemupgrade.items.ItemUpgradeConfig;
import com.wasmake.itemupgrade.items.ItemsConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemUpgradeCmd extends AbstractCommand {

    private ItemsConfig itemsConfig;

    public ItemUpgradeCmd() {
        super("itemupgrade", "iu");

        this.itemsConfig = ItemUpgrade.getInstance().getItemsConfig();
    }

    @Command(name = "setfrag", desc = "Set frag item")
    public void executeSetfrag(@Sender Player sender) {
        // Check if is OP
        if (!sender.isOp()) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return;
        }

        // Set the frag item
        ItemStack itemStack = sender.getInventory().getItemInMainHand();
        this.itemsConfig.setFragItem(itemStack);
        this.itemsConfig.save();

        sender.sendMessage("§aFrag item set.");
    }

    @Command(name = "set", desc = "Set an item to the upgrade menu")
    public void execute(@Sender CommandSender sender, int index, int level, int cost) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Check if is OP
            if (!player.isOp()) {
                player.sendMessage("§cYou don't have permission to use this command.");
                return;
            }

            // Get item in hand
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            addItem(itemStack, index, level, cost);

            player.sendMessage("§aItem added to the upgrade menu.");
        }
    }

    @Command(name = "remove", desc = "Removes an item from the upgrade menu")
    public void execute(@Sender CommandSender sender, int level) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Check if is OP
            if (!player.isOp()) {
                player.sendMessage("§cYou don't have permission to use this command.");
                return;
            }

            removeItem(level);

            player.sendMessage("§aItem removed from the upgrade menu.");
        }
    }

    @Command(name = "give", desc = "Give an item to the player")
    public void executeGive(@Sender Player player, int index, int level, @OptArg Player argPlayer) {
        if (argPlayer != null) {
            player = argPlayer;
        }
        giveItem(player, index, level);
    }

    @Command(name = "show", desc = "Display a menu with all the items to the player")
    public void executeShow(@Sender Player player) {
        // Show the items menu
        Map<Integer, List<ItemUpgradeConfig>> items = this.itemsConfig.getItems();

        // Create a Bukkit inventory
        String title = "Item Upgrade Menu";
        Inventory inventory = Bukkit.createInventory(player, 54, title);

        for (int index : items.keySet()) {
            List<ItemUpgradeConfig> itemUpgradeConfigs = items.get(index);
            for (ItemUpgradeConfig itemUpgradeConfig : itemUpgradeConfigs) {
                inventory.addItem(itemUpgradeConfig.getItem());
            }
        }

        player.openInventory(inventory);
        player.sendMessage("§aItem upgrade menu opened.");
    }

    @Command(name = "upgrade", desc = "Upgrade the item in your hand")
    public void executeUpgrade(@Sender Player player) {
        upgradeItem(player);
    }

    public void giveItem(Player player, int index, int level) {
        Map<Integer, List<ItemUpgradeConfig>> items = this.itemsConfig.getItems();

        if (items.containsKey(index)) {
            List<ItemUpgradeConfig> itemUpgradeConfigs = items.get(index);
            for (ItemUpgradeConfig itemUpgradeConfig : itemUpgradeConfigs) {
                if (itemUpgradeConfig.getLevel() == level) {
                    player.getInventory().addItem(itemUpgradeConfig.getItem());
                    player.sendMessage("§aItem delivered to " + player.getName());
                    return;
                }
            }
        }

        player.sendMessage("§cCan't get the item.");
    }
    public void upgradeItem(Player player) {
        // Upgrade item
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemUpgradeConfig itemUpgradeConfig = getNextUpgrade(itemStack);

        if (itemUpgradeConfig == null) {
            player.sendMessage("§cCan't get an upgrade for this item.");
            return;
        }

        // Check if the player has the required frags to upgrade the item in the inventory
        ItemStack fragItem = this.itemsConfig.getFragItem();
        if (fragItem != null) {
            int cost = itemUpgradeConfig.getCost();
            int amount = 0;
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.isSimilar(fragItem)) {
                    amount += item.getAmount();
                }
            }

            if (amount < cost) {
                player.sendMessage("§cYou don't have enough frags to upgrade this item.");
                return;
            }

            // Remove the required frags from the player's inventory
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.isSimilar(fragItem)) {
                    if (item.getAmount() > cost) {
                        item.setAmount(item.getAmount() - cost);
                        break;
                    } else {
                        cost -= item.getAmount();
                        item.setAmount(0);
                    }
                }
            }
        }

        // Upgrade the item
        player.getInventory().setItemInMainHand(itemUpgradeConfig.getItem());
        player.sendMessage("§aItem upgraded.");

        // Save the player's inventory
        player.updateInventory();
    }

    public ItemUpgradeConfig getNextUpgrade(ItemStack item){
        Map<Integer, List<ItemUpgradeConfig>> items = this.itemsConfig.getItems();

        for (int index : items.keySet()) {
            List<ItemUpgradeConfig> itemUpgradeConfigs = items.get(index);
            for (ItemUpgradeConfig itemUpgradeConfig : itemUpgradeConfigs) {
                if (itemUpgradeConfig.getItem().isSimilar(item)) {
                    int level = itemUpgradeConfig.getLevel();
                    if (level < itemUpgradeConfigs.size()) {
                        return getLevel(index, level + 1);
                    }
                }
            }
        }
        return null;
    }

    public ItemUpgradeConfig getLevel(int index, int level) {
        Map<Integer, List<ItemUpgradeConfig>> items = this.itemsConfig.getItems();

        if (items.containsKey(index)) {
            List<ItemUpgradeConfig> itemUpgradeConfigs = items.get(index);
            for (ItemUpgradeConfig itemUpgradeConfig : itemUpgradeConfigs) {
                if (itemUpgradeConfig.getLevel() == level) {
                    return itemUpgradeConfig;
                }
            }
        }

        return null;
    }

    public void addItem(ItemStack itemStack, int index, int level, int cost) {
        Map<Integer, List<ItemUpgradeConfig>> items = this.itemsConfig.getItems();

        List<ItemUpgradeConfig> savedItems = new ArrayList<>();
        if (items.containsKey(index)) {
            savedItems.addAll(items.get(index));
        }
        savedItems.add(new ItemUpgradeConfig(level, cost, itemStack));

        items.put(index, savedItems);

        this.itemsConfig.save();
    }

    public void removeItem(int level) {
        // Remove item by level
        Map<Integer, List<ItemUpgradeConfig>> items = this.itemsConfig.getItems();

        for (List<ItemUpgradeConfig> itemUpgradeConfigs : items.values()) {
            for (ItemUpgradeConfig itemUpgradeConfig : itemUpgradeConfigs) {
                if (itemUpgradeConfig.getLevel() == level) {
                    itemUpgradeConfigs.remove(itemUpgradeConfig);
                    break;
                }
            }
        }

        // Order again the levels
        for (List<ItemUpgradeConfig> itemUpgradeConfigs : items.values()) {
            for (int i = 0; i < itemUpgradeConfigs.size(); i++) {
                itemUpgradeConfigs.get(i).setLevel(i + 1);
            }
        }

        this.itemsConfig.save();
    }
}
