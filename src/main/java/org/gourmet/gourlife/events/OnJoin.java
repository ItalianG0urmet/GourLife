package org.gourmet.gourlife.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.gourmet.gourlife.GourLife;

public class OnJoin implements Listener {
    FileConfiguration config = GourLife.getInstance().getConfig();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();
        String playerName = event.getPlayer().getName();

        /* Messaggio di benvenuto */
        String message = config.getString("join");
        message = message.replace("%player%", playerName);
        if(config.getBoolean("Join-messages")){
            event.setJoinMessage(GourLife.color(config.getString("prefix") + message));
        }

        /* Confiuguratore vite al primo accesso */
        if (!config.contains("players-life." + playerName)) {
            config.set("players-life." + playerName, config.getInt("life-number"));
            GourLife.getInstance().saveConfig();
        } else {
            int vitaPlayer = config.getInt("players-life." + playerName);
        }


    }

    /* Leave event*/
    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        String message = config.getString("leave");
        message = message.replace("%player%", event.getPlayer().getName());
        if(config.getBoolean("leave-messages")){
            event.setQuitMessage(GourLife.color(config.getString("prefix") + message));
        }


    }
}
