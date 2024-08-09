package org.gourmet.gourlife.placeHolder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.gourmet.gourlife.GourLife;
import org.gourmet.gourlife.utils.RevelationTask;
import org.gourmet.gourlife.utils.Utils;

public class PlaceHolderHearts extends PlaceholderExpansion {

    private FileConfiguration config = GourLife.getInstance().getConfig();
    int minutes;

    @Override
    public String getAuthor() {
        return "gourmet";
    }

    @Override
    public String getIdentifier() {
        return "glife";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(player == null) {
            return "";
        }

        if(params.equalsIgnoreCase("life")) {

            String viteString = "" + GourLife.getJsonDataLoader().getPlayerLives((Player) player);
            return Utils.color("&c❤ " + viteString);

        }

        if (params.equalsIgnoreCase("revelation")) {

            if (!GourLife.getInstance().getConfig().getBoolean("revelation")) return "revelatio-off";

            minutes = RevelationTask.getTime() / 60;
            String minutesString = minutes + "m";
            return Utils.color(minutesString);


        } else {
            return ".";
        }
    }
}

