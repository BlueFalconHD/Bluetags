package com.bluefalconhd.bluetags;

import com.bluefalconhd.bluetags.util.Chat;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.function.Consumer;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BluetagsGUI {

    private static final Map<String, String> NameFormats = Map.of(
            "player", "&r&e{playername}",
            "taginlist", "&7 - &f{tag}",
            "tagitemname", "&r{tag}",
            "tagitemloreremove", "&4:cross: &cRemove tag from {playername}",
            "tagadd", "&7 + &fAdd tag to {playername}",
            "pagenext", "&7Next page",
            "pageprev", "&7Previous page",
            "backbutton", "&7Back"
    );

    private static void PlayClickSound(Player player) {
        player.playSound(player.getLocation(), "ui.button.click", 1, 1);
    }

    private static ItemStack BuildPlayerItem(Player player) {

        ItemStack playeritem = new ItemStack(Material.PLAYER_HEAD);

        String playername = player.getName();
        ArrayList<String> tags = BluetagsAPI.getPlayerData(player.getUniqueId()).getTags();

        ArrayList<String> lore = new ArrayList<>();

        // max 5 tags in lore then ...
        if (tags.size() > 5) {
            tags = new ArrayList<>(tags.subList(0, 5));
            tags.add("...");
        }

        for (String tag : tags) {
            lore.add(Chat.Format(NameFormats.get("taginlist"), Map.of("tag", tag)));
        }

        String displayName = Chat.Format(NameFormats.get("player"), Map.of("playername", playername));

        ItemMeta meta = playeritem.getItemMeta();

        meta.setDisplayName(displayName);
        meta.setLore(lore);

        playeritem.setItemMeta(meta);

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
        String displayName = Chat.Format(NameFormats.get("tagitemname"), Map.of("tag", tag));

        ItemMeta meta = tagitem.getItemMeta();

        meta.setDisplayName(displayName);
        meta.setLore(lore);

        tagitem.setItemMeta(meta);
        return tagitem;
    }

    private static Gui PageGui(ArrayList<GuiItem> items, String title, Boolean backbutton, Consumer<Gui> backbuttonaction) {

        ItemStack nextpage = new ItemStack(Material.AMETHYST_SHARD);
        ItemStack prevpage = new ItemStack(Material.ECHO_SHARD);
        ItemStack bg = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta nextmeta = nextpage.getItemMeta();
        ItemMeta prevmeta = prevpage.getItemMeta();
        ItemMeta bgmeta = bg.getItemMeta();
        ItemMeta backmeta = back.getItemMeta();
        nextmeta.setDisplayName(Chat.Format(NameFormats.get("pagenext")));
        prevmeta.setDisplayName(Chat.Format(NameFormats.get("pageprev")));
        bgmeta.setDisplayName(" ");
        backmeta.setDisplayName(Chat.Format(NameFormats.get("backbutton")));
        nextpage.setItemMeta(nextmeta);
        prevpage.setItemMeta(prevmeta);
        bg.setItemMeta(bgmeta);
        back.setItemMeta(backmeta);


        ChestGui gui = new ChestGui(6, title);

        // convert items ArrayList to List
        List<GuiItem> itemsList = items;

        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 5);
        paginatedPane.populateWithGuiItems(itemsList);

        OutlinePane background = new OutlinePane(0, 5, 9, 1, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(bg, event -> {
            event.setCancelled(true);
        }));
        background.setRepeat(true);

        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        navigation.addItem(new GuiItem(prevpage, event -> {
            PlayClickSound((Player) event.getWhoClicked());
            if (paginatedPane.getPage() > 0) {
                paginatedPane.setPage(paginatedPane.getPage() - 1);

                gui.update();
            }
            event.setCancelled(true);
        }), 3, 0);

        navigation.addItem(new GuiItem(nextpage, event -> {
            PlayClickSound((Player) event.getWhoClicked());

            if (paginatedPane.getPage() < paginatedPane.getPages() - 1) {
                paginatedPane.setPage(paginatedPane.getPage() + 1);

                gui.update();
            }
            event.setCancelled(true);
        }), 5, 0);

        if (backbutton) {
            navigation.addItem(new GuiItem(back, event -> {
                PlayClickSound((Player) event.getWhoClicked());
                backbuttonaction.accept(gui);
                event.setCancelled(true);
            }), 0, 0);
        }

        gui.addPane(background);
        gui.addPane(paginatedPane);
        gui.addPane(navigation);

        return gui;
    }

    private static Gui PlayerGui(Player player) {
        ArrayList<GuiItem> tagItems = new ArrayList<>();
        PlayerData playerData = BluetagsAPI.getPlayerData(player.getUniqueId());
        ArrayList<String> tags = playerData.getTags();

        for (String tag : tags) {
            ItemStack tagitemstack = BuildTagItem(player, tag);
            GuiItem tagitem = new GuiItem(tagitemstack, event -> {
                PlayClickSound(player);
                player.sendMessage("Clicked tag: " + tag);
                BluetagsAPI.removePlayerTag(player.getUniqueId(), tag);
                Gui playergui = PlayerGui(player);
                playergui.show(event.getWhoClicked());
                event.setCancelled(true);
            });

            tagItems.add(tagitem);
        }

        Gui pagegui = PageGui(tagItems, player.getName() + "'s Tags", true, gui -> {
            Gui playerlistgui = PlayerListGui();
            playerlistgui.show(player);
        });

        return pagegui;
    }

    private static Gui PlayerListGui() {

        ArrayList<GuiItem> playerItems = new ArrayList<>();

        for (Player player : Bluetags.getPluginInstance().getServer().getOnlinePlayers()) {
            ItemStack playeritemstack = BuildPlayerItem(player);
            GuiItem playeritem = new GuiItem(playeritemstack, event -> {
                PlayClickSound((Player) event.getWhoClicked());
                Gui playergui = PlayerGui(player);
                playergui.show(event.getWhoClicked());
                event.setCancelled(true);
            });

            playerItems.add(playeritem);
        }



        return PageGui(playerItems, "Player List", false, null);
    }





    public static void OpenGUI(Player player) {
        Gui gui = PlayerListGui();
        gui.show(player);
    }
}
