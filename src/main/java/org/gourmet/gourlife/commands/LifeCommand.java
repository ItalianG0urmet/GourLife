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
import org.gourmet.gourlife.jsonManager.JsonDataLoader;
import org.gourmet.gourlife.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LifeCommand implements CommandExecutor {

    private FileConfiguration config = GourLife.getInstance().getConfig();
    private String messages;
    private Player player;
    private JsonDataLoader jsonDataLoader = GourLife.getJsonDataLoader();

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
            if(args[0].equalsIgnoreCase("reset")) resetLife(args[1]);
            if(args[0].equalsIgnoreCase("check")) checkLife(args[1]);
            if(args[0].equalsIgnoreCase("give")) giveLife(args);
            return true;

        }

        if(args.length >= 3){
            if(args[0].equalsIgnoreCase("set")) setLife(args);
            return true;
        }

        /* Nessun Argomento, ritorna la vita */
        if(args.length == 0) {
            checkLife(player.getName());
            return true;
        }

        player.sendMessage(Utils.color(config.getString("prefix") + config.getString("help-commands")));

        return true;
    }

    /* Command function */

    private void resetLife(String name){
        if (!player.hasPermission("glife.reset")) {
            player.sendMessage(Utils.color(config.getString("prefix") + config.getString("no-permission")));
            return;
        }

        jsonDataLoader.setPlayerLives(player, config.getInt("life-number"));
        jsonDataLoader.savePlayerData();

        Player reciver = Bukkit.getPlayer(name);

        reciver.setGameMode(GameMode.SURVIVAL);
        reciver.teleport(Bukkit.getWorld("world").getSpawnLocation());

        String message = config.getString("reset-life");
        message = message.replace("%player%", reciver.getName());
        Utils.sendMessageAll(Utils.color(config.getString("prefix") + message));
    }

    private void giveLife(String[] args){
        if(args[1].equalsIgnoreCase(player.getName())) {
            player.sendMessage(Utils.color(config.getString("prefix") + config.getString("cant-send-to-yourself")));
            return;
        }
        if(!config.contains("players-life." + args[1])){
            player.sendMessage(Utils.color(config.getString("prefix") + config.getString("cant-find-player")));
            return;
        }
        if(!(config.getInt("players-life." + player.getName()) >= 1)){
            player.sendMessage(Utils.color(config.getString("prefix") + config.getString("not-enough-lifes")));
            return;
        }

        Player reciver = Bukkit.getPlayer(args[1]);

        int senderLives = GourLife.getJsonDataLoader().getPlayerLives(player);
        int reciverLives = GourLife.getJsonDataLoader().getPlayerLives(reciver);

        jsonDataLoader.setPlayerLives(player, senderLives - 1);
        jsonDataLoader.setPlayerLives(player, reciverLives + 1);
        jsonDataLoader.savePlayerData();

        String message = config.getString("recive-life");
        message = message.replace("%player%", player.getName());

        player.sendMessage(Utils.color(config.getString("prefix") + config.getString("give-life")));
        reciver.sendMessage(Utils.color(config.getString("prefix") + message));
        if(reciverLives + 1 == 1){
            reciver.setGameMode(GameMode.SURVIVAL);
            reciver.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }


    }

    public void checkLife(String name){

        int lifes = jsonDataLoader.getPlayerLives(Bukkit.getPlayer(name));

        String message = config.getString("check-lifes");
        message = message.replace("%player%", name);
        message = message.replace("%lifes%", "" + lifes);

        player.sendMessage(Utils.color(config.getString("prefix") + message));

    }

    public void setLife(String[] args){
        if(!player.hasPermission("glife.set")) {
            player.sendMessage(Utils.color(config.getString("prefix") + config.getString("not-enough-lifes")));
        }

        Player reciver = Bukkit.getPlayer(args[1]);
        int reciverLifeNumber = Integer.parseInt(args[2]);

        String message = config.getString("set-life");
        message = message.replace("%new_life%", args[2]);
        player.sendMessage(Utils.color(config.getString("prefix") + message));

        if(reciverLifeNumber <= 0){
            Location loc = Bukkit.getWorld("world").getSpawnLocation();
            reciver.teleport(loc);
            reciver.getInventory().clear();
            reciver.setGameMode(GameMode.SPECTATOR);
            jsonDataLoader.setPlayerLives(reciver, reciverLifeNumber);
            jsonDataLoader.savePlayerData();
            return;
        }
        if(jsonDataLoader.getPlayerLives(reciver) <= 0 && reciverLifeNumber >= 0){
            Location loc = Bukkit.getWorld("world").getSpawnLocation();
            reciver.teleport(loc);
            reciver.setGameMode(GameMode.SURVIVAL);
            String newmessage = config.getString("reset-life");
            newmessage = newmessage.replace("%player%", reciver.getName());
            Utils.sendMessageAll(Utils.color(config.getString("prefix") + newmessage));
            jsonDataLoader.setPlayerLives(reciver, reciverLifeNumber);
            jsonDataLoader.savePlayerData();
            return;
        }

        jsonDataLoader.setPlayerLives(reciver, reciverLifeNumber);
        jsonDataLoader.savePlayerData();

    }
}
