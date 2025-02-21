package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Equipe;
import tn.esprit.services.ServiceEquipe;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class GestionEquipe {

    @FXML private TextField tfIdeq;
    @FXML private TextField tfNomEquipe;
    @FXML private DatePicker dpDateCreation;
    @FXML private TextField tfIdu;
    @FXML private VBox equipeContainer; // Correspond maintenant à "equipeContainer" dans le FXML

    private Equipe selectedEquipe = null;
    private final IService<Equipe> se = new ServiceEquipe();

    @FXML
    public void initialize() {
        if (equipeContainer == null) {
            System.err.println("ERREUR : equipeContainer est NULL. Vérifiez le fichier FXML.");
        } else {
            refreshEquipesList();
        }
        addInputRestrictions();
    }

    private void addInputRestrictions() {
        tfIdeq.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfIdeq.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tfIdu.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfIdu.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private boolean validateInputs() {
        if (tfNomEquipe.getText().isEmpty() || dpDateCreation.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    public void ajouterEquipe(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }
        try {
            Equipe e = new Equipe();
            e.setNom_equipe(tfNomEquipe.getText());

            Date dateCreation = Date.from(dpDateCreation.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            e.setDate_creation(dateCreation);

            if (!tfIdu.getText().isEmpty()) {
                e.setIdu(Integer.parseInt(tfIdu.getText()));
            }

            se.add(e);
            refreshEquipesList();
            showAlert("Succès", "Équipe ajoutée avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout de l'équipe.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void supprimerEquipe(ActionEvent event) {
        if (selectedEquipe == null) {
            showAlert("Erreur", "Veuillez sélectionner une équipe à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        Optional<Equipe> equipeASupprimer = se.getAll().stream()
                .filter(e -> e.getIdeq() == selectedEquipe.getIdeq())
                .findFirst();

        if (equipeASupprimer.isPresent()) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation");
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer cette équipe ?");
            Optional<ButtonType> result = confirmDialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                se.delete(equipeASupprimer.get());
                refreshEquipesList();
                showAlert("Succès", "Équipe supprimée avec succès!", Alert.AlertType.INFORMATION);
                clearFields();
            }
        } else {
            showAlert("Erreur", "Équipe introuvable.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void modifierEquipe(ActionEvent event) {
        if (selectedEquipe == null || !validateInputs()) {
            showAlert("Erreur", "Veuillez sélectionner une équipe à modifier et remplir les champs correctement.", Alert.AlertType.ERROR);
            return;
        }
        try {
            selectedEquipe.setNom_equipe(tfNomEquipe.getText());

            Date dateCreation = Date.from(dpDateCreation.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            selectedEquipe.setDate_creation(dateCreation);

            if (!tfIdu.getText().isEmpty()) {
                selectedEquipe.setIdu(Integer.parseInt(tfIdu.getText()));
            }

            se.update(selectedEquipe);
            refreshEquipesList();
            showAlert("Succès", "Équipe modifiée avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification de l'équipe.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void refreshEquipesList() {
        if (equipeContainer == null) {
            System.err.println("ERREUR : equipeContainer est NULL. Impossible de rafraîchir la liste.");
            return;
        }

        Platform.runLater(() -> {
            equipeContainer.getChildren().clear();
            for (Equipe e : se.getAll()) {
                Label equipeLabel = new Label(e.getNom_equipe() + " - Créée le: " + e.getDate_creation());
                equipeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
                equipeLabel.setOnMouseClicked(event -> {
                    selectedEquipe = e;
                    remplirChamps(e);
                });
                equipeContainer.getChildren().add(equipeLabel);
            }
        });
    }

    public void remplirChamps(Equipe e) {
        tfIdeq.setText(String.valueOf(e.getIdeq()));
        tfNomEquipe.setText(e.getNom_equipe());

        Instant instant = e.getDate_creation().toInstant();
        dpDateCreation.setValue(instant.atZone(ZoneId.systemDefault()).toLocalDate());

        tfIdu.setText(e.getIdu() > 0 ? String.valueOf(e.getIdu()) : "");
    }

    private void clearFields() {
        tfIdeq.clear();
        tfNomEquipe.clear();
        dpDateCreation.setValue(null);
        tfIdu.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
