package me.notro.specialwarps.commands;

import lombok.RequiredArgsConstructor;
import me.notro.specialwarps.SpecialWarps;
import me.notro.specialwarps.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class SetWarpCommand implements CommandExecutor {

    private final SpecialWarps plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.getPrefix().append(MessageUtils.NO_SENDER_EXECUTOR));
            return false;
        }

        if (!player.hasPermission("specialwarps.setwarp")) {
            player.sendMessage(MessageUtils.getPrefix().append(MessageUtils.NO_PERMISSION));
            return false;
        }

        if (args.length < 1) {
            player.sendMessage(MessageUtils.getPrefix().append(MessageUtils.fixColor("&c/" + label + " <name>")));
            return false;
        }

        plugin.getWarpManager().createWarp(player, args[0], player.getLocation());
        return true;
    }
}
