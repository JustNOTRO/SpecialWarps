package me.notro.specialwarps.commands;

import lombok.RequiredArgsConstructor;
import me.notro.specialwarps.SpecialWarps;
import me.notro.specialwarps.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class WarpsCommand implements CommandExecutor {

    private final SpecialWarps plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtils.sendComponentMessage(sender, MessageUtils.NO_SENDER_EXECUTOR);
            return false;
        }

        if (!player.hasPermission("specialwarps.warps")) {
            MessageUtils.sendComponentMessage(player, MessageUtils.NO_PERMISSION);
            return false;
        }

        if (args.length > 0) {
            MessageUtils.sendPrefixedMessage(player,"&c/" + label);
            return false;
        }

        plugin.getGuiManager()
                .createMenu(player, 36,
                        Component.text("Warps")
                                .color(NamedTextColor.RED));
        plugin.getGuiManager()
                .setItem(35, new ItemStack(Material.END_CRYSTAL),
                        Component.text("Random Warp")
                                .color(NamedTextColor.AQUA));
        plugin.getGuiManager()
                .setItem(34, new ItemStack(Material.SPONGE),
                        Component.text("Warps: ")
                                .color(NamedTextColor.YELLOW)
                                .append(Component.text(plugin.getWarpManager().getWarps()).color(NamedTextColor.GRAY)));
        plugin.getGuiManager()
                .setItem(31, new ItemStack(Material.ANVIL),
                        Component.text("Create Warp")
                                .color(NamedTextColor.YELLOW));

        plugin.getWarpManager().openWarpsMenu(player);

        if (!plugin.getGuiManager().getMenu().contains(Material.NAME_TAG)) {
            MessageUtils.sendPrefixedMessage(player,"&cThere is no warps currently&7.");
            return false;
        }

        return true;
    }
}
