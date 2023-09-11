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
public class WarpsCommand implements CommandExecutor {

    private final SpecialWarps plugin;

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtils.sendComponentMessage(sender, ChatUtils.NO_SENDER_EXECUTOR);
            return false;
        }

        if (!player.hasPermission("specialwarps.warps")) {
            ChatUtils.sendComponentMessage(player, ChatUtils.NO_PERMISSION);
            return false;
        }

        if (args.length > 0) {
            ChatUtils.sendPrefixedMessage(player,"&c/" + label);
            return false;
        }

        plugin.getWarpManager().openWarpsMenu(player);
        return true;
    }
}
