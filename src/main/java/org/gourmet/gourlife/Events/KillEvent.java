package org.gourmet.gourlife.Events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.gourmet.gourlife.GourLife;
import org.gourmet.gourlife.jsonManager.JsonDataLoader;
import org.gourmet.gourlife.utils.Utils;

public class KillEvent implements Listener {

    private FileConfiguration config = GourLife.getInstance().getConfig();
    private JsonDataLoader jsonDataLoader = GourLife.getJsonDataLoader();

    @EventHandler
    public void onkill(PlayerDeathEvent event){

        Player player = (Player) event.getEntity();
        Player killer = (Player) player.getKiller();

        if(!(killer instanceof Player)) return;

        int lives = jsonDataLoader.getPlayerLives(killer);
        jsonDataLoader.setPlayerLives(killer, lives + 1);
        killer.sendMessage(Utils.color(config.getString("prefix") + config.getString("gained-life")));

        lives = jsonDataLoader.getPlayerLives(player);
        jsonDataLoader.setPlayerLives(player, lives - 1);
        player.sendMessage(Utils.color(config.getString("prefix") + config.getString("gained-life")));

        jsonDataLoader.savePlayerData();

    }
}
