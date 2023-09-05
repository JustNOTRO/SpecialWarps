package me.notro.specialwarps.managers;

import lombok.Getter;
import lombok.NonNull;
import me.notro.specialwarps.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class GUIManager {

    private Inventory menu;
    private Inventory anvilMenu;

    public void createMenu(@NonNull InventoryHolder holder, int size, @NonNull Component title) {
        this.menu = Bukkit.createInventory(holder, size, title);
    }

    public void openMenu(@NonNull Player player) {
        player.openInventory(this.menu);
    }

    public void closeMenu(@NonNull Player player) {
        player.closeInventory();
    }

    public void addItem(@NonNull ItemStack stack) {
        this.menu.addItem(stack);
    }

    public void setItem(int index, @NonNull ItemStack stack, @NonNull Component title) {
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(title);

        stack.setItemMeta(meta);
        this.menu.setItem(index, stack);
    }

    public void createAnvilMenu(InventoryHolder holder, @NonNull String title, NamedTextColor color) {
        this.anvilMenu = Bukkit.createInventory(holder, InventoryType.ANVIL, Component.text(title).color(color));
    }

    public void openAnvilMenu(@NonNull Player player) {
        player.openInventory(this.anvilMenu);
    }

    public void closeAnvilMenu() {
        this.anvilMenu.close();
    }
}
