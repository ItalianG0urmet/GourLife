package org.gourmet.gourlife.jsonManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.entity.Player;
import org.gourmet.gourlife.GourLife;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JsonDataLoader {

    private File data;
    private Gson gson;
    private Map<UUID, Integer> playerLives;

    public JsonDataLoader(GourLife gourLife){
        this.data = new File(gourLife.getDataFolder(), "data.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.playerLives = new HashMap<>();

        if (!data.exists()) {
            try {
                data.createNewFile();
                try (FileWriter writer = new FileWriter(data)) {
                    gson.toJson(new JsonObject(), writer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadPlayerData(){
        try (FileReader reader = new FileReader(data)){
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            for(String key : jsonObject.keySet()){
                UUID playerId = UUID.fromString(key);
                int lives = jsonObject.get(key).getAsInt();
                playerLives.put(playerId, lives);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerData(){
        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<UUID, Integer> entry : playerLives.entrySet()) jsonObject.addProperty(entry.getKey().toString(), entry.getValue());

        try (FileWriter writer = new FileWriter(data)){
            gson.toJson(jsonObject, writer);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addPlayer(Player player){
        playerLives.putIfAbsent(player.getUniqueId(), 3);
    }

    public int getPlayerLives(Player player){
        return playerLives.getOrDefault(player.getUniqueId(), 0);
    }

    public void setPlayerLives(Player player, int lives){
        playerLives.put(player.getUniqueId(), lives);
    }
}
