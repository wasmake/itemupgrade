package com.wasmake.itemupgrade.serializer;

import java.lang.reflect.Type;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

public class ItemStackSerializer implements TypeSerializer<ItemStack> {

    @Override
    public ItemStack deserialize(final Type type, final ConfigurationNode node) throws SerializationException {

        final var material = Material.matchMaterial(node.node("type")
            .getString(Material.BEDROCK.name()));

        if (material == null) {
            return new ItemStack(Material.BEDROCK);
        }

        final var item = new ItemStack(material, node.node("amount").getInt(1));

        final var itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return item;
        }

        final var displayName = node.node("display-name").get(Component.class);
        if (displayName != null) {
            itemMeta.displayName(displayName);
        }

        final var lore = node.node("lore").getList(Component.class);
        if (lore != null) {
            itemMeta.lore(lore);
        }

        final var nbtTags = node.node("nbt-tags").getList(String.class);
        
        if (nbtTags != null) {
            final var persistentDataContainer = itemMeta.getPersistentDataContainer();
            for (final var nbtTag : nbtTags) {
                final var split = nbtTag.split(":");
                persistentDataContainer.set(
                    new NamespacedKey(split[0], split[1]),
                    PersistentDataType.BYTE_ARRAY,
                    new byte[] {}
                );

            }
        }

        final var enchants = node.node("enchantments").getList(String.class);

        if (enchants != null) {
            for (final var enchantStr : enchants) {
                final var split = enchantStr.split(":");
                @SuppressWarnings("deprecation")
                final var enchantment = Enchantment.getByName(split[0]);
                if (enchantment != null) {
                    try {
                        itemMeta.addEnchant(enchantment, Integer.parseInt(split[1]), true);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        final var flags = node.node("flags").getList(String.class);

        if (flags != null) {
            for (final var flag : flags) {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(flag));
                } catch (IllegalArgumentException ignored) {}
            }
        }

        return item;
    }

    @Override
    public void serialize(Type type, @Nullable ItemStack obj, ConfigurationNode node) throws SerializationException {

        if (obj == null) {
            node.raw(null);
            return;
        }

        final var itemMeta = obj.getItemMeta();

        node.node("type").set(obj.getType().name());
        node.node("amount").set(obj.getAmount());

        if (itemMeta != null) {
            if (itemMeta.hasDisplayName()) {
                node.node("display-name").set(itemMeta.displayName());
            }

            if (itemMeta.hasLore()) {
                node.node("lore").setList(Component.class, itemMeta.lore());
            }

            final var persistentDataContainer = itemMeta.getPersistentDataContainer();
            final var tags = persistentDataContainer.getKeys().stream().map(Object::toString).toList();

            if (!tags.isEmpty()) {
                node.node("nbt-tags").setList(String.class, tags);
            }

            @SuppressWarnings("deprecation")
            final var enchants = itemMeta.getEnchants().entrySet().stream()
                .map(entry -> entry.getKey().getName() + ":" + entry.getValue())
                .toList();

            if (!enchants.isEmpty()) {
                node.node("enchantments").setList(String.class, enchants);
            }

            final var flags = itemMeta.getItemFlags().stream().map(ItemFlag::name).toList();

            if (!flags.isEmpty()) {
                node.node("flags").setList(String.class, flags);
            }

        }

    }
}
