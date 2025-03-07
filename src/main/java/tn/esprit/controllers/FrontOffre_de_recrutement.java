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
import tn.esprit.models.Offre_de_recrutement;
import tn.esprit.services.ServiceOffre_de_recrutement;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FrontOffre_de_recrutement implements Initializable {

    @FXML private VBox offreContainer;
    private final ServiceOffre_de_recrutement serviceOffre = new ServiceOffre_de_recrutement();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshOffresList();
    }

    private void refreshOffresList() {
        Platform.runLater(() -> {
            offreContainer.getChildren().clear();
            List<Offre_de_recrutement> offres = serviceOffre.getAll();
            System.out.println("Nombre d'offres récupérées: " + offres.size());

            for (Offre_de_recrutement offre : offres) {
                StackPane card = createOfferCard(offre);
                offreContainer.getChildren().add(card);
            }
        });
    }

    private StackPane createOfferCard(Offre_de_recrutement offre) {
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

        // Informations sur l'offre
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        // Poste recherché
        Label lblPoste = new Label("Poste: " + offre.getPoste_recherche());
        lblPoste.getStyleClass().add("team-name");

        // Niveau requis
        Label lblNiveau = new Label("Niveau Requis: " + offre.getNiveu_requis());
        lblNiveau.getStyleClass().add("team-info");

        // Salaire proposé
        Label lblSalaire = new Label("Salaire: " + offre.getSalaire_propose() + " €");
        lblSalaire.getStyleClass().add("team-info");

        // Statut
        Label lblStatus = new Label("Status: " + offre.getStatus());
        lblStatus.getStyleClass().add("team-info");

        // Type de contrat
        Label lblContrat = new Label("Contrat: " + offre.getContrat());
        lblContrat.getStyleClass().add("team-info");

        // Ajouter tous les éléments à la boîte d'info
        infoBox.getChildren().addAll(lblPoste, lblNiveau, lblSalaire, lblStatus, lblContrat);

        // Ajouter infoBox au content
        content.getChildren().add(infoBox);

        // Ajouter un effet brillant sur le bord lors du survol
        card.setOnMouseEntered(e -> cardBorder.setStroke(javafx.scene.paint.Color.rgb(224, 176, 255, 1)));
        card.setOnMouseExited(e -> cardBorder.setStroke(javafx.scene.paint.Color.rgb(177, 79, 255, 1)));

        // Ajouter les éléments au conteneur principal
        card.getChildren().addAll(cardBorder, content);

        return card;
    }
}