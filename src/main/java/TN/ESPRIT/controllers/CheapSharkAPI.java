package tn.esprit.controllers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.HttpUrl;

import java.io.IOException;

public class CheapSharkAPI {

    private static final String BASE_URL = "https://www.cheapshark.com/api/1.0/deals"; // URL de base sans paramètres

    private final OkHttpClient client = new OkHttpClient();

    // Méthode pour rechercher des offres par mot-clé
    public String searchDeals(String query) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();

        // Ajouter les paramètres de recherche
        urlBuilder.addQueryParameter("storeID", "1"); // Filtre par magasin (storeID=1)
        urlBuilder.addQueryParameter("upperPrice", "15"); // Prix maximum (upperPrice=15)

        if (query != null && !query.isEmpty()) {
            urlBuilder.addQueryParameter("title", query); // Recherche par titre du jeu
        }

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erreur lors de la requête : " + response);
            }
            return response.body().string();
        }
    }
}