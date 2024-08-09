package org.gourmet.gourlife.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.gourmet.gourlife.GourLife;
import org.gourmet.gourlife.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LifeCommand implements CommandExecutor {

    private FileConfiguration config = GourLife.getInstance().getConfig();
    private int viteMandatario;
    private String messages;
    private Player player;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(!(commandSender instanceof Player)){
            System.out.println("Only in-game");
            return true;
        }

        player = (Player) commandSender;

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("reload")){
                GourLife.getInstance().reloadConfig();
                player.sendMessage(Utils.color("&aPlugin reloaded"));
                return true;
            }

            player.sendMessage(Utils.color(config.getString("prefix") + config.getString("help-commands")));
            return true;

        }


            if(args.length == 2){
                if(args[0].equalsIgnoreCase("reset")) resetLife(args);
                if(args[0].equalsIgnoreCase("check")) checkLife(args);
                if(args[0].equalsIgnoreCase("give")) giveLife(args);
                return true;

            }

            if(args.length >= 3){
                if(args[0].equalsIgnoreCase("set")) setLife(args);
                return true;
            }

            /* Nessun Argomento, ritorna la vita */
            if(args.length == 0){
                viteMandatario = config.getInt("players-life." + player.getName());

                messages = config.getString("your-lifes");
                messages = messages.replace("%lifes%", "" + viteMandatario);

                player.sendMessage(Utils.color(config.getString("prefix") + messages));
                return true;
            }

            player.sendMessage(Utils.color(config.getString("prefix") + config.getString("help-commands")));


        return true;

    }

    private void resetLife(String[] args){
        if (player.hasPermission("glife.reset")) {

            if (config.contains("players-life." + args[1])) {
                String nome = args[1];

                config.set("players-life." + nome, config.getInt("life-number"));
                Bukkit.getPlayer(nome).setGameMode(GameMode.SURVIVAL);
                Bukkit.getPlayer(nome).teleport(Bukkit.getWorld("world").getSpawnLocation());

                String message = config.getString("reset-life");
                message = message.replace("%player%", nome);

                Utils.sendMessageAll(Utils.color(config.getString("prefix") + message));
                GourLife.getInstance().saveConfig();
            } else {
                player.sendMessage(Utils.color(config.getString("no-player")));
            }
        } else {
            player.sendMessage(Utils.color(config.getString("prefix") + config.getString("no-permission")));
        }
    }

    private void giveLife(String[] args){
        if(!args[1].equalsIgnoreCase(player.getName())){

            if(config.contains("players-life." + args[1])){
                if(config.getInt("players-life." + player.getName()) >= 1){
                    String nome = args[1];

                    int viteMandatario = config.getInt("players-life." + player.getName());
                    int viteDestinatario = config.getInt("players-life." + nome);

                    config.set("players-life." + player.getName(), viteMandatario - 1);
                    config.set("players-life." + nome, viteDestinatario + 1);

                    String message = config.getString("recive-life");
                    message = message.replace("%player%", player.getName());

                    player.sendMessage(Utils.color(config.getString("prefix") + config.getString("give-life")));
                    Bukkit.getPlayer(nome).sendMessage(Utils.color(config.getString("prefix") + message));
                    GourLife.getInstance().saveConfig();
                    if(viteDestinatario + 1 == 1){
                        Bukkit.getPlayer(nome).setGameMode(GameMode.SURVIVAL);
                        Bukkit.getPlayer(nome).teleport(Bukkit.getWorld("world").getSpawnLocation());
                    }
                } else {
                    player.sendMessage(Utils.color(config.getString("prefix") + config.getString("not-enough-lifes")));
                }
            } else {
                player.sendMessage(Utils.color(config.getString("prefix") + config.getString("cant-find-player")));
            }

        } else {
            player.sendMessage(Utils.color(config.getString("prefix") + config.getString("cant-send-to-yourself")));

        }
    }

    public void checkLife(String[] args){
        if(config.contains("players-life." + args[1])){
            int lifes = config.getInt("players-life." + args[1]);

            String message = config.getString("check-lifes");
            message = message.replace("%player%", args[1]);
            message = message.replace("%lifes%", "" + lifes);

            player.sendMessage(Utils.color(config.getString("prefix") + message));
        }
    }

    public void setLife(String[] args){
            if(player.hasPermission("glife.set")){
                if(config.contains("players-life." + args[1])){

                    String name = args[1];
                    int numero = Integer.parseInt(args[2]);
                    String message = config.getString("set-life");
                    message = message.replace("%new_life%", args[2]);
                    player.sendMessage(Utils.color(config.getString("prefix") + message));

                    if(numero <= 0){
                        Player p = Bukkit.getPlayer(name);
                        Location loc = Bukkit.getWorld("world").getSpawnLocation();
                        p.teleport(loc);
                        p.getInventory().clear();
                        p.setGameMode(GameMode.SPECTATOR);
                        config.set("players-life." + name, numero);
                        GourLife.getInstance().saveConfig();
                        return;
                    } else if(config.getInt("players-life." + name) <= 0 && numero >= 0){
                        Player p = Bukkit.getPlayer(name);
                        Location loc = Bukkit.getWorld("world").getSpawnLocation();
                        p.teleport(loc);
                        p.setGameMode(GameMode.SURVIVAL);
                        String newmessage = config.getString("reset-life");
                        newmessage = newmessage.replace("%player%", name);
                        Utils.sendMessageAll(Utils.color(config.getString("prefix") + newmessage));
                        config.set("players-life." + name, numero);
                        GourLife.getInstance().saveConfig();
                        return;
                    }

                    config.set("players-life." + name, numero);
                    GourLife.getInstance().saveConfig();

                }
            }
    }
}
