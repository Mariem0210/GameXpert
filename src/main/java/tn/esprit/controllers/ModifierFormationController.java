package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.interfaces.FormationService;
import tn.esprit.models.Formation;
import javafx.scene.control.TextArea;

import java.sql.SQLException;
import java.time.LocalDate;

public class ModifierFormationController {

    @FXML
    private TextField nomfField;
    @FXML
    private TextField descriptionfField;
    @FXML
    private TextField niveaufField;
    @FXML
    private TextField dateDebutfField;
    @FXML
    private TextField dateFinfField;
    @FXML
    private TextField capacitefField;
    @FXML
    private TextField prixField;
    @FXML
    private TextField idUserField;
    @FXML
    private Button modifierButton;
    @FXML
    private Button annulerButton;

    private Formation formation;
    private FormationService formationService = new FormationService();
    private Runnable onUpdateSuccess; // Callback pour rafraîchir l'affichage après modification

    public void initData(Formation formation) {
        this.formation = formation;
        nomfField.setText(formation.getNomf());
        descriptionfField.setText(formation.getDescriptionf());
        niveaufField.setText(formation.getNiveauf());
        dateDebutfField.setText(String.valueOf(formation.getDateDebutf()));
        dateFinfField.setText(String.valueOf(formation.getDateFinf()));
        capacitefField.setText(String.valueOf(formation.getCapacitef()));
        prixField.setText(String.valueOf(formation.getPrixf()));
        idUserField.setText(String.valueOf(formation.getIdu()));
    }

    public void setOnUpdateSuccess(Runnable runnable) {
        this.onUpdateSuccess = runnable;
    }

    @FXML
    private void validerModification() {
        try {
            // Mettre à jour l'objet Formation
            formation.setNomf(nomfField.getText());
            formation.setDescriptionf(descriptionfField.getText());
            formation.setNiveauf(niveaufField.getText());
            formation.setDateDebutf(LocalDate.parse(dateDebutfField.getText()));
            formation.setDateFinf(LocalDate.parse(dateFinfField.getText()));
            formation.setCapacitef(Integer.parseInt(capacitefField.getText()));
            formation.setPrixf(Float.parseFloat(prixField.getText()));
            formation.setIdu(Integer.parseInt(idUserField.getText()));

            // Appeler le service pour modifier la formation
            formationService.modifier(formation, formation.getIdf());

            // Fermer la fenêtre après modification
            Stage stage = (Stage) modifierButton.getScene().getWindow();
            stage.close();

            // Déclencher le callback pour mettre à jour la liste
            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void annulerModification() {
        // Fermer la fenêtre sans enregistrer les modifications
        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }
}
