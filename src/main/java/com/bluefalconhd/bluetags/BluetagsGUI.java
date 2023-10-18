package com.bluefalconhd.bluetags;

import com.bluefalconhd.bluetags.util.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.bluefalconhd.bluetags.BluetagsAPI;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Map;

public class BluetagsGUI {

    private static final Map<String, String> NameFormats = Map.of(
            "player", "{playername}",
            "taginlist", "&7 - &f{tag}",
            "tagitemname", "&f",
            "tagitemloreremove", ":cross: &cRemove tag from {playername}",
            "tagadd", "&7 + &fAdd tag to {playername}",
            "pagenext", "&7Next page",
            "pageprev", "&7Previous page"
    );

    private static ItemStack BuildPlayerItem(Player player) {

        ItemStack playeritem = new ItemStack(Material.PLAYER_HEAD);

        String playername = player.getName();
        ArrayList<String> tags = BluetagsAPI.getPlayerData(player.getUniqueId()).getTags();

        ArrayList<String> lore = new ArrayList<>();

        for (String tag : tags) {
            lore.add(Chat.Format(NameFormats.get("taginlist"), Map.of("tag", tag)));
        }

        playeritem.getItemMeta().setDisplayName(Chat.Format(NameFormats.get("player"), Map.of("playername", playername)));
        playeritem.getItemMeta().setLore(lore);

        // set head skin
        SkullMeta skullMeta = (SkullMeta) playeritem.getItemMeta();
        skullMeta.setOwningPlayer(player);
        playeritem.setItemMeta(skullMeta);

        return playeritem;
    }

    private static ItemStack BuildTagItem(Player player, String tag) {
        ItemStack tagitem = new ItemStack(Material.NAME_TAG);
        String playername = player.getName();

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Chat.Format(NameFormats.get("tagitemloreremove"), Map.of("playername", playername)));

        tagitem.getItemMeta().setDisplayName(Chat.Format(NameFormats.get("tagitemname"), Map.of("playername", playername)));
        tagitem.getItemMeta().setLore(lore);

        return tagitem;
    }
}
