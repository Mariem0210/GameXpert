package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Formation;
import tn.esprit.interfaces.FormationService;

import java.sql.Date;
import java.sql.SQLException;

public class ModifierFormationController {

    @FXML
    private TextField nomFormationField, capaciteField, prixField, niveauField, descriptionField, iduField;

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
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les dates ne peuvent pas être vides !");
                return;
            }

            // Mise à jour des valeurs de la formation
            formation.setNomf(nomFormationField.getText());
            formation.setDateDebutf(dateDebutPicker.getValue()); // Conversion en Date SQL
            formation.setDateFinf(dateFinPicker.getValue()); // Conversion en Date SQL
            formation.setCapacitef(Integer.parseInt(capaciteField.getText()));
            formation.setPrixf(Float.parseFloat(prixField.getText()));
            formation.setNiveauf(niveauField.getText());
            formation.setDescriptionf(descriptionField.getText());
            formation.setIdu(Integer.parseInt(iduField.getText()));

            // Appel du service pour modifier la formation
            formationService.modifierFormation(formation, formation.getIdf());

            showAlert(Alert.AlertType.INFORMATION, "Succès", "La formation a été modifiée !");
            fermerFenetre();

            // Déclenchement du callback après modification
            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification !");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de données incorrect !");
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

