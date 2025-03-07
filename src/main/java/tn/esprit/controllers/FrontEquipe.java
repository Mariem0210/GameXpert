package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
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
                // Créer une carte pour chaque équipe avec le style cyber futuriste
                StackPane card = createTeamCard(e);
                equipeContainer.getChildren().add(card);
            }
        });
    }

    private StackPane createTeamCard(Equipe equipe) {
        // Conteneur principal de la carte
        StackPane card = new StackPane();
        card.getStyleClass().add("team-card");
        card.setMaxWidth(900);

        // Rectangle de fond avec des coins hexagonaux (simulé avec Polygon)
        Polygon cardBorder = new Polygon(
                0, 10,
                10, 0,
                880, 0,
                890, 10,
                890, 110,
                880, 120,
                10, 120,
                0, 110
        );
        cardBorder.setFill(javafx.scene.paint.Color.rgb(40, 0, 60, 0.7));
        cardBorder.setStroke(javafx.scene.paint.Color.rgb(177, 79, 255, 1));
        cardBorder.setStrokeWidth(2);

        // Contenu de la carte
        HBox content = new HBox(20);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.CENTER_LEFT);

        // Image générée par Robohash
        Image logo = generateLogoForTeam(equipe.getNom_equipe());
        if (logo != null) {
            // Conteneur pour l'image avec bordure futuriste
            StackPane imageContainer = new StackPane();

            Polygon imageBorder = new Polygon(
                    0, 5,
                    5, 0,
                    95, 0,
                    100, 5,
                    100, 95,
                    95, 100,
                    5, 100,
                    0, 95
            );
            imageBorder.setFill(javafx.scene.paint.Color.TRANSPARENT);
            imageBorder.setStroke(javafx.scene.paint.Color.rgb(177, 79, 255, 1));
            imageBorder.setStrokeWidth(2);

            ImageView imageView = new ImageView(logo);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);

            imageContainer.getChildren().addAll(imageBorder, imageView);
            content.getChildren().add(imageContainer);
        }

        // Informations sur l'équipe
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        // Nom de l'équipe avec style futuriste
        Label lblNom = new Label(equipe.getNom_equipe());
        lblNom.getStyleClass().add("team-name");

        // Affichage de l'ID de l'équipe
        HBox idContainer = new HBox(5);
        Label idLabel = new Label("ID:");
        idLabel.setStyle("-fx-text-fill: #b14fff; -fx-font-weight: bold;");
        Label idValue = new Label(String.valueOf(equipe.getIdeq()));
        idValue.getStyleClass().add("team-info");
        idContainer.getChildren().addAll(idLabel, idValue);

        // Affichage de la date de création
        HBox dateContainer = new HBox(5);
        Label dateLabel = new Label("Créé le:");
        dateLabel.setStyle("-fx-text-fill: #b14fff; -fx-font-weight: bold;");

        String dateString = Instant.ofEpochMilli(equipe.getDate_creation().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .toString();
        Label dateValue = new Label(dateString);
        dateValue.getStyleClass().add("team-info");
        dateContainer.getChildren().addAll(dateLabel, dateValue);

        // Ajouter tous les éléments à la boîte d'info
        infoBox.getChildren().addAll(lblNom, idContainer, dateContainer);

        // Ajouter infoBox au content
        content.getChildren().add(infoBox);

        // Ajouter un effet brillant sur le bord lors du survol
        card.setOnMouseEntered(e -> cardBorder.setStroke(javafx.scene.paint.Color.rgb(224, 176, 255, 1)));
        card.setOnMouseExited(e -> cardBorder.setStroke(javafx.scene.paint.Color.rgb(177, 79, 255, 1)));

        // Ajouter les éléments au conteneur principal
        card.getChildren().addAll(cardBorder, content);

        return card;
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