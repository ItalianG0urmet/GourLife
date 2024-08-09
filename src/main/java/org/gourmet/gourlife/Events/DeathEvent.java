package org.gourmet.gourlife.Events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.gourmet.gourlife.GourLife;
import org.gourmet.gourlife.jsonManager.JsonDataLoader;
import org.gourmet.gourlife.utils.Utils;

public class DeathEvent implements Listener {

    private JsonDataLoader jsonDataLoader = GourLife.getJsonDataLoader();
    private FileConfiguration config = GourLife.getInstance().getConfig();

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event){

        Player player = (Player) event.getEntity();

        event.setDeathMessage(null);
        int lives = jsonDataLoader.getPlayerLives(player);

        if (lives > 1) {
            lives--;
            jsonDataLoader.setPlayerLives(player, lives);
            jsonDataLoader.savePlayerData();

            String deathMessage = config.getString("death");
            deathMessage = deathMessage.replace("%player%", player.getName());
            deathMessage = deathMessage.replace("%lifes%", "" + lives);
            if(config.getBoolean("death-message")) Utils.sendMessageAll(config.getString("prefix") + deathMessage);

            return;

        }

        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skullItem.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        meta.setDisplayName(player.getDisplayName());
        skullItem.setItemMeta(meta);
        event.getDrops().add(skullItem);

        String message = config.getString("final-death");
        message = message.replace("%player%", player.getName());
        if(config.getBoolean("final-message")){Utils.sendMessageAll(config.getString("prefix") + message);}

        player.setGameMode(GameMode.SPECTATOR);
        jsonDataLoader.setPlayerLives(player, 0);
        jsonDataLoader.savePlayerData();

    }
}
