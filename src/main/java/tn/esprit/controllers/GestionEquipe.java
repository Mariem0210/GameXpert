package tn.esprit.controllers;

import tn.esprit.models.Equipe;
import tn.esprit.services.ServiceEquipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;


public class GestionEquipe {



        private ServiceEquipe serviceEquipe = new ServiceEquipe();

        @FXML
        private TextField tfIdeq;
        @FXML
        private TextField tfNomEquipe;
        @FXML
        private DatePicker dpDateCreation;
        @FXML
        private TextField tfIdu;
        @FXML
        private TableView<Equipe> tableViewEquipes;
        @FXML
        private TableColumn<Equipe, Integer> colIdeq;
        @FXML
        private TableColumn<Equipe, String> colNomEquipe;
        @FXML
        private TableColumn<Equipe, String> colDateCreation;
        @FXML
        private TableColumn<Equipe, Integer> colIdu;

        private ObservableList<Equipe> equipesList = FXCollections.observableArrayList();

        @FXML
        public void initialize() {
            colIdeq.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdeq()).asObject());
            colNomEquipe.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom_equipe()));
            colDateCreation.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate_creation().toString()));
            colIdu.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdu()).asObject());
            refreshEquipesList();
        }

        public void refreshEquipesList() {
            List<Equipe> equipes = serviceEquipe.getAll();
            equipesList.setAll(equipes);
            tableViewEquipes.setItems(equipesList);
        }

        @FXML
        private void ajouterEquipe(ActionEvent event) {
            Equipe equipe = new Equipe();
            try {
                equipe.setNom_equipe(tfNomEquipe.getText());
                equipe.setDate_creation(java.sql.Date.valueOf(dpDateCreation.getValue()));
                equipe.setIdu(tfIdu.getText().isEmpty() ? null : Integer.parseInt(tfIdu.getText()));

                serviceEquipe.add(equipe);
                refreshEquipesList();
                showAlert("Succès", "Équipe ajoutée avec succès!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", "Erreur dans les champs de saisie.", Alert.AlertType.ERROR);
            }
        }

        @FXML
        private void modifierEquipe(ActionEvent event) {
            Equipe selectedEquipe = tableViewEquipes.getSelectionModel().getSelectedItem();

            if (selectedEquipe == null) {
                showAlert("Erreur", "Veuillez sélectionner une équipe à modifier.", Alert.AlertType.ERROR);
                return;
            }

            try {
                selectedEquipe.setNom_equipe(tfNomEquipe.getText());
                selectedEquipe.setDate_creation(java.sql.Date.valueOf(dpDateCreation.getValue()));
                selectedEquipe.setIdu(tfIdu.getText().isEmpty() ? null : Integer.parseInt(tfIdu.getText()));

                serviceEquipe.update(selectedEquipe);
                refreshEquipesList();
                showAlert("Succès", "Équipe modifiée avec succès!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", "Erreur dans la modification de l'équipe.", Alert.AlertType.ERROR);
            }
        }

        @FXML
        private void supprimerEquipe(ActionEvent event) {
            Equipe selectedEquipe = tableViewEquipes.getSelectionModel().getSelectedItem();
            if (selectedEquipe == null) {
                showAlert("Erreur", "Veuillez sélectionner une équipe à supprimer.", Alert.AlertType.ERROR);
                return;
            }

            serviceEquipe.delete(selectedEquipe);
            refreshEquipesList();
            showAlert("Succès", "Équipe supprimée avec succès!", Alert.AlertType.INFORMATION);
        }

        private void showAlert(String title, String message, Alert.AlertType alertType) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }


