package org.gourmet.gourlife.Events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.gourmet.gourlife.GourLife;
import org.gourmet.gourlife.jsonManager.JsonDataLoader;
import org.gourmet.gourlife.utils.Utils;

public class LeaveEvent implements Listener {

    private FileConfiguration config = GourLife.getInstance().getConfig();
    private JsonDataLoader jsonDataLoader = GourLife.getJsonDataLoader();

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
