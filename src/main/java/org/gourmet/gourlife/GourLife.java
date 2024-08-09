package org.gourmet.gourlife;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.gourmet.gourlife.commands.LifeCommand;
import org.gourmet.gourlife.commands.LifeTabComplete;
import org.gourmet.gourlife.events.DeathEvent;
import org.gourmet.gourlife.events.JoinEvent;
import org.gourmet.gourlife.events.KillEvent;
import org.gourmet.gourlife.placeHolder.PlaceHolderHearts;
import org.gourmet.gourlife.utils.RevelationTask;
import org.gourmet.gourlife.utils.Utils;

public final class GourLife extends JavaPlugin {

    @Getter private static GourLife instance;
    @Getter private int timer;

    private BukkitRunnable revelationTask = new RevelationTask();

    @Override
    public void onEnable() {
        /* Importi */
        instance = this;
        saveDefaultConfig();

        if(getConfig().getBoolean("rivelation")){
            revelationTask.runTaskTimer(GourLife.getInstance(), 0L, 20L);
        }

        /* PlaceHolderApi */
        placeHolderInit();

        /* Events */
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new KillEvent(), this);
        new PlaceHolderHearts().register();

        /* Comands */
        this.getCommand("life").setExecutor(new LifeCommand());
        this.getCommand("life").setTabCompleter(new LifeTabComplete());


    }

    @Override
    public void onDisable() {
        revelationTask.cancel();
        saveConfig();
    }



    private void placeHolderInit(){
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.hasPermission("glife.error")){
                    p.sendMessage("PlaceHolderApi required");
                }
            }
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

}
