package tn.esprit.controllers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.models.Game;

public class RawgAPI {
    private static final String API_KEY = "af1c407e71e5466fb7f7b4e8a1b85609"; // Remplace avec ta clé API
    private static final String BASE_URL = "https://api.rawg.io/api/games?key=" + API_KEY;

    public static List<Game> getGames() {
        List<Game> games = new ArrayList<>();
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parser le JSON
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray results = jsonResponse.getAsJsonArray("results");

            for (int i = 0; i < results.size(); i++) {
                JsonObject gameJson = results.get(i).getAsJsonObject();
                String name = gameJson.get("name").getAsString();
                String imageUrl = gameJson.get("background_image").getAsString();
                double rating = gameJson.get("rating").getAsDouble();

                games.add(new Game(name, imageUrl, rating));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return games;
    }
}