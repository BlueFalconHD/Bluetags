package com.bluefalconhd.bluetags;

import com.bluefalconhd.bluetags.util.Log;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private final File playerDataFile;
    private final FileConfiguration playerDataConfig;

    private final Bluetags bluetags;
    private final Log log = new Log("Bluetags");

    public PlayerDataManager(Bluetags bluetags) {
        this.bluetags = bluetags;
        playerDataFile = new File(bluetags.getDataFolder(), "players.yml");
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    public void loadPlayerData() {
        // Load player data from the YAML file
        for (String uuidString : playerDataConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);

            log.info("Loading player data for " + uuid);

            PlayerData playerData = new PlayerData(uuid);

            for (String tag : playerDataConfig.getStringList(uuidString)) {
                playerData.addTag(tag);

                log.info("Added tag " + tag + " to player data for " + uuid);
            }

            playerDataMap.put(uuid, playerData);

            log.info("Final data for" + uuid + ": " + playerData);
        }
    }

    public void savePlayerData() {
        // Update playerDataConfig from playerDataMap
        for (Map.Entry<UUID, PlayerData> entry : playerDataMap.entrySet()) {
            playerDataConfig.set(entry.getKey().toString(), entry.getValue().getTags());
        }

        // Save player data to the YAML file
        try {
            playerDataConfig.save(playerDataFile);
            log.info("Saved player data to file.");
        } catch (IOException e) {
            log.severe("Failed to save player data to file: %s", e.getMessage());
            e.printStackTrace();
        }
    }

    public PlayerData getPlayerData(UUID uuid) {
        log.info("Getting player data for " + uuid.toString());

        if (!playerDataMap.containsKey(uuid)) {
            log.info("Player data for " + uuid + " is null, initializing...");
            initPlayerData(uuid);
        }

        return playerDataMap.get(uuid);
    }

    public void initPlayerData(UUID uuid) {
        PlayerData playerData = new PlayerData(uuid);
        playerDataMap.put(uuid, playerData);

        log.info("Initialized player data for " + uuid.toString());
    }

    public void setPlayerData(UUID uuid, PlayerData playerData) {
        playerDataMap.put(uuid, playerData);
    }
}
