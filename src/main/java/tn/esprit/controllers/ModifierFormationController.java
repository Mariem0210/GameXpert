package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Formation;
import tn.esprit.interfaces.FormationService;

import java.sql.Date;
import java.sql.SQLException;

public class ModifierFormationController {

    @FXML
    private TextField nomFormationField, capaciteField, prixField, niveauField, iduField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker dateDebutPicker, dateFinPicker;

    @FXML
    private Button modifierButton, annulerButton;

    private FormationService formationService = new FormationService();
    private Formation formation;  // Formation sélectionnée
    private Runnable onUpdateSuccess; // Callback

    // Méthode d'initialisation pour charger les données de la formation
    @FXML
    public void initData(Formation formation) {
        this.formation = formation;
        nomFormationField.setText(formation.getNomf());
        capaciteField.setText(String.valueOf(formation.getCapacitef()));
        prixField.setText(String.valueOf(formation.getPrixf()));
        niveauField.setText(formation.getNiveauf());
        descriptionField.setText(formation.getDescriptionf());
        iduField.setText(String.valueOf(formation.getIdu()));

        // Initialisation des DatePicker
        dateDebutPicker.setValue(formation.getDateDebutf());
        dateFinPicker.setValue(formation.getDateFinf());
    }

    public void setOnUpdateSuccess(Runnable runnable) {
        this.onUpdateSuccess = runnable;
    }

    @FXML
    private void initialize() {
        modifierButton.setOnAction(event -> modifieFormation());
        annulerButton.setOnAction(event -> fermerFenetre());
    }

    @FXML
    private void modifieFormation() {
        try {
            // Vérification des champs DatePicker
            if (dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les deux dates doivent être renseignées !");
                return;
            }

            // Vérification que la date de début est antérieure à la date de fin
            if (dateDebutPicker.getValue().isAfter(dateFinPicker.getValue())) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La date de début doit être antérieure à la date de fin !");
                return;
            }

            // Vérification que les dates ne sont pas passées (optionnel)
            if (dateDebutPicker.getValue().isBefore(java.time.LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La date de début ne peut pas être antérieure à aujourd'hui !");
                return;
            }

            // Contrôle de saisie
            if (nomFormationField.getText().isEmpty() || niveauField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                    capaciteField.getText().isEmpty() || prixField.getText().isEmpty() || iduField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            if (!capaciteField.getText().matches("\\d+")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La capacité doit être un nombre entier.");
                return;
            }

            if (!prixField.getText().matches("\\d+(\\.\\d{1,2})?")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix doit être un nombre valide.");
                return;
            }

            if (!iduField.getText().matches("\\d+")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID utilisateur doit être un nombre entier.");
                return;
            }

            // Mise à jour des valeurs de la formation
            formation.setNomf(nomFormationField.getText());
            formation.setDateDebutf(dateDebutPicker.getValue());
            formation.setDateFinf(dateFinPicker.getValue());
            formation.setCapacitef(Integer.parseInt(capaciteField.getText()));
            formation.setPrixf(Float.parseFloat(prixField.getText()));
            formation.setNiveauf(niveauField.getText());
            formation.setDescriptionf(descriptionField.getText());
            formation.setIdu(Integer.parseInt(iduField.getText()));

            // Appel du service pour modifier la formation
            formationService.modifierFormation(formation);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "La formation a été modifiée !");
            fermerFenetre();

            // Déclenchement du callback après modification
            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la modification.");
            e.printStackTrace();
        }
    }

    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) modifierButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
