package com.bluefalconhd.bluetags;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.EntitySelector;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import com.bluefalconhd.bluetags.util.Chat;

import java.util.HashMap;
import java.util.Map;

/*

    Command /bluetags

    options:
        info
        reload
        save
        help
        add|remove|toggle
            <tag>
                <player>

 */


@Command("bluetags")
@CommandPermission("bluetags.command")
public class BluetagsCommand {

    // create a messages dictionary

    // color codes: https://minecraft.tools/en/color-code.php
    private static final Map<String, String> Translations = Map.of(
            "info", ":info: &1Bluetags&r v1.0.0 by BlueFalconHD &5<3&r",
            "reload", ":info: &3&nReloading Bluetags&r...",
            "save", ":info: &9&nSaving Bluetags&r...",
            "add", ":check: &aSuccessfully added tag &f&n{tag}&r&a to &f&n{player}&r.",
            "addfail", ":cross: &cCannot add tag &f&n{tag}&r&c to &f&n{player}&r because the tag already exists.",
            "remove", ":check: &aSuccessfully removed tag &f&n{tag}&r&a from &f&n{player}&r.",
            "removefail", ":cross: &cCannot remove tag &f&n{tag}&r&c from &f&n{player}&r as the tag doesn't exist.",
            "toggleon", ":yin_yang: &2Tag &f&n{tag}&r&2 is now enabled for &f&n{player}&r.",
            "toggleoff", ":yin_yang: &cTag &f&n{tag}&r&c is now disabled for &f&n{player}&r.",
            "list", ":bucket: &9Current tags for &f&n{player}&r: &f&n{tag}&r."
    );


    // create a function to use Chat.format on a given translation key

    private static String formatTranslation(String key) {
        return formatTranslation(key, null, null);
    }

    private static String formatTranslation(String key, String tag, String player) {
        // create a hashmap to store the tag and player, and add each one if neither is null
        Map<String, String> replacements = new HashMap<>();
        if (tag != null) {
            replacements.put("tag", tag);
        }
        if (player != null) {
            replacements.put("player", player);
        }

        // run Chat.Format on the translation with the replacements
        return Chat.Format(Translations.get(key), replacements);
    }

    // define the bluetags instance
    private static final Bluetags bluetags = Bluetags.getPluginInstance();

    @Command("bluetags help")
    private static void sendHelpMessage(Player sender) {
        String[] messages = {
                "&9Bluetags&r v1.0.0 by BlueFalconHD &c<3&r",
                "&b/bluetags info&r - Show plugin info",
                "&b/bluetags reload&r - Reload the plugin",
                "&b/bluetags save&r - Save player data",
                "&b/bluetags <player> add <tag>&r - Add a tag to a player",
                "&b/bluetags <player> remove <tag>&r - Remove a tag from a player",
                "&b/bluetags <player> toggle <tag>&r - Toggle a tag for a player"
        };

        for (String msg : messages) {
            sender.sendMessage(Chat.Format(msg));
        }
    }


    @Subcommand("info")
    public static void info(Player sender) {
        sender.sendMessage(formatTranslation("info"));
    }

    @Subcommand("reload")
    public static void reload() {
        // reload the plugin
        BluetagsAPI.loadPlayerData();
    }

    @Subcommand("save")
    public static void save() {
        // save the player data
        bluetags.getPlayerDataManager().savePlayerData();
    }

    @Subcommand("add")
    public static void add(
            Player sender,
            String tag,
            EntitySelector<Player> targets
    ) {

        for (Player player: targets) {
            // add a tag to a player
            Boolean res = BluetagsAPI.addPlayerTag(player.getUniqueId(), tag);

            String transkey;

            if (res) {
                transkey = "add";
            } else {
                transkey = "addfail";
            }

            String msg = formatTranslation(transkey, tag, player.getName());
            sender.sendMessage(msg);
        }
    }

    @Subcommand("remove")
    public static void remove(
            Player sender,
            String tag,
            EntitySelector<Player> targets
    ) {

        for (Player player: targets) {
            // remove a tag from a player
            Boolean res = BluetagsAPI.removePlayerTag(player.getUniqueId(), tag);

            String transkey;

            if (res) {
                transkey = "remove";
            } else {
                transkey = "removefail";
            }

            String msg = formatTranslation(transkey, tag, player.getName());
            sender.sendMessage(msg);
        }

    }

    @Subcommand("toggle")
    public static void toggle(
            Player sender,
            String tag,
            EntitySelector<Player> targets
    ) {

        for (Player player: targets) {
            // toggle a tag for a player
            if (BluetagsAPI.hasPlayerTag(player.getUniqueId(), tag)) {
                BluetagsAPI.removePlayerTag(player.getUniqueId(), tag);
                sender.sendMessage(formatTranslation("toggleoff", tag, player.getName()));
            } else {
                BluetagsAPI.addPlayerTag(player.getUniqueId(), tag);
                sender.sendMessage(formatTranslation("toggleon", tag, player.getName()));
            }
        }
    }

    @Subcommand("list")
    public static void list(
            Player sender,
            EntitySelector<Player> targets
    ) {
        for (Player player: targets) {
            // list a player's tags
            PlayerData playerData = BluetagsAPI.getPlayerData(player.getUniqueId());
            String msg = formatTranslation("list", playerData.getTags().toString(), player.getName());
            sender.sendMessage(Chat.Format(msg));
        }
    }

    @Subcommand("gui")
    public static void gui(Player sender) {
        BluetagsGUI.OpenGUI(sender);
    }
}