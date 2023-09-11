package me.notro.specialwarps.managers;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


@Getter
public class GUIManager {

    public Inventory createMenu(@NonNull InventoryHolder holder, int size, @NonNull Component title) {
        return Bukkit.createInventory(holder, size, title);
    }

    public void addItem(@NonNull Inventory menu ,@NonNull ItemStack stack, @NonNull Component displayName, Component[] loreElements) {
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(displayName);

        List<Component> lorelist = new ArrayList<>();
        for (Component loreElement : loreElements) {
            if (loreElement == null) lorelist.add(Component.text(""));

            lorelist.add(loreElement);
        }

        meta.lore(lorelist);
        stack.setItemMeta(meta);
        menu.addItem(stack);
    }

    public void setItem(@NonNull Inventory menu, int index, @NonNull ItemStack stack, @NonNull Component title) {
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(title);

       stack.setItemMeta(meta);
       menu.setItem(index, stack);
    }
}
