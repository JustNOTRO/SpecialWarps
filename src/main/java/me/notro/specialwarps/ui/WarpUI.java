package me.notro.specialwarps.ui;

import lombok.RequiredArgsConstructor;
import me.notro.specialwarps.SpecialWarps;
import me.notro.specialwarps.models.Warp;
import me.notro.specialwarps.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public class WarpUI implements Listener {

    private final SpecialWarps plugin;
    private String slotName;

    @EventHandler
    public void onPlayerOpenWarps(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());
        TextComponent titleName = Component.text("Warps").color(NamedTextColor.RED);

        if (!event.getView().title().equals(titleName)) return;
        if (slot == null || slot.getType() != Material.NAME_TAG) return;

        slotName = LegacyComponentSerializer.legacyAmpersand().serialize(slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName());
        ConfigurationSection section = plugin.getWarpsFile().getConfig().getConfigurationSection("special-warps." + slotName);

        switch (event.getAction()) {
            case PICKUP_ALL -> {
                player.teleport(section.getLocation("location"));
                ChatUtils.sendPrefixedMessage(player, "&aSuccessfully teleported to &b" + slotName + "&7.");
            }

            case PICKUP_HALF -> {
                Inventory editMenu = plugin.getGuiManager().createMenu(player, 36, ChatUtils.fixColor("&7Edit: &b" + slotName));

                plugin.getGuiManager()
                                .setItem(editMenu, 0, new ItemStack(Material.RED_CONCRETE),
                                        Component.text("Delete")
                                                .color(NamedTextColor.RED));

                plugin.getGuiManager()
                        .setItem(editMenu, 8, new ItemStack(Material.BARRIER),
                                Component.text("Close")
                                        .color(NamedTextColor.RED));

                player.openInventory(editMenu);
            }
        }
    }

    @EventHandler
    public void onPlayerEditWarps(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());
        Component titleName = ChatUtils.fixColor("&7Edit: &b" + slotName);

        if (!event.getView().title().equals(titleName)) return;
        if (!event.getAction().equals(InventoryAction.PICKUP_ALL)) return;
        if (slot == null) return;


        ConfigurationSection section = plugin.getWarpsFile().getConfig().getConfigurationSection("special-warps." + slotName);
        Warp warp = new Warp(player.getName(), slotName, section.getLocation("location"));

        switch (slot.getType()) {
            case RED_CONCRETE -> {
                plugin.getWarpManager().removeWarp(player, warp);
                player.closeInventory();
            }

            case BARRIER -> player.closeInventory();
        }
    }

    @EventHandler
    public void onPlayerTeleportRandomWarp(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());
        TextComponent titleName = Component.text("Warps").color(NamedTextColor.RED);

        if (!event.getView().title().equals(titleName)) return;
        if (slot == null || slot.getType() != Material.END_CRYSTAL) return;

        Component slotName = slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName();
        if (!slotName.equals(Component.text("Random Warp").color(NamedTextColor.AQUA))) return;

        event.setCancelled(true);
        plugin.getWarpManager().chooseRandomWarp(player);
    }

    @EventHandler
    public void onPlayerCreateWarp(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());
        TextComponent titleName =
                Component.text("Warps")
                        .color(NamedTextColor.RED);

        if (!event.getView().title().equals(titleName)) return;
        if (slot == null || slot.getType() != Material.ANVIL) return;

        Component slotName = slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName();
        if (!slotName.equals(Component.text("Create Warp").color(NamedTextColor.YELLOW))) return;

        ItemStack stack = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(slotName);
        stack.setItemMeta(meta);

        if (plugin.getWarpManager().containsPlayer(player.getUniqueId())) {
            ChatUtils.sendPrefixedMessage(player, "&cYou cannot create another warp while creating one already&7.");
            return;
        }

        plugin.getWarpManager().addPlayer(player.getUniqueId());
        player.closeInventory();
        player.showTitle(Title.title(Component.text("Type warp name in chat"), Component.text("")));
    }
}
