package tn.esprit.controllers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "Tournament Calendar";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        File in = new File(CREDENTIALS_FILE_PATH);
        if (!in.exists()) {
            throw new IOException("Fichier credentials.json introuvable !");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream(in)));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static Calendar getCalendarService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void addEvent(Calendar service, String summary, LocalDate startDate) {
        try {
            Event event = new Event().setSummary(summary);

            // Format Google Calendar pour un événement d'une journée entière
            DateTime startDateTime = new DateTime(startDate.toString()); // YYYY-MM-DD

            EventDateTime start = new EventDateTime()
                    .setDate(startDateTime) // Utilisation correcte pour un événement journalier
                    .setTimeZone("UTC");

            event.setStart(start);

            // La date de fin doit être le jour suivant pour un événement d'une journée entière
            LocalDate endDate = startDate.plusDays(1);
            DateTime endDateTime = new DateTime(endDate.toString());

            EventDateTime end = new EventDateTime()
                    .setDate(endDateTime)
                    .setTimeZone("UTC");

            event.setEnd(end);

            String calendarId = "primary";
            event = service.events().insert(calendarId, event).execute();

            System.out.println("Événement ajouté : " + event.getHtmlLink());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }
}
