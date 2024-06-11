package com.wasmake.itemupgrade.serializer;

import java.lang.reflect.Type;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

public class ComponentSerializer implements TypeSerializer<Component> {

    private final static MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public Component deserialize(final Type type, final ConfigurationNode node) {
        final var string = node.getString();
        return string == null ? null : MINI_MESSAGE.deserialize(string);
    }

    @Override
    public void serialize(Type type, @Nullable Component obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }
        node.set(MINI_MESSAGE.serialize(obj));
    }
}
