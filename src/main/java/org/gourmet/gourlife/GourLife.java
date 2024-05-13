package org.gourmet.gourlife;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.gourmet.gourlife.commands.LifeCommand;
import org.gourmet.gourlife.events.OnDeath;
import org.gourmet.gourlife.events.OnJoin;
import org.gourmet.gourlife.events.OnKill;
import org.gourmet.gourlife.placeHolder.PlaceHolderHearts;

public final class GourLife extends JavaPlugin {

    private static GourLife instance;
    public static GourLife getInstance(){
        return instance;
    }

    public int timer;

    @Override
    public void onEnable() {
        /* Importi */
        instance = this;
        saveDefaultConfig();
        if(getConfig().getBoolean("rivelation")){startSpawnTimer();}

        /* Console */
        Bukkit.getConsoleSender().sendMessage(color("\n    &b---------------------\n      &fGourLife \n      &fBy G0urmet \n    &b---------------------"));


        /* PlaceHolderApi */
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.hasPermission("glife.error")){
                    p.sendMessage("PlaceHolderApi required");
                }
            }
            Bukkit.getPluginManager().disablePlugin(this);
        }

        /* Event */
        getServer().getPluginManager().registerEvents(new OnDeath(), this);
        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new OnKill(), this);
        new PlaceHolderHearts().register();

        /* Comandi */
        this.getCommand("life").setExecutor(new LifeCommand());
        this.getCommand("life").setTabCompleter(new LifeCommand());

    }

    public void startSpawnTimer() {
        FileConfiguration config = GourLife.getInstance().getConfig();
        new BukkitRunnable() {

            @Override
            public void run() {


                if (timer <= 0) {

                    for(Player p : Bukkit.getOnlinePlayers()){
                        String messages = getConfig().getString("rivelation_message");
                        messages = messages.replace("%player%", p.getName());
                        messages = messages.replace("%position_x%", "" + p.getLocation().getBlockX());
                        messages = messages.replace("%position_y%", "" + p.getLocation().getBlockY());
                        messages = messages.replace("%position_z%", "" + p.getLocation().getBlockZ());
                        messages = messages.replace("%world%", "" + p.getLocation().getWorld().getName());

                        sendAll(getConfig().getString("prefix") + messages);
                        sendTitleALL(color(getConfig().getString("title_rivelation")), color(getConfig().getString("subtitle_rivelation")), 10, 70, 20, true);
                    }
                    timer = getConfig().getInt("timer_rivelation");

                }
                if(timer >= 0){
                    timer--;
                }
            }
        }.runTaskTimer(GourLife.getInstance(), 0L, 20L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if(timer == getConfig().getInt("timer_rivelation") / 3) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Player p = event.getPlayer();
                String messages = getConfig().getString("rivelation_message");
                messages = messages.replace("%player%", p.getName());
                messages = messages.replace("%position_x%", "" + p.getLocation().getBlockX());
                messages = messages.replace("%position_y%", "" + p.getLocation().getBlockY());
                messages = messages.replace("%position_z%", "" + p.getLocation().getBlockZ());
                messages = messages.replace("%world%", "" + p.getLocation().getWorld().getName());

                player.sendMessage(getConfig().getString("prefix") + messages);
            }
        }
    }

    @Override
    public void onDisable() {saveConfig();}

    /* ServerUtil */
    public static String color(String msg) {return msg == null ? " " : ChatColor.translateAlternateColorCodes('&', msg);}
    public static void sendAll(String send){for(Player player : Bukkit.getOnlinePlayers()){player.sendMessage(color(send));}}
    public static void sendTitle(String send){for(Player player : Bukkit.getOnlinePlayers()){player.sendMessage(color(send));}}
    private void sendTitleALL(String title, String subtitle, int fadeIn, int stay, int fadeOut, boolean playSound) {
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            if(playSound){
                p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1.0f, 1.0f);
            }
        }
    }
}
