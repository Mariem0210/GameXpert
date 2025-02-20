package tn.esprit.controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import tn.esprit.models.Giveaway;
import tn.esprit.interfaces.GiveawayService;

import java.sql.Date;
import java.sql.SQLException;

public class ModifierGiveawayController {

    @FXML
    private TextField titreGiveawayField, descriptionGiveawayField;

    @FXML
    private DatePicker dateDebutPicker, dateFinPicker;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Button modifierButton, annulerButton;

    private GiveawayService giveawayService = new GiveawayService();
    private Giveaway giveaway;  // Giveaway sélectionné
    private Runnable onUpdateSuccess; // Callback

    // Méthode d'initialisation pour charger les données du giveaway
    @FXML
    public void initData(Giveaway giveaway) {
        this.giveaway = giveaway;
        titreGiveawayField.setText(giveaway.getTitreg());
        descriptionGiveawayField.setText(giveaway.getDescg());

        // Initialisation des DatePicker
        dateDebutPicker.setValue(giveaway.getDatedg());
        dateFinPicker.setValue(giveaway.getDatefg());

        // Initialisation du ComboBox
        statusComboBox.getItems().setAll("Actif", "Terminé", "Annulé");
        statusComboBox.setValue(giveaway.getStatusg());
    }

    public void setOnUpdateSuccess(Runnable runnable) {
        this.onUpdateSuccess = runnable;
    }

    @FXML
    private void initialize() {
        modifierButton.setOnAction(event -> modifieGiveaway());
        annulerButton.setOnAction(event -> fermerFenetre());
    }

    @FXML
    private void modifieGiveaway() {
        try {
            // Vérification des champs DatePicker et ComboBox
            if (dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null || statusComboBox.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les dates et le statut ne peuvent pas être vides !");
                return;
            }

            // Mise à jour des valeurs du giveaway
            giveaway.setTitreg(titreGiveawayField.getText());
            giveaway.setDescg(descriptionGiveawayField.getText());
            giveaway.setDatedg(dateDebutPicker.getValue());
            giveaway.setDatefg(dateFinPicker.getValue());
            giveaway.setStatusg(statusComboBox.getValue());

            // Appel du service pour modifier le giveaway
            giveawayService.modifierGiveaway(giveaway);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le giveaway a été modifié !");
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
