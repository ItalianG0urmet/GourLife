package org.gourmet.gourlife;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.gourmet.gourlife.Events.LeaveEvent;
import org.gourmet.gourlife.commands.LifeCommand;
import org.gourmet.gourlife.commands.LifeTabComplete;
import org.gourmet.gourlife.Events.DeathEvent;
import org.gourmet.gourlife.Events.JoinEvent;
import org.gourmet.gourlife.Events.KillEvent;
import org.gourmet.gourlife.jsonManager.JsonDataLoader;
import org.gourmet.gourlife.placeHolder.PlaceHolderHearts;
import org.gourmet.gourlife.utils.RevelationTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GourLife extends JavaPlugin {

    @Getter private static GourLife instance;
    @Getter private int timer;
    @Getter private static JsonDataLoader jsonDataLoader;

    private BukkitRunnable revelationTask;

    @Override
    public void onEnable() {

        instance = this;
        saveDefaultConfig();
        placeHolderInit();
        jsonDataLoader = new JsonDataLoader(this);
        jsonDataLoader.loadPlayerData();

        timer = getConfig().getInt("timer_revelation");
        revelationTask = new RevelationTask();
        if(getConfig().getBoolean("revelation")) revelationTask.runTaskTimer(GourLife.getInstance(), 0L, 20L);


        /* Events */
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new KillEvent(), this);
        getServer().getPluginManager().registerEvents(new LeaveEvent(), this);
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
