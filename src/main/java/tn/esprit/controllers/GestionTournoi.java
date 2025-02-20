package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Tournoi;
import tn.esprit.services.ServiceTournoi;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class GestionTournoi {

    @FXML private TextField tfNomt;
    @FXML private TextField tfDescriptiont;
    @FXML private DatePicker dpDateDebutt;
    @FXML private DatePicker dpDateFint;
    @FXML private TextField tfNbrEquipes;
    @FXML private TextField tfPrixt;
    @FXML private TextField tfStatutt;
    @FXML private VBox cardContainer; // VBox to display the cards
    private Tournoi selectedTournoi = null; // Variable to store the selected tournament

    private final IService<Tournoi> st = new ServiceTournoi();

    @FXML
    public void initialize() {
        refreshTournoisList();

        // Add listener to detect selected card click
        cardContainer.setOnMouseClicked(event -> {
            if (selectedTournoi != null) {
                remplirChamps(selectedTournoi);
            }
        });
    }

    @FXML
    public void ajouterTournoi(ActionEvent actionEvent) {
        try {
            Tournoi t = new Tournoi();
            t.setNomt(tfNomt.getText());
            t.setDescriptiont(tfDescriptiont.getText());
            t.setDate_debutt(dpDateDebutt.getValue());
            t.setDate_fint(dpDateFint.getValue());
            t.setNbr_equipes(Integer.parseInt(tfNbrEquipes.getText()));
            t.setPrixt(Float.parseFloat(tfPrixt.getText()));
            t.setStatutt(tfStatutt.getText());

            st.add(t);
            refreshTournoisList();
            showAlert("Succès", "Tournoi ajouté avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void supprimerTournoi(ActionEvent event) {
        String selectedTournoiInfo = selectedTournoi != null ? selectedTournoi.getNomt() : null;

        if (selectedTournoiInfo == null) {
            showAlert("Erreur", "Veuillez sélectionner un tournoi à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        Optional<Tournoi> tournoiASupprimer = st.getAll().stream()
                .filter(t -> t.getNomt().equals(selectedTournoiInfo))
                .findFirst();

        if (tournoiASupprimer.isPresent()) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation");
            confirmDialog.setHeaderText(null);
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce tournoi ?");
            Optional<ButtonType> result = confirmDialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                st.delete(tournoiASupprimer.get());
                refreshTournoisList();
                showAlert("Succès", "Tournoi supprimé avec succès!", Alert.AlertType.INFORMATION);
                clearFields();
            }
        } else {
            showAlert("Erreur", "Tournoi introuvable.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void modifierTournoi(ActionEvent event) {
        if (selectedTournoi == null) {
            showAlert("Erreur", "Veuillez sélectionner un tournoi à modifier.", Alert.AlertType.ERROR);
            return;
        }

        try {
            selectedTournoi.setDescriptiont(tfDescriptiont.getText());
            selectedTournoi.setDate_debutt(dpDateDebutt.getValue());
            selectedTournoi.setDate_fint(dpDateFint.getValue());
            selectedTournoi.setNbr_equipes(Integer.parseInt(tfNbrEquipes.getText()));
            selectedTournoi.setPrixt(Float.parseFloat(tfPrixt.getText()));
            selectedTournoi.setStatutt(tfStatutt.getText());

            st.update(selectedTournoi);
            refreshTournoisList();
            showAlert("Succès", "Tournoi modifié avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void refreshTournoisList() {
        cardContainer.getChildren().clear();

        HBox currentRow = new HBox(10);  // Create a row with 10px spacing between cards
        currentRow.setAlignment(Pos.TOP_LEFT); // Align cards to the top left

        int cardCount = 0;

        for (Tournoi t : st.getAll()) {
            // Create a VBox for each card
            VBox card = new VBox(10);
            card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-padding: 20px; -fx-max-width: 300px; -fx-spacing: 15px; -fx-background-radius: 20px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 10); -fx-opacity: 0.95; -fx-transition: transform 0.3s ease, opacity 0.3s ease;");

            // Add tournament details inside the card
            Label nameLabel = new Label("Nom: " + t.getNomt());
            nameLabel.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 16px; -fx-font-family: 'Cambria', serif; -fx-font-weight: bold;");

            Label descriptionLabel = new Label("Description: " + t.getDescriptiont());
            descriptionLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Arial', sans-serif; -fx-line-spacing: 4px;");

            Label startDateLabel = new Label("Début: " + t.getDate_debutt());
            startDateLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Arial', sans-serif;");

            Label endDateLabel = new Label("Fin: " + t.getDate_fint());
            endDateLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Arial', sans-serif;");

            Label teamsLabel = new Label("Équipes: " + t.getNbr_equipes());
            teamsLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Arial', sans-serif;");

            Label priceLabel = new Label("Prix: " + t.getPrixt());
            priceLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Arial', sans-serif;");

            Label statusLabel = new Label("Statut: " + t.getStatutt());
            statusLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Arial', sans-serif;");

            // Add labels to the card
            card.getChildren().addAll(nameLabel, descriptionLabel, startDateLabel, endDateLabel, teamsLabel, priceLabel, statusLabel);

            // Handle single-click to select the tournament
            card.setOnMouseClicked(event -> {
                selectedTournoi = t;
                remplirChamps(t);
            });

            // Handle double-click to navigate to GestionMatch interface
            card.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) { // Double click
                    ouvrirGestionMatch(t);
                }
            });

            // Add the card to the current row
            currentRow.getChildren().add(card);
            cardCount++;

            // If the row contains 4 cards, start a new row
            if (cardCount >= 4) {
                cardContainer.getChildren().add(currentRow);
                currentRow = new HBox(10); // Create a new row
                currentRow.setAlignment(Pos.TOP_LEFT);
                cardCount = 0; // Reset the card count
            }
        }

        // Add the last row if it contains fewer than 4 cards
        if (cardCount > 0) {
            cardContainer.getChildren().add(currentRow);
        }
    }
    private void ouvrirGestionMatch(Tournoi tournoi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionMatch.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de GestionMatch et lui passer le tournoi sélectionné
            GestionMatch controller = loader.getController();
            controller.setTournoi(tournoi); // Assurez-vous que GestionMatch a une méthode setTournoi()

            // Ouvrir la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Gestion des Matchs - " + tournoi.getNomt());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface GestionMatch.", Alert.AlertType.ERROR);
        }
    }

    public void remplirChamps(Tournoi t) {
        tfNomt.setText(t.getNomt());
        tfDescriptiont.setText(t.getDescriptiont());
        dpDateDebutt.setValue(t.getDate_debutt());
        dpDateFint.setValue(t.getDate_fint());
        tfNbrEquipes.setText(String.valueOf(t.getNbr_equipes()));
        tfPrixt.setText(String.valueOf(t.getPrixt()));
        tfStatutt.setText(t.getStatutt());
    }

    private void clearFields() {
        tfNomt.clear();
        tfDescriptiont.clear();
        dpDateDebutt.setValue(null);
        dpDateFint.setValue(null);
        tfNbrEquipes.clear();
        tfPrixt.clear();
        tfStatutt.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}