package com.wasmake.itemupgrade.command.spigot.authorizer;

import com.wasmake.itemupgrade.command.api.authorizer.IAuthorizer;
import com.wasmake.itemupgrade.command.api.command.WrappedCommand;
import com.wasmake.itemupgrade.command.api.sender.ICommandSender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public final class SpigotAuthorizer implements IAuthorizer<CommandSender> {
    @Override
    public boolean isAuthorized(@Nonnull ICommandSender<CommandSender> sender, @Nonnull WrappedCommand command) {
        if (command.getPermission() != null && command.getPermission().length() > 0) {
            if (!sender.hasPermission(command.getPermission())) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to perform this command.");
                return false;
            }
        }
        return true;
    }
}
