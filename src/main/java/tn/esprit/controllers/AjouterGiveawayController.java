package tn.esprit.controllers;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import tn.esprit.interfaces.GiveawayService;

import javafx.scene.control.Button;


import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import tn.esprit.models.Giveaway;
import tn.esprit.interfaces.GiveawayService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AjouterGiveawayController {
    // Champs FXML
    @FXML
    private TextField titregField;
    @FXML
    private TextArea descgField;
    @FXML
    private DatePicker datedgField;
    @FXML
    private DatePicker datefgField;
    @FXML
    private TextField iduField;
    @FXML
    private ComboBox<String> statusgComboBox;
    @FXML
    private Button afficherBtn;
    @FXML
    private Label activeCountLabel; // Label pour afficher le nombre de giveaways actifs
    @FXML
    private Label completedCountLabel; // Label pour afficher le nombre de giveaways terminés
    @FXML
    private Label cancelledCountLabel; // Label pour afficher le nombre de giveaways terminés
    @FXML
    private BarChart<String, Number> statusBarChart; // Histogramme pour les statuts
    @FXML
    private CategoryAxis xAxis; // Axe X (statuts)
    @FXML
    private NumberAxis yAxis;



    // Service pour interagir avec la base de données
    private GiveawayService giveawayService = new GiveawayService();

    // Initialisation
    @FXML
    public void initialize() {
        // Remplir la ComboBox avec les statuts
        statusgComboBox.getItems().addAll("actif", "terminé", "annulé");
        statusgComboBox.setValue("actif");
        xAxis.setLabel("Statut");
        yAxis.setLabel("Nombre de Giveaways");

        // Mettre à jour les statistiques et l'histogramme
        updateStatistics();



    }

    // Méthode pour ajouter un giveaway
    @FXML
    public void ajouterGiveaway(ActionEvent event) {
        // Vérification du titre
        if (titregField.getText().isEmpty()) {
            showAlert("Erreur", "Le titre ne peut pas être vide.");
            return;
        }

        // Vérification de la description
        if (descgField.getText().isEmpty()) {
            showAlert("Erreur", "La description ne peut pas être vide.");
            return;
        }

        if (descgField.getText().length() > 500) { // Exemple de limite de longueur
            showAlert("Erreur", "La description ne peut pas dépasser 500 caractères.");
            return;
        }

        // Récupérer les dates du DatePicker
        LocalDate datedg = datedgField.getValue();
        LocalDate datefg = datefgField.getValue();
        LocalDate today = LocalDate.now();

        // Vérification des dates
        if (datedg == null || datefg == null) {
            showAlert("Erreur", "Veuillez sélectionner les dates de début et de fin.");
            return;
        }

        if (datedg.isBefore(today)) {
            showAlert("Erreur", "La date de début ne peut pas être antérieure à aujourd'hui.");
            return;
        }

        if (datefg.isBefore(datedg)) {
            showAlert("Erreur", "La date de fin ne peut pas être antérieure à la date de début.");
            return;
        }

        // Vérification du statut
        if (statusgComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un statut.");
            return;
        }

        // Création du giveaway avec les données validées
        Giveaway giveaway = new Giveaway();
        giveaway.setTitreg(titregField.getText());
        giveaway.setDescg(descgField.getText());
        giveaway.setDatedg(datedg);
        giveaway.setDatefg(datefg);
        giveaway.setStatusg(statusgComboBox.getValue());

        // Ajouter le giveaway à la base de données
        giveawayService.add(giveaway);
        showAlert("Succès", "Giveaway ajouté avec succès !");
        updateStatistics();
        clearFields();
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
        titregField.clear();
        descgField.clear();
        datedgField.setValue(null);
        datefgField.setValue(null);
        //iduField.clear();
        statusgComboBox.setValue("actif");
    }

    // Méthode pour afficher la liste des giveaways
    @FXML
    public void afficherBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGiveaways.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Giveaways");
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'interface AfficherGiveaways.fxml : " + e.getMessage());
        }
    }


    private void updateStatistics() {
        try {
            List<Giveaway> giveaways = giveawayService.recupererGiveaways();

            // Mettre à jour les labels
            long activeCount = giveaways.stream()
                    .filter(g -> "actif".equals(g.getStatusg()))
                    .count();

            long completedCount = giveaways.stream()
                    .filter(g -> "terminé".equals(g.getStatusg()))
                    .count();

            long cancelledCount = giveaways.stream()
                    .filter(g -> "annulé".equals(g.getStatusg()))
                    .count();

            activeCountLabel.setText("Actifs : " + activeCount);
            completedCountLabel.setText("Terminés : " + completedCount);
            cancelledCountLabel.setText("Annulé : " + cancelledCount);

            // Mettre à jour l'histogramme
            updateBarChart();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la récupération des giveaways.");
        }
    }

    private void updateBarChart() {
        try {
            List<Giveaway> giveaways = giveawayService.recupererGiveaways();

            // Compter le nombre de giveaways par statut
            long activeCount = giveaways.stream().filter(g -> "actif".equals(g.getStatusg())).count();
            long completedCount = giveaways.stream().filter(g -> "terminé".equals(g.getStatusg())).count();
            long cancelledCount = giveaways.stream().filter(g -> "annulé".equals(g.getStatusg())).count();

            // Créer une série de données pour l'histogramme
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Statut des Giveaways");
            series.getData().add(new XYChart.Data<>("Actifs", activeCount));
            series.getData().add(new XYChart.Data<>("Terminés", completedCount));
            series.getData().add(new XYChart.Data<>("Annulés", cancelledCount));

            // Ajouter la série au BarChart
            statusBarChart.getData().clear(); // Effacer les données précédentes
            statusBarChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la mise à jour de l'histogramme.");
        }
    }
}





