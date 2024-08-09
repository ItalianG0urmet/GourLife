package org.gourmet.gourlife.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.gourmet.gourlife.GourLife;
import org.gourmet.gourlife.utils.Utils;

public class JoinEvent implements Listener {

    private FileConfiguration config = GourLife.getInstance().getConfig();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        if(!config.getBoolean("Join-messages")){
            return;
        }

        Player player = event.getPlayer();
        String playerName = event.getPlayer().getName();

        /* Messaggio di benvenuto */
        String message = config.getString("join");
        message = message.replace("%player%", playerName);
        event.setJoinMessage(Utils.color(config.getString("prefix") + message));


        /* Confiuguratore vite al primo accesso */
        if (!config.contains("players-life." + playerName)) {
            config.set("players-life." + playerName, config.getInt("life-number"));
            GourLife.getInstance().saveConfig();
            return;
        }

        int vitaPlayer = config.getInt("players-life." + playerName);



    }

    /* Leave event*/
    @EventHandler
    public void onLeave(PlayerQuitEvent event){

        if(config.getBoolean("leave-messages")){
            return;
        }

        String message = config.getString("leave");
        message = message.replace("%player%", event.getPlayer().getName());
        event.setQuitMessage(Utils.color(config.getString("prefix") + message));



    }
}
