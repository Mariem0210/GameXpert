package tn.esprit.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import tn.esprit.models.Utilisateur;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class AuthService {
    private static final String CLIENT_SECRET_PATH = "client_secret_595778214069-6lb7fgh59o4bkt8pih6cqpmslapco647.apps.googleusercontent.com.json";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email"
    );
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private final GoogleAuthorizationCodeFlow flow;

    public AuthService() throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                new FileReader(new File(CLIENT_SECRET_PATH))
        );

        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                clientSecrets,
                SCOPES
        )
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
    }

    public Utilisateur authenticate() throws Exception {
        System.out.println("[AUTH] Début de l'authentification...");

        Credential credential = flow.loadCredential("user");
        boolean tokenExpired = credential != null && credential.getExpiresInSeconds() != null
                && credential.getExpiresInSeconds() <= 0;

        if (credential == null || tokenExpired) {
            System.out.println("[AUTH] Lancement du flux OAuth2...");
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        }

        Oauth2 oauth2 = new Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("gamexpert1")
                .build();

        Userinfo userInfo = oauth2.userinfo().get().execute();
        System.out.println("[AUTH] Données Google récupérées : " + userInfo);

        // Gestion des valeurs null
        String nom = userInfo.getGivenName() != null ? userInfo.getGivenName() : "";
        String prenom = userInfo.getFamilyName() != null ? userInfo.getFamilyName() : nom;
        String email = userInfo.getEmail() != null ? userInfo.getEmail() : "";
        String photo = userInfo.getPicture() != null ? userInfo.getPicture() : "";

        return new Utilisateur(
                0,
                nom,
                prenom,
                email,
                "",
                0,
                "JOUEUR",
                LocalDate.now(),
                null,
                photo
        );
    }

    public void supprimerToken() {
        File tokenDir = new File(TOKENS_DIRECTORY_PATH);
        if (tokenDir.exists()) {
            Arrays.stream(tokenDir.listFiles()).forEach(File::delete);
            tokenDir.delete();
        }
    }
}