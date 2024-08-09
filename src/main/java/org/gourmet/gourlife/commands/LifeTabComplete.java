package org.gourmet.gourlife.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.gourmet.gourlife.GourLife;

import java.util.ArrayList;
import java.util.List;

public class LifeTabComplete implements TabCompleter {

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
