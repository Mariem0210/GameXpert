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
                // Créer une carte pour chaque transfert
                StackPane card = new StackPane();
                card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-radius: 20px; -fx-padding: 20px;");
                card.setMaxWidth(900); // Limite la largeur de la carte

                // Ajouter une image de fond (optionnel)
                try {
                    ImageView backgroundImage = new ImageView(new Image(getClass().getResource("/transfer.jpg").toExternalForm()));
                    backgroundImage.setFitWidth(900); // Ajuster la largeur de l'image
                    backgroundImage.setFitHeight(150);
                    backgroundImage.setOpacity(0.3);
                    card.getChildren().add(backgroundImage);
                } catch (Exception ex) {
                    System.out.println("Erreur de chargement de l'image : " + ex.getMessage());
                }

                // Contenu de la carte
                VBox content = new VBox(10);
                content.setAlignment(Pos.CENTER);

                Label lblAncienneEquipe = new Label("Ancienne Équipe: " + t.getAncienne_equipe());
                lblAncienneEquipe.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

                Label lblNouvelleEquipe = new Label("Nouvelle Équipe: " + t.getNouvelle_equipe());
                lblNouvelleEquipe.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

                Label lblMontantt = new Label("Montant: " + t.getMontantt() + " €");
                lblMontantt.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

                String dateString = Instant.ofEpochMilli(t.getDatet().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString();
                Label lblDate = new Label("Date: " + dateString);
                lblDate.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

                content.getChildren().addAll(lblAncienneEquipe, lblNouvelleEquipe, lblMontantt, lblDate);
                card.getChildren().add(content);

                // Ajouter la carte à la VBox
                transfertContainer.getChildren().add(card);
            }
        });
    }
}