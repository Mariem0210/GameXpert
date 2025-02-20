package tn.esprit.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import tn.esprit.models.Formation;
import tn.esprit.interfaces.FormationService;
import java.io.IOException;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AjouterFormationController {

    // Champs FXML
    @FXML
    private TextField nomfField;
    @FXML
    private TextArea descriptionfField;
    @FXML
    private TextField niveaufField;
    @FXML
    private DatePicker dateDebutfField;
    @FXML
    private DatePicker dateFinfField;
    @FXML
    private TextField capacitefField;
    @FXML
    private TextField prixfField;
    @FXML
    private TextField iduField;
    @FXML
    private Button afficherBtn;
    @FXML
    private Button gestionFormationBtn;

    // Service pour interagir avec la base de données
    private FormationService formationService = new FormationService();

    // Méthode pour ajouter une formation avec contrôle de saisie
    @FXML
    public void ajouterFormation(ActionEvent event) {

            try {
                // Vérification des champs obligatoires
                if (nomfField.getText().isEmpty() || descriptionfField.getText().isEmpty() || niveaufField.getText().isEmpty()
                        || dateDebutfField.getValue() == null || dateFinfField.getValue() == null
                        || capacitefField.getText().isEmpty() || prixfField.getText().isEmpty() || iduField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs !");
                    return;
                }

                // Vérification des valeurs numériques
                int capacite;
                float prix;
                int idu;

                try {
                    capacite = Integer.parseInt(capacitefField.getText());
                    if (capacite <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Erreur de capacité", "La capacité doit être un nombre positif.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer un nombre valide pour la capacité.");
                    return;
                }

                try {
                    prix = Float.parseFloat(prixfField.getText());
                    if (prix < 0) {
                        showAlert(Alert.AlertType.ERROR, "Erreur de prix", "Le prix ne peut pas être négatif.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer un nombre valide pour le prix.");
                    return;
                }

                try {
                    idu = Integer.parseInt(iduField.getText());
                    if (idu <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Erreur d'ID Utilisateur", "L'ID utilisateur doit être positif.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer un nombre valide pour l'ID utilisateur.");
                    return;
                }

                // Vérification des dates
                LocalDate dateDebutf = dateDebutfField.getValue();
                LocalDate dateFinf = dateFinfField.getValue();

                if (dateDebutf.isAfter(dateFinf)) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de dates", "La date de début doit être antérieure à la date de fin.");
                    return;
                }

                if (dateDebutf.isBefore(LocalDate.now())) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de date de début", "La date de début ne peut pas être dans le passé.");
                    return;
                }

                // Création de l'objet Formation
                Formation formation = new Formation();
                formation.setNomf(nomfField.getText());
                formation.setDescriptionf(descriptionfField.getText());
                formation.setNiveauf(niveaufField.getText());
                formation.setDateDebutf(dateDebutf);
                formation.setDateFinf(dateFinf);
                formation.setCapacitef(capacite);
                formation.setPrixf(prix);
                formation.setIdu(idu);

                // Appel du service pour ajouter la formation
                formationService.add(formation);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Formation ajoutée avec succès !");
                clearFields();

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
                e.printStackTrace();
            }
        }


        // Méthode pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour réinitialiser les champs
    private void clearFields() {
        nomfField.clear();
        descriptionfField.clear();
        niveaufField.clear();
        dateDebutfField.setValue(null);
        dateFinfField.setValue(null);
        capacitefField.clear();
        prixfField.clear();
        iduField.clear();
    }

    // Méthode pour afficher la liste des formations
    @FXML
    public void affichebtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormations.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Formations");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de l'interface AfficherFormations.fxml : " + e.getMessage());
        }
    }

}
