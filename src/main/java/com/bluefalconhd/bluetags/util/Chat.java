package com.bluefalconhd.bluetags.util;

import org.bukkit.ChatColor;

import java.util.Map;

public class Chat {

    public static final Map<String, String> SYMBOLS = Map.ofEntries(Map.entry("sun", "☀"), Map.entry("cloud", "☁"), Map.entry("umbrella", "☂"), Map.entry("snowman_rain", "☃"), Map.entry("comet", "☄"), Map.entry("checkbox", "☑"), Map.entry("umbrella_rain", "☔"), Map.entry("skull", "☠"), Map.entry("peace", "☮"), Map.entry("yin_yang", "☯"), Map.entry("sad", "☹"), Map.entry("happy", "☺"), Map.entry("male", "♀"), Map.entry("female", "♂"), Map.entry("spade", "♠"), Map.entry("club", "♣"), Map.entry("heart", "♥"), Map.entry("diamond", "♦"), Map.entry("anchor", "⚓"), Map.entry("crossed_swords", "⚔"), Map.entry("splash_potion", "⚗"), Map.entry("warning", "⚠"), Map.entry("lightning", "⚡"), Map.entry("snowman", "⛄"), Map.entry("cloud_lightning", "⛈"), Map.entry("pickaxe", "⛏"), Map.entry("envelope", "✉"), Map.entry("check", "✔"), Map.entry("heart_exclamation", "❣"), Map.entry("snowflake", "❄"), Map.entry("fire", "🔥"), Map.entry("wave", "🌊"), Map.entry("dagger", "🗡"), Map.entry("bow_arrow", "🏹"), Map.entry("axe", "🪓"), Map.entry("trident", "🔱"), Map.entry("fishing_pole", "🎣"), Map.entry("potion", "🧪"), Map.entry("shears", "✂"), Map.entry("meat", "🍖"), Map.entry("bucket", "🪣"), Map.entry("bell", "🔔"), Map.entry("hourglass", "⏳"), Map.entry("info", "ℹ"), Map.entry("cross", "❌"));


    public static String Format(String message) {
        return Format(message, Map.of());
    }

    public static String Format(String message, Map<String, String> replacements) {
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);

            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }

            // replace :<symbolname>: with the symbol
            for (Map.Entry<String, String> entry : SYMBOLS.entrySet()) {
                message = message.replace(":" + entry.getKey() + ":", entry.getValue());
            }
        }
        return message;
    }


}
