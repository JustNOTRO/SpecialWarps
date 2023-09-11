package me.notro.specialwarps.commands;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.notro.specialwarps.SpecialWarps;
import me.notro.specialwarps.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SetWarpCommand implements CommandExecutor {

    private final SpecialWarps plugin;

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtils.sendComponentMessage(sender, ChatUtils.NO_SENDER_EXECUTOR);
            return false;
        }

        if (!player.hasPermission("specialwarps.setwarp")) {
            ChatUtils.sendComponentMessage(player, ChatUtils.NO_PERMISSION);
            return false;
        }

        if (args.length < 1) {
            ChatUtils.sendPrefixedMessage(sender, "&c/" + label + " <name>");
            return false;
        }

        plugin.getWarpManager().createWarp(player, args[0], player.getLocation());
        return true;
    }
}
