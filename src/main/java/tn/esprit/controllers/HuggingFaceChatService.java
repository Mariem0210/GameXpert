package tn.esprit.controllers;

import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HuggingFaceChatService {
    private static final String API_URL = "https://api-inference.huggingface.co/models/HuggingFaceH4/zephyr-7b-beta";
    private static final String API_KEY = "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"; // 🔹 Remplacez par votre vraie clé API

    public static String sendMessage(String message) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // Timeout de connexion
                .readTimeout(60, TimeUnit.SECONDS)    // Timeout de lecture
                .writeTimeout(60, TimeUnit.SECONDS)   // Timeout d'écriture
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        // Construire la requête JSON
        String json = "{ \"inputs\": \"" + message + "\" }";

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)  // ✅ Ajout de "Bearer " avant la clé API
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Analyser la réponse JSON
        JsonNode jsonResponse = objectMapper.readTree(responseBody);

        // ✅ Vérification si la réponse contient des données valides
        if (jsonResponse.isArray() && jsonResponse.size() > 0 && jsonResponse.get(0).has("generated_text")) {
            return jsonResponse.get(0).get("generated_text").asText();
        } else {
            return "Erreur : Réponse invalide de l'API → " + responseBody;
        }
    }
}
