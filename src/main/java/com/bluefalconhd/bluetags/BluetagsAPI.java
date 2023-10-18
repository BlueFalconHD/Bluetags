package com.bluefalconhd.bluetags;

import com.bluefalconhd.bluetags.util.Log;

import java.util.UUID;

public class BluetagsAPI {


    private static final Log log = new Log("Bluetags.API");

    public static void syncData(UUID uuid, PlayerData playerData) {
        Bluetags.getPluginInstance().getPlayerDataManager().setPlayerData(uuid, playerData);
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return Bluetags.getPluginInstance().getPlayerDataManager().getPlayerData(uuid);
    }

    public static boolean addPlayerTag(UUID uuid, String tag) {
        PlayerData playerData = getPlayerData(uuid);
        log.info("Adding tag " + tag + " to player with UUID " + uuid.toString());
        if (playerData != null) {
            if (playerData.addTag(tag)) {
                syncData(uuid, playerData);
                return true;
            } else {
                log.warning("   Player already has tag");
            }
        }

        log.severe("   Player data is null");
        return false;
    }

    public static boolean removePlayerTag(UUID uuid, String tag) {
        PlayerData playerData = getPlayerData(uuid);
        log.info("Removing tag " + tag + " from player with UUID " + uuid.toString());
        if (playerData != null) {
            if (playerData.removeTag(tag)) {
                syncData(uuid, playerData);
                return true;
            } else {
                log.warning("   Player doesn't have tag");
            }
        }

        log.severe("   Player data is null");
        return false;
    }

    public static boolean hasPlayerTag(UUID uuid, String tag) {
        PlayerData playerData = getPlayerData(uuid);
        return playerData != null && playerData.hasTag(tag);
    }

    public void savePlayerData() {
        Bluetags.getPluginInstance().getPlayerDataManager().savePlayerData();
    }

    public static void loadPlayerData() {
        Bluetags.getPluginInstance().getPlayerDataManager().loadPlayerData();
    }
}
