package org.gourmet.gourlife.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Utils {

    /**
     * Color a string with "&e"
     *
     * @param msg The text to color
     */
    public static String color(String msg) {
        return msg == null ? " " : ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * Send message to all the player
     * on the server
     * @param send
     */
    public static void sendMessageAll(String send){
        for(Player player : Bukkit.getOnlinePlayers()){
            player.sendMessage(color(send));
        }
    }

    /**
     * Se
     * @param send
     */
    public static void sendTitleAll(String send){
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(color(send));
        }
    }

    /**
     * Send a title to all the player
     * on the server and a sound
     *
     * @param title Princiapl title text
     * @param subtitle Text under the titler
     * @param playSound Sound to play when the title appear
     */
    public static void sendTitleAllEX(String title, String subtitle, int fadeIn, int stay, int fadeOut, boolean playSound) {
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            if(playSound){
                p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1.0f, 1.0f);
            }
        }
    }

}
