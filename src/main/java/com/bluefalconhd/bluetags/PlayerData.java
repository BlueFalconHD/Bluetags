package com.bluefalconhd.bluetags;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private final List<String> tags = new ArrayList<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean addTag(String tag) {

        // check if tag is already in list
        if (tags.contains(tag)) {
            return false;
        }

        tags.add(tag);
        return true;
    }

    public boolean removeTag(String tag) {
        // check if tag is in list
        if (!tags.contains(tag)) {
            return false;
        }

        tags.remove(tag);
        return true;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }
}
