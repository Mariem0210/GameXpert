package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import tn.esprit.models.Giveaway;
import tn.esprit.interfaces.GiveawayService;

import java.sql.SQLException;
import java.util.List;

public class AfficherGiveawayView {
    @FXML
    private StackPane giveawayStackPane;

    private GiveawayService giveawayService = new GiveawayService();

    @FXML
    public void initialize() {
        loadGiveaways();
    }

    @FXML
    public void loadGiveaways() {
        giveawayStackPane.getChildren().clear();

        // Utilisation de TilePane pour un affichage en grille
        TilePane container = new TilePane();
        container.setHgap(15); // Réduction des espaces horizontaux
        container.setVgap(15); // Réduction des espaces verticaux
        container.setPrefColumns(3); // Nombre de colonnes

        try {
            List<Giveaway> giveaways = giveawayService.recupererGiveaways();
            for (Giveaway giveaway : giveaways) {
                StackPane giveawayCard = new StackPane();
                VBox cardContent = new VBox(5);
                cardContent.setPrefSize(250, 300); // Taille optimisée

                // Définition du fond pour la carte
                Image backgroundImage = new Image(getClass().getResource("/ground.jpg").toExternalForm());
                BackgroundImage background = new BackgroundImage(
                        backgroundImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, false, true)
                );
                giveawayCard.setBackground(new Background(background));

                // Titres et autres informations
                Label titreLabel = new Label("Titre: " + giveaway.getTitreg());
                Label descriptionLabel = new Label("Description: " + giveaway.getDescg());
                Label dateDebutLabel = new Label("Début: " + giveaway.getDatedg());
                Label dateFinLabel = new Label("Fin: " + giveaway.getDatefg());
                Label statusLabel = new Label("Statut: " + giveaway.getStatusg());

                // Style de texte uniforme pour les labels
                String textStyle = "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;";
                titreLabel.setStyle("-fx-font-size: 18px; " + textStyle); // Plus grand pour le titre
                descriptionLabel.setStyle(textStyle);
                dateDebutLabel.setStyle(textStyle);
                dateFinLabel.setStyle(textStyle);
                statusLabel.setStyle(textStyle);

                // Ajout des éléments dans la carte
                cardContent.getChildren().addAll(titreLabel, descriptionLabel, dateDebutLabel, dateFinLabel, statusLabel);
                giveawayCard.getChildren().add(cardContent);

                // Ajout de la carte au conteneur
                container.getChildren().add(giveawayCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les giveaways.");
        }

        // Ajout du ScrollPane pour éviter les débordements
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");

        giveawayStackPane.getChildren().add(scrollPane);
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
