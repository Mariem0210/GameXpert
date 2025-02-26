package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import tn.esprit.models.Transfert;
import tn.esprit.services.ServiceTransfert;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GestionTransfert {

    @FXML private TextField tfIdu;
    @FXML private TextField tfAncienneEquipe;
    @FXML private TextField tfNouvelleEquipe;
    @FXML private TextField tfMontantt;
    @FXML private DatePicker dpDateT;
    @FXML private VBox transfertContainer;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;

    private Transfert selectedTransfert = null;
    private final ServiceTransfert serviceTransfert = new ServiceTransfert();

    @FXML
    public void initialize() {
        // Appliquer la couleur du texte en blanc pour les champs de saisie et boutons
        tfIdu.setStyle("-fx-text-fill: white;");
        tfAncienneEquipe.setStyle("-fx-text-fill: white;");
        tfNouvelleEquipe.setStyle("-fx-text-fill: white;");
        tfMontantt.setStyle("-fx-text-fill: white;");
        dpDateT.setStyle("-fx-text-fill: white;");
        btnModifier.setStyle("-fx-text-fill: white;");
        btnSupprimer.setStyle("-fx-text-fill: white;");

        refreshTransfertsList();
    }

    @FXML
    public void ajouterTransfert(ActionEvent event) {
        if (!validerSaisie()) {
            return;
        }
        try {
            Transfert transfert = new Transfert();
            transfert.setIdu(Integer.parseInt(tfIdu.getText()));
            transfert.setAncienne_equipe(tfAncienneEquipe.getText());
            transfert.setNouvelle_equipe(tfNouvelleEquipe.getText());
            transfert.setMontantt(Integer.parseInt(tfMontantt.getText()));
            transfert.setDatet(java.sql.Date.valueOf(dpDateT.getValue()));

            serviceTransfert.add(transfert);
            refreshTransfertsList();
            showAlert("Succès", "Transfert ajouté avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur dans les champs de saisie.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void supprimerTransfert(ActionEvent event) {
        if (selectedTransfert == null) {
            showAlert("Erreur", "Veuillez sélectionner un transfert à supprimer.", Alert.AlertType.ERROR);
            return;
        }
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer ce transfert ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            serviceTransfert.delete(selectedTransfert);
            refreshTransfertsList();
            showAlert("Succès", "Transfert supprimé avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        }
    }

    @FXML
    public void refreshTransfertsList() {
        Platform.runLater(() -> {
            transfertContainer.getChildren().clear();
            HBox currentRow = new HBox(10);
            currentRow.setAlignment(Pos.TOP_LEFT);
            int cardCount = 0;

            List<Transfert> transferts = serviceTransfert.getAll();
            for (Transfert t : transferts) {
                StackPane card = new StackPane();
                card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-radius: 20px; -fx-padding: 20px;");

                try {
                    ImageView backgroundImage = new ImageView(new Image(getClass().getResource("/transfer.jpg").toExternalForm()));
                    backgroundImage.setFitWidth(200);
                    backgroundImage.setFitHeight(300);
                    backgroundImage.setOpacity(0.3);
                    card.getChildren().add(backgroundImage);
                } catch (Exception ex) {
                    System.out.println("Erreur de chargement de l'image : " + ex.getMessage());
                }

                VBox content = new VBox(10);
                content.setAlignment(Pos.CENTER);

                Label lblId = new Label("ID: " + t.getIdtr());
                lblId.setStyle("-fx-text-fill: white;");

                Label lblIdu = new Label("ID Joueur: " + t.getIdu());
                lblIdu.setStyle("-fx-text-fill: white;");

                Label lblAncienneEquipe = new Label("Ancienne Équipe: " + t.getAncienne_equipe());
                lblAncienneEquipe.setStyle("-fx-text-fill: white;");

                Label lblNouvelleEquipe = new Label("Nouvelle Équipe: " + t.getNouvelle_equipe());
                lblNouvelleEquipe.setStyle("-fx-text-fill: white;");

                Label lblMontantt = new Label("Montant: " + t.getMontantt() + " €");
                lblMontantt.setStyle("-fx-text-fill: white;");

                String dateString = Instant.ofEpochMilli(t.getDatet().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString();
                Label lblDate = new Label("Date: " + dateString);
                lblDate.setStyle("-fx-text-fill: white;");

                content.getChildren().addAll(lblId, lblIdu, lblAncienneEquipe, lblNouvelleEquipe, lblMontantt, lblDate);
                card.getChildren().add(content);
                card.setOnMouseClicked(event -> remplirChamps(t));

                currentRow.getChildren().add(card);
                cardCount++;

                if (cardCount >= 4) {
                    transfertContainer.getChildren().add(currentRow);
                    currentRow = new HBox(10);
                    cardCount = 0;
                }
            }
            if (cardCount > 0) transfertContainer.getChildren().add(currentRow);
        });
    }

    public void remplirChamps(Transfert t) {
        selectedTransfert = t;
        tfIdu.setText(String.valueOf(t.getIdu()));
        tfAncienneEquipe.setText(t.getAncienne_equipe());
        tfNouvelleEquipe.setText(t.getNouvelle_equipe());
        tfMontantt.setText(String.valueOf(t.getMontantt()));
        dpDateT.setValue(t.getDatet().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    @FXML
    private void modifierTransfert(ActionEvent event) {
        if (selectedTransfert == null) {
            showAlert("Erreur", "Veuillez sélectionner un transfert à modifier.", Alert.AlertType.ERROR);
            return;
        }
        if (!validerSaisie()) {
            return;
        }
        try {
            selectedTransfert.setIdu(Integer.parseInt(tfIdu.getText()));
            selectedTransfert.setAncienne_equipe(tfAncienneEquipe.getText());
            selectedTransfert.setNouvelle_equipe(tfNouvelleEquipe.getText());
            selectedTransfert.setMontantt(Integer.parseInt(tfMontantt.getText()));
            selectedTransfert.setDatet(java.sql.Date.valueOf(dpDateT.getValue()));

            serviceTransfert.update(selectedTransfert);
            refreshTransfertsList();
            showAlert("Succès", "Transfert modifié avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification du transfert.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        tfIdu.clear();
        tfAncienneEquipe.clear();
        tfNouvelleEquipe.clear();
        tfMontantt.clear();
        dpDateT.setValue(null);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validerSaisie() {
        if (tfIdu.getText().isEmpty() || tfAncienneEquipe.getText().isEmpty() || tfNouvelleEquipe.getText().isEmpty()
                || tfMontantt.getText().isEmpty() || dpDateT.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            int montant = Integer.parseInt(tfMontantt.getText());
            if (montant < 0) {
                showAlert("Erreur", "Le montant ne peut pas être négatif.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.", Alert.AlertType.ERROR);
            return false;
        }

        if (dpDateT.getValue().isBefore(java.time.LocalDate.now())) {
            showAlert("Erreur", "La date du transfert doit être supérieure à aujourd'hui.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }
}
