package me.notro.specialwarps;

import lombok.Getter;
import me.notro.specialwarps.commands.SetWarpCommand;
import me.notro.specialwarps.commands.WarpsCommand;
import me.notro.specialwarps.listeners.AsyncChatListener;
import me.notro.specialwarps.managers.GUIManager;
import me.notro.specialwarps.managers.WarpManager;
import me.notro.specialwarps.ui.WarpUI;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SpecialWarps extends JavaPlugin {

    private WarpManager warpManager;
    private GUIManager guiManager;
    private ConfigFile warpsFile;

    @Override
    public void onEnable() {
        this.warpsFile = new ConfigFile(this, "specialwarps");

        loadCommands();
        loadListeners();
        loadManagers();
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getLogger().info("has been enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("has been disabled");
    }

    private void loadCommands() {
        getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        getCommand("warps").setExecutor(new WarpsCommand(this));
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new WarpUI(this), this);
        getServer().getPluginManager().registerEvents(new AsyncChatListener(this), this);
    }

    private void loadManagers() {
        this.warpManager = new WarpManager(this);
        this.guiManager = new GUIManager();
    }
}
