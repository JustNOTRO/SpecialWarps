package me.notro.specialwarps.managers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.notro.specialwarps.SpecialWarps;
import me.notro.specialwarps.models.Warp;
import me.notro.specialwarps.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
public class WarpManager {

    private final SpecialWarps plugin;
    private final List<Warp> warps = new ArrayList<>();
    private final List<UUID> warpCreators = new ArrayList<>();

    public void createWarp(@NonNull Player owner, @NonNull String warpName, @NonNull Location warpLocation) {
        Warp warp = new Warp(owner.getName(), warpName, warpLocation);

        if (isExist(warp)) {
            MessageUtils.sendPrefixedMessage(owner, "&cWarp already exist&7.");
            return;
        }

        warps.add(warp);
        plugin.getWarpsFile().getConfig().set("special-warps." + warp.getWarpName() + ".owner", warp.getOwnerName());
        plugin.getWarpsFile().getConfig().set("special-warps." + warp.getWarpName() + ".location", warp.getWarpLocation());
        plugin.getWarpsFile().saveConfig();

        MessageUtils.sendPrefixedMessage(owner, "&aSuccessfully added a new warp named &b" + warp.getWarpName() + "&7.");
    }

    public void removeWarp(@NonNull Player player, @NonNull Warp warp) {
        if (!isExist(warp)) {
            MessageUtils.sendPrefixedMessage(player, "&cWarp does not exist&7.");
            return;
        }

        warps.remove(warp);
        plugin.getWarpsFile().getConfig().set("special-warps." + warp.getWarpName(), null);
        plugin.getWarpsFile().saveConfig();

        MessageUtils.sendPrefixedMessage(player,"&aSuccessfully removed warp named &b" + warp.getWarpName() + "&7.");
    }

    public void chooseRandomWarp(@NonNull Player player) {
        final Random random = new Random();
        final int numberOfWarps = getWarps();

        if (numberOfWarps == 1) {
            MessageUtils.sendPrefixedMessage(player, "&cThere is not enough warps to teleport randomly&7.");
            return;
        }

        final int randomWarp = random.nextInt(numberOfWarps);

        for (int i = 0; i < numberOfWarps; i++) {
            if (randomWarp == i) {
                player.teleport(warps.get(i).getWarpLocation());
                MessageUtils.sendPrefixedMessage(player, "&aSuccessfully teleported randomly to &b" + warps.get(i).getWarpName() + "&7.");
                return;
            }
        }
    }

    public int getWarps() {
        int numberOfWarps = 0;

        for (String key : plugin.getWarpsFile().getConfig().getConfigurationSection("special-warps").getKeys(false)) {
            ConfigurationSection warpSection = plugin.getWarpsFile().getConfig().getConfigurationSection("special-warps." + key);
            if (warpSection == null) continue;

            warps.add(new Warp(warpSection.getString("owner"), key, warpSection.getLocation("location")));
            numberOfWarps++;
        }

        return numberOfWarps;
    }

    public void openWarpsMenu(@NonNull Player player) {
        for (String key : plugin.getWarpsFile().getConfig().getConfigurationSection("special-warps").getKeys(false)) {
            ConfigurationSection warpSection = plugin.getWarpsFile().getConfig().getConfigurationSection("special-warps." + key);
            if (warpSection == null) continue;

            ItemStack stack = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = stack.getItemMeta();
            meta.displayName(MessageUtils.fixColor(key));

            List<Component> loreList = new ArrayList<>();
            loreList.add(MessageUtils.fixColor("&7Owner: &e" + warpSection.getString("owner")));
            loreList.add(MessageUtils.fixColor("&cRight-Click &7to edit"));
            loreList.add(MessageUtils.fixColor("&cLeft-Click &7to teleport"));

            meta.lore(loreList);
            stack.setItemMeta(meta);
            plugin.getGuiManager().addItem(stack);
            plugin.getGuiManager().openMenu(player);
        }
    }

    public void addPlayer(@NonNull UUID uuid) {
        warpCreators.add(uuid);
    }

    public void removePlayer(@NonNull UUID uuid) {
        warpCreators.remove(uuid);
    }

    public boolean containsPlayer(@NonNull UUID uuid) {
        return warpCreators.contains(uuid);
    }

    private boolean isExist(@NonNull Warp warp) {
        return plugin.getWarpsFile().getConfig().isConfigurationSection("special-warps." + warp.getWarpName());
    }
}