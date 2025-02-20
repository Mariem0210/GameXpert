package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Giveaway;
import tn.esprit.interfaces.GiveawayService;

import java.time.LocalDate;

public class ModifierGiveawayController {

    @FXML
    private TextField titreGiveawayField;

    @FXML
    private TextArea descriptionGiveawayArea; // Changement ici

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
        descriptionGiveawayArea.setText(giveaway.getDescg()); // Utilise le TextArea

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
            // Vérification du titre
            if (titreGiveawayField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le titre ne peut pas être vide !");
                return;
            }

            // Vérification de la description
            if (descriptionGiveawayArea.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La description ne peut pas être vide !");
                return;
            }

            if (descriptionGiveawayArea.getText().length() > 500) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La description ne peut pas dépasser 500 caractères !");
                return;
            }

            // Vérification des dates
            LocalDate today = LocalDate.now();
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();

            if (dateDebut == null || dateFin == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les dates de début et de fin doivent être sélectionnées !");
                return;
            }

            if (dateDebut.isBefore(today)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La date de début ne peut pas être antérieure à aujourd'hui !");
                return;
            }

            if (dateFin.isBefore(dateDebut)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La date de fin ne peut pas être antérieure à la date de début !");
                return;
            }

            // Vérification du statut
            if (statusComboBox.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un statut !");
                return;
            }

            // Mise à jour des valeurs du giveaway
            giveaway.setTitreg(titreGiveawayField.getText());
            giveaway.setDescg(descriptionGiveawayArea.getText());
            giveaway.setDatedg(dateDebut);
            giveaway.setDatefg(dateFin);
            giveaway.setStatusg(statusComboBox.getValue());

            // Appel du service pour modifier le giveaway
            giveawayService.modifierGiveaway(giveaway);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le giveaway a été modifié avec succès !");
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
