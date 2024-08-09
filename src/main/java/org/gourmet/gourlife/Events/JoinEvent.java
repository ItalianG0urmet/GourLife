package org.gourmet.gourlife.Events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.gourmet.gourlife.GourLife;
import org.gourmet.gourlife.jsonManager.JsonDataLoader;
import org.gourmet.gourlife.utils.Utils;

public class JoinEvent implements Listener {

    private FileConfiguration config = GourLife.getInstance().getConfig();
    private JsonDataLoader jsonDataLoader = GourLife.getJsonDataLoader();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        if(!config.getBoolean("Join-messages")) return;

        Player player = event.getPlayer();
        String playerName = event.getPlayer().getName();

        String message = config.getString("join");
        message = message.replace("%player%", playerName);
        event.setJoinMessage(Utils.color(config.getString("prefix") + message));

        jsonDataLoader.addPlayer(player);

    }

}
