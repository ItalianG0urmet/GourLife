package org.gourmet.gourlife.placeHolder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.gourmet.gourlife.GourLife;
import org.gourmet.gourlife.utils.Utils;

public class PlaceHolderHearts extends PlaceholderExpansion {

    private FileConfiguration config = GourLife.getInstance().getConfig();
    private int timer = GourLife.getInstance().getTimer();

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

        if(params.equalsIgnoreCase("lifes")) {
            if (config.contains("players-life." + player.getName())){
                String viteString = "" + config.getString("players-life." + player.getName());
                return Utils.color("&c❤ " + viteString);
            } else {
                return ".";
            }

        }

        if (params.equalsIgnoreCase("revelation")) {
            int minuti = timer / 60;
            if(minuti <= 0){
                return Utils.color("" + timer);
            } else{
                String minutiString = "" + minuti;
                return Utils.color(minutiString);
            }

        } else {
            return ".";
        }
    }
}

