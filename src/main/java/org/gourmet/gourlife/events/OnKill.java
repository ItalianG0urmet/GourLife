package org.gourmet.gourlife.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.gourmet.gourlife.GourLife;

public class OnKill implements Listener {

    FileConfiguration config = GourLife.getInstance().getConfig();
    @EventHandler
    public void onkill(PlayerDeathEvent event){

        if(event.getEntity().getKiller() instanceof Player){
            Player player = (Player) event.getEntity();
            Player killer = (Player) player.getKiller();

            if(config.contains("players-life." + killer.getName())){
                int lives = config.getInt("players-life." + killer.getName());

                config.set("players-life." + killer.getName(), lives + 1);
                int livesPlus = lives + 1;


                killer.sendMessage(GourLife.color(config.getString("prefix") + config.getString("gained-life")));
                GourLife.getInstance().saveConfig();

            }


        }
    }
}
