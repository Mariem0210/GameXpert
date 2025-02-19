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

    // Méthode pour ajouter une formation
    @FXML
    public void ajouterFormation(ActionEvent event)  {
        // Récupérer les données du formulaire
        Formation formation = new Formation();
        formation.setNomf(nomfField.getText());
        formation.setDescriptionf(descriptionfField.getText());
        formation.setNiveauf(niveaufField.getText());

        // Récupérer les dates du DatePicker
        LocalDate dateDebutf = dateDebutfField.getValue();
        LocalDate dateFinf = dateFinfField.getValue();

        if (dateDebutf != null && dateFinf != null) {
            formation.setDateDebutf(dateDebutf);
            formation.setDateFinf(dateFinf);
        }

        formation.setCapacitef(Integer.parseInt(capacitefField.getText()));
        formation.setPrixf((float) Double.parseDouble(prixfField.getText()));
        formation.setIdu(Integer.parseInt(iduField.getText()));

        // Ajouter la formation à la base de données (ServiceFormation)
        formationService.add(formation);
    }
    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
            // Charger l'interface AfficherFormation.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormations.fxml"));
            Parent root = loader.load();

            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Formations");
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'interface AfficherFormation.fxml : " + e.getMessage());
        }
    }

}
