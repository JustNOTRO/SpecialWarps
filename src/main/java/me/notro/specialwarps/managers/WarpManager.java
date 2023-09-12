package me.notro.specialwarps.managers;

import lombok.NonNull;
import me.notro.specialwarps.SpecialWarps;
import me.notro.specialwarps.models.Warp;
import me.notro.specialwarps.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class WarpManager {

    private final SpecialWarps plugin;
    private final List<Warp> warps = new ArrayList<>();
    private final List<UUID> warpCreations = new ArrayList<>();

    public WarpManager(@NonNull SpecialWarps plugin) {
        this.plugin = plugin;

        loadWarps();
    }

    public void createWarp(@NonNull Player owner, @NonNull String warpName, @NonNull Location warpLocation) {
        Warp warp = new Warp(owner.getName(), warpName, warpLocation);

        if (isExist(warp)) {
            ChatUtils.sendPrefixedMessage(owner, "&cWarp already exist&7.");
            return;
        }

        warps.add(warp);
        plugin.getWarpsFile().getConfig().set("special-warps." + warp.getWarpName() + ".owner", warp.getOwnerName());
        plugin.getWarpsFile().getConfig().set("special-warps." + warp.getWarpName() + ".location", warp.getWarpLocation());
        plugin.getWarpsFile().saveConfig();

        ChatUtils.sendPrefixedMessage(owner, "&aSuccessfully added a new warp named &b" + warp.getWarpName() + "&7.");
    }

    public void removeWarp(@NonNull Player player, @NonNull Warp warp) {
        if (!isExist(warp)) {
            ChatUtils.sendPrefixedMessage(player, "&cWarp does not exist&7.");
            return;
        }

        warps.remove(warp);
        plugin.getWarpsFile().getConfig().set("special-warps." + warp.getWarpName(), null);
        plugin.getWarpsFile().saveConfig();

        ChatUtils.sendPrefixedMessage(player,"&aSuccessfully removed warp named &b" + warp.getWarpName() + "&7.");
    }

    public void chooseRandomWarp(@NonNull Player player) {
        final Random random = new Random();
        final int numberOfWarps = getWarpsCount();

        if (numberOfWarps == 1) {
            ChatUtils.sendPrefixedMessage(player, "&cThere is not enough warps to teleport randomly&7.");
            return;
        }

        final int randomWarpIndex = random.nextInt(numberOfWarps);
        final Warp randomWarp = warps.get(randomWarpIndex);

        player.teleport(randomWarp.getWarpLocation());
        ChatUtils.sendPrefixedMessage(player, "&aSuccessfully teleported randomly to &b" + randomWarp.getWarpName() + "&7.");
    }

    public int getWarpsCount() {
        return warps.size();
    }

    private void loadWarps() {
        if (!plugin.getWarpsFile().getConfig().isConfigurationSection("special-warps")) plugin.getWarpsFile().getConfig().createSection("special-warps");

        for (String key : plugin.getWarpsFile().getConfig().getConfigurationSection("special-warps").getKeys(false)) {
            ConfigurationSection warpSection = plugin.getWarpsFile().getConfig().getConfigurationSection("special-warps." + key);
            if (warpSection == null) continue;

            warps.add(new Warp(warpSection.getString("owner"), key, warpSection.getLocation("location")));
        }
    }

    public void openWarpsMenu(@NonNull Player player) {
        @NonNull Inventory warpMenu = plugin.getGuiManager().createMenu(player, 36, Component.text("Warps").color(NamedTextColor.RED));

        for (String key : plugin.getWarpsFile().getConfig().getConfigurationSection("special-warps").getKeys(false)) {
            ConfigurationSection warpSection = plugin.getWarpsFile().getConfig().getConfigurationSection("special-warps." + key);
            if (warpSection == null) continue;

            Component[] loreElements =
                    {
                            Component.text("Owner: ")
                                    .color(NamedTextColor.GRAY)
                                    .append(
                                            Component.text(warpSection.getString("owner"))
                                                    .color(NamedTextColor.YELLOW)),

                            Component.text("Right-Click ")
                                    .color(NamedTextColor.RED)
                                    .append(
                                            Component.text("to edit")
                                                    .color(NamedTextColor.GRAY)),

                            Component.text("Left-Click ")
                                    .color(NamedTextColor.RED)
                                    .append(
                                            Component.text("to teleport")
                                                    .color(NamedTextColor.GRAY))
                    };

            plugin.getGuiManager()
                    .addItem(warpMenu, new ItemStack(Material.NAME_TAG),
                            Component.text(key), loreElements);
        }

        if (!warpMenu.contains(Material.NAME_TAG)) {
            ChatUtils.sendPrefixedMessage(player,"&cThere is no warps currently&7.");
            return;
        }

        plugin.getGuiManager()
                .setItem(warpMenu, 35, new ItemStack(Material.END_CRYSTAL),
                        Component.text("Random Warp")
                                .color(NamedTextColor.AQUA));

        plugin.getGuiManager()
                .setItem(warpMenu, 34, new ItemStack(Material.SPONGE),
                        Component.text("Warps: ")
                                .color(NamedTextColor.YELLOW)
                                .append(
                                        Component.text(getWarpsCount())
                                                .color(NamedTextColor.GRAY)));

        plugin.getGuiManager()
                .setItem(warpMenu, 31, new ItemStack(Material.ANVIL),
                        Component.text("Create Warp")
                                .color(NamedTextColor.YELLOW));

        plugin.getGuiManager()
                .setItem(warpMenu, 30, new ItemStack(Material.TRIPWIRE_HOOK),
                        Component.text("Reload Warps")
                                .color(NamedTextColor.RED));

        player.openInventory(warpMenu);
    }

    public void addPlayer(@NonNull UUID uuid) {
        warpCreations.add(uuid);
    }

    public void removePlayer(@NonNull UUID uuid) {
        warpCreations.remove(uuid);
    }

    public boolean containsPlayer(@NonNull UUID uuid) {
        return warpCreations.contains(uuid);
    }

    private boolean isExist(@NonNull Warp warp) {
        return plugin.getWarpsFile().getConfig().isConfigurationSection("special-warps." + warp.getWarpName());
    }
}