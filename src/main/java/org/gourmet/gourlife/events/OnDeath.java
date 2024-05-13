package org.gourmet.gourlife.events;

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

public class OnDeath implements Listener {

    FileConfiguration config = GourLife.getInstance().getConfig();

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event){

        Player player = (Player) event.getEntity();
        String playerName = player.getName();

        event.setDeathMessage(null);

        if (config.contains("players-life." + playerName)) {



            int lives = config.getInt("players-life." + playerName);
            if (lives > 1) {
                // Decrementa il numero di vite del giocatore di 1
                config.set("players-life." + playerName, lives - 1);
                int livesMinus = lives - 1;

                String deathMessage = config.getString("death");
                deathMessage = deathMessage.replace("%player%", playerName);
                deathMessage = deathMessage.replace("%lifes%", "" + livesMinus);
                if(config.getBoolean("death-message")){
                    GourLife.sendAll(config.getString("prefix") + deathMessage);
                }
                GourLife.getInstance().saveConfig();



            } else {
                ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) skullItem.getItemMeta();
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
                meta.setDisplayName(player.getDisplayName());
                skullItem.setItemMeta(meta);
                event.getDrops().add(skullItem);

                String message = config.getString("final-death");
                message = message.replace("%player%", playerName);

                if(config.getBoolean("final-message")){
                    GourLife.sendAll(config.getString("prefix") + message);
                }
                player.setGameMode(GameMode.SPECTATOR);
                config.set("players-life." + playerName, 0);
                GourLife.getInstance().saveConfig();


            }
        }



    }


}
