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
import tn.esprit.models.Transfert;
import tn.esprit.services.ServiceTransfert;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

public class FrontTransfert implements Initializable {

    @FXML private VBox transfertContainer;

    private final ServiceTransfert serviceTransfert = new ServiceTransfert();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshTransfertsList();
    }

    private void refreshTransfertsList() {
        Platform.runLater(() -> {
            transfertContainer.getChildren().clear();
            List<Transfert> transferts = serviceTransfert.getAll();

            for (Transfert t : transferts) {
                // Créer une carte pour chaque transfert avec le style cyber futuriste
                StackPane card = createTransferCard(t);
                transfertContainer.getChildren().add(card);
            }
        });
    }

    private StackPane createTransferCard(Transfert transfert) {
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

        // Informations sur le transfert
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        // Ancienne équipe
        Label lblAncienneEquipe = new Label("Ancienne Équipe: " + transfert.getAncienne_equipe());
        lblAncienneEquipe.getStyleClass().add("team-name");

        // Nouvelle équipe
        Label lblNouvelleEquipe = new Label("Nouvelle Équipe: " + transfert.getNouvelle_equipe());
        lblNouvelleEquipe.getStyleClass().add("team-info");

        // Montant du transfert
        Label lblMontant = new Label("Montant: " + transfert.getMontantt() + " €");
        lblMontant.getStyleClass().add("team-info");

        // Date du transfert
        String dateString = Instant.ofEpochMilli(transfert.getDatet().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .toString();
        Label lblDate = new Label("Date: " + dateString);
        lblDate.getStyleClass().add("team-info");

        // Ajouter tous les éléments à la boîte d'info
        infoBox.getChildren().addAll(lblAncienneEquipe, lblNouvelleEquipe, lblMontant, lblDate);

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