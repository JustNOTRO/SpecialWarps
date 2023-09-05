package me.notro.specialwarps.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import me.notro.specialwarps.SpecialWarps;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class AsyncChatListener implements Listener {

    private final SpecialWarps plugin;

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getWarpManager().containsPlayer(player.getUniqueId())) return;

        String message = LegacyComponentSerializer.legacyAmpersand().serialize(event.message());

        event.setCancelled(true);
        plugin.getWarpManager().removePlayer(player.getUniqueId());
        plugin.getWarpManager().createWarp(player, message, player.getLocation());
    }
}
