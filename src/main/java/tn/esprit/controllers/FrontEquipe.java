package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import tn.esprit.models.Equipe;
import tn.esprit.services.ServiceEquipe;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

public class FrontEquipe implements Initializable {

    @FXML private VBox equipeContainer;

    private final ServiceEquipe serviceEquipe = new ServiceEquipe();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshEquipesList();
    }

    private void refreshEquipesList() {
        Platform.runLater(() -> {
            equipeContainer.getChildren().clear();
            List<Equipe> equipes = serviceEquipe.getAll();

            for (Equipe e : equipes) {
                // Créer une carte pour chaque équipe
                StackPane card = new StackPane();
                card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-radius: 20px; -fx-padding: 20px;");
                card.setMaxWidth(900); // Limite la largeur de la carte

                // Ajouter une image de fond générée par Robohash
                Image logo = generateLogoForTeam(e.getNom_equipe());
                if (logo != null) {
                    ImageView backgroundImage = new ImageView(logo);
                    backgroundImage.setFitWidth(900); // Ajuster la largeur de l'image
                    backgroundImage.setFitHeight(150);
                    backgroundImage.setOpacity(0.3);
                    card.getChildren().add(backgroundImage);
                }

                // Contenu de la carte
                VBox content = new VBox(10);
                content.setAlignment(Pos.CENTER);

                // Affichage du nom de l'équipe
                Label lblNom = new Label(e.getNom_equipe());
                lblNom.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

                // Affichage de l'ID de l'équipe
                Label lblId = new Label("ID: " + e.getIdeq());
                lblId.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

                // Affichage de la date de création de l'équipe
                String dateString = Instant.ofEpochMilli(e.getDate_creation().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString();
                Label lblDate = new Label("Créé le: " + dateString);
                lblDate.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

                // Ajouter les informations à la VBox
                content.getChildren().addAll(lblNom, lblId, lblDate);

                // Ajouter le contenu dans la carte
                card.getChildren().add(content);

                // Ajouter la carte à la VBox
                equipeContainer.getChildren().add(card);
            }
        });
    }

    private Image generateLogoForTeam(String teamName) {
        try {
            // Utilisation de Robohash pour générer un logo unique basé sur le nom de l'équipe
            String urlString = "https://robohash.org/" + teamName + "?set=set2"; // set2 est un style pour générer un logo
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Récupération de l'image en entrée
            InputStream inputStream = connection.getInputStream();
            Image logo = new Image(inputStream);

            return logo;
        } catch (Exception e) {
            System.out.println("Erreur lors de la génération du logo : " + e.getMessage());
            return null;
        }
    }
}