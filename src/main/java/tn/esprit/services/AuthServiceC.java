package tn.esprit.services; // Ajoutez le package approprié

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthServiceC { // Classe correspondant au nom du fichier
    private static final String SECRET_KEY = "6Lc1gesqAAAAADpgDWdPaRZLOR8Rj9Q7m-gUWu1F"; // Clé exacte de l'image

    public boolean verifyRecaptcha(String token) { // Méthode publique
        final String URL = "https://www.google.com/recaptcha/api/siteverify";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(URL);
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("secret", SECRET_KEY));
            params.add(new BasicNameValuePair("response", token));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse response = httpClient.execute(httpPost);
            String jsonResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            return rootNode.path("success").asBoolean();

        } catch (IOException | ParseException e) {
            System.err.println("Erreur de vérification reCAPTCHA: " + e.getMessage());
            return false;
        }
    }
}