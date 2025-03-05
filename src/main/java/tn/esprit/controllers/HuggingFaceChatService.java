package tn.esprit.controllers;

import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HuggingFaceChatService {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private static final String API_KEY = "AIzaSyBuLIu8b-ECeYFfbA6HqyBn-dEeYXBJhUg"; // Votre clé API Gemini

    public static String sendMessage(String message) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // Timeout de connexion
                .readTimeout(60, TimeUnit.SECONDS)    // Timeout de lecture
                .writeTimeout(60, TimeUnit.SECONDS)   // Timeout d'écriture
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        // Construire la requête JSON pour Gemini
        String json = "{"
                + "\"contents\": ["
                + "    {"
                + "        \"parts\": ["
                + "            {"
                + "                \"text\": \"" + message + "\""
                + "            }"
                + "        ]"
                + "    }"
                + "]"
                + "}";

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL + "?key=" + API_KEY) // Ajouter la clé API comme paramètre de requête
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();

            // Analyser la réponse JSON
            JsonNode jsonResponse = objectMapper.readTree(responseBody);

            // Vérifier si la réponse contient une erreur
            if (jsonResponse.has("error")) {
                String errorMessage = jsonResponse.get("error").get("message").asText();
                return "Erreur de l'API Gemini : " + errorMessage;
            }

            // Extraire la réponse générée
            if (jsonResponse.has("candidates") && jsonResponse.get("candidates").isArray() && jsonResponse.get("candidates").size() > 0) {
                JsonNode candidate = jsonResponse.get("candidates").get(0);
                if (candidate.has("content") && candidate.get("content").has("parts")) {
                    JsonNode parts = candidate.get("content").get("parts");
                    if (parts.isArray() && parts.size() > 0 && parts.get(0).has("text")) {
                        return parts.get(0).get("text").asText();
                    }
                }
            }

            // En cas de réponse invalide
            return "Erreur : Réponse invalide de l'API Gemini → " + responseBody;
        } catch (IOException e) {
            return "Erreur de connexion à l'API Gemini : " + e.getMessage();
        }
    }
}