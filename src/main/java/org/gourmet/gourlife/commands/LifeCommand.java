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

import java.util.ArrayList;
import java.util.List;

public class LifeCommand implements CommandExecutor, TabCompleter {

    FileConfiguration config = GourLife.getInstance().getConfig();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player) {

            Player player = (Player) commandSender;
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")){
                    GourLife.getInstance().reloadConfig();
                   player.sendMessage(GourLife.color("&aPlugin reloaded"));
                    return true;
                } else {
                    player.sendMessage(GourLife.color(config.getString("prefix") + config.getString("help-commands")));
                    return true;
                }
            }



            /* tre argomenti : reset + give */
            if(args.length == 2){
                // Reset
                if(args[0].equalsIgnoreCase("reset")) {
                    if (player.hasPermission("glife.reset")) {

                        if (config.contains("players-life." + args[1])) {
                            String nome = args[1];

                            config.set("players-life." + nome, config.getInt("life-number"));
                            Bukkit.getPlayer(nome).setGameMode(GameMode.SURVIVAL);
                            Bukkit.getPlayer(nome).teleport(Bukkit.getWorld("world").getSpawnLocation());

                            String message = config.getString("reset-life");
                            message = message.replace("%player%", nome);

                            GourLife.sendAll(GourLife.color(config.getString("prefix") + message));
                            GourLife.getInstance().saveConfig();
                            return true;
                        } else {
                            player.sendMessage(GourLife.color(config.getString("no-player")));
                            return true;
                        }
                    } else {
                        player.sendMessage(GourLife.color(config.getString("prefix") + config.getString("no-permission")));
                        return true;
                    }
                }

                if(args[0].equalsIgnoreCase("check")){
                    if(config.contains("players-life." + args[1])){
                        int lifes = config.getInt("players-life." + args[1]);

                        String message = config.getString("check-lifes");
                        message = message.replace("%player%", args[1]);
                        message = message.replace("%lifes%", "" + lifes);

                        player.sendMessage(GourLife.color(config.getString("prefix") + message));
                        return true;
                    }
                }

                if(args[0].equalsIgnoreCase("give")){
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

                                player.sendMessage(GourLife.color(config.getString("prefix") + config.getString("give-life")));
                                Bukkit.getPlayer(nome).sendMessage(GourLife.color(config.getString("prefix") + message));
                                GourLife.getInstance().saveConfig();
                                if(viteDestinatario + 1 == 1){
                                    Bukkit.getPlayer(nome).setGameMode(GameMode.SURVIVAL);
                                    Bukkit.getPlayer(nome).teleport(Bukkit.getWorld("world").getSpawnLocation());
                                }
                                return true;
                            } else {
                                player.sendMessage(GourLife.color(config.getString("prefix") + config.getString("not-enough-lifes")));
                                return true;
                            }
                        } else {
                            player.sendMessage(GourLife.color(config.getString("prefix") + config.getString("cant-find-player")));
                            return true;
                        }

                    } else {
                        player.sendMessage(GourLife.color(config.getString("prefix") + config.getString("cant-send-to-yourself")));
                        return true;
                    }

                }

            }

            if(args.length >= 3){
                if(args[0].equalsIgnoreCase("set")){
                    if(player.hasPermission("glife.set")){
                        if(config.contains("players-life." + args[1])){

                            String name = args[1];
                            int numero = Integer.parseInt(args[2]);
                            String message = config.getString("set-life");
                            message = message.replace("%new_life%", args[2]);
                            player.sendMessage(GourLife.color(config.getString("prefix") + message));

                            if(numero <= 0){
                                Player p = Bukkit.getPlayer(name);
                                Location loc = Bukkit.getWorld("world").getSpawnLocation();
                                p.teleport(loc);
                                p.getInventory().clear();
                                p.setGameMode(GameMode.SPECTATOR);
                                config.set("players-life." + name, numero);
                                GourLife.getInstance().saveConfig();
                                return true;
                            } else if(config.getInt("players-life." + name) <= 0 && numero >= 0){
                                Player p = Bukkit.getPlayer(name);
                                Location loc = Bukkit.getWorld("world").getSpawnLocation();
                                p.teleport(loc);
                                p.setGameMode(GameMode.SURVIVAL);
                                String newmessage = config.getString("reset-life");
                                newmessage = newmessage.replace("%player%", name);
                                GourLife.sendAll(GourLife.color(config.getString("prefix") + newmessage));
                                config.set("players-life." + name, numero);
                                GourLife.getInstance().saveConfig();
                                return true;
                            }

                            config.set("players-life." + name, numero);
                            GourLife.getInstance().saveConfig();
                            return true;

                        }
                    }
                }
            }

            /* Nessun Argomento, ritorna la vita */
            if(args.length == 0){
                int viteMandatario = config.getInt("players-life." + player.getName());

                String messages = config.getString("your-lifes");
                messages = messages.replace("%lifes%", "" + viteMandatario);

                player.sendMessage(GourLife.color(config.getString("prefix") + messages));
                return true;
            }

            player.sendMessage(GourLife.color(config.getString("prefix") + config.getString("help-commands")));

        } else {
            System.out.println("Only in-game");
        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("give");
            completions.add("check");

            if(sender.hasPermission("glife.reload")){
                completions.add("reload");
            }
            if(sender.hasPermission("glife.set")){
                completions.add("set");
            }

            if(sender.hasPermission("glife.reset")){
                completions.add("reset");

            }
        } else if (args.length == 2) {
            if(args[0].equalsIgnoreCase("reset") && sender.hasPermission("glife.reset")){
                for (Player player : GourLife.getInstance().getServer().getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("check")) {
                for (Player player : GourLife.getInstance().getServer().getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
            if (args[0].equalsIgnoreCase("set")) {
                for (Player player : GourLife.getInstance().getServer().getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }

        }
        List<String> matchedCompletions = new ArrayList<>();
        String currentArg = args[args.length - 1];
        for (String completion : completions) {
            if (completion.toLowerCase().startsWith(currentArg.toLowerCase())) {
                matchedCompletions.add(completion);
            }
        }

        return matchedCompletions;
    }
}
