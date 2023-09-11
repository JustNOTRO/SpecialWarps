package me.notro.specialwarps.utils;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

public class ChatUtils {

    public static final Component
            NO_SENDER_EXECUTOR = fixColor("&cOnly players can execute this command&7."),
            NO_PERMISSION = fixColor("&cYou don't have permission to perform that command&7."),
            NO_PLAYER_EXISTENCE = fixColor("&cPlayer does not exist/online&7.");

    public static Component fixColor(@NonNull String message) {
        return LegacyComponentSerializer.legacy('&').deserialize(message);
    }

    public static void sendPrefixedMessage(@NonNull CommandSender sender, @NonNull String message) {
        sender.sendMessage(prefix.append(fixColor(message)));
    }

    public static void sendComponentMessage(@NonNull CommandSender sender, @NonNull Component message) {
        sender.sendMessage(prefix.append(message));
    }

    @Getter
    private static final Component prefix = fixColor("&8[&eSpecialWarps&8] &7>>> ");
}
