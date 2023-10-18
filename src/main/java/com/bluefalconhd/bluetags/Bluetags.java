package com.bluefalconhd.bluetags;

import com.bluefalconhd.bluetags.util.Log;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public final class Bluetags extends JavaPlugin {

    public Bluetags instance;
    public final PlayerDataManager playerDataManager = new PlayerDataManager(this);
    public final BluetagsAPI bluetagsAPI = new BluetagsAPI(playerDataManager);

    private final Log log = new Log("Bluetags");


    public Bluetags getPluginInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        log.info("Enabling Bluetags v1.0.0 by BlueFalconHD <3");

        try {
            playerDataManager.loadPlayerData();
            log.info("Loaded player data.");
        } catch (Exception e) {
            log.severe("Failed to load player data: %s", e.getMessage());
            e.printStackTrace();
        }

        BukkitCommandHandler handler = BukkitCommandHandler.create(instance);
        handler.register(new BluetagsCommand(bluetagsAPI));

        handler.getBrigadier().ifPresent(brigadier -> {
            log.info("Brigadier is present!");
            brigadier.register();
        });
        log.info("Bluetags v1.0.0 by BlueFalconHD <3 enabled!");
    }

    @Override
    public void onDisable() {
        playerDataManager.savePlayerData();
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}
