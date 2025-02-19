package tn.esprit.controllers;

import tn.esprit.models.Offre_de_recrutement;
import tn.esprit.services.ServiceOffre_de_recrutement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class GestionOffre_de_recrutement {



        private ServiceOffre_de_recrutement serviceOffre = new ServiceOffre_de_recrutement();

        @FXML
        private TextField tfIdo;
        @FXML
        private TextField tfIdu;
        @FXML
        private TextField tfPosteRecherche;
        @FXML
        private TextField tfNiveauRequis;
        @FXML
        private TextField tfSalairePropose;
        @FXML
        private TextField tfStatus;
        @FXML
        private TextField tfContrat;
        @FXML
        private TableView<Offre_de_recrutement> tableViewOffres;
        @FXML
        private TableColumn<Offre_de_recrutement, Integer> colIdo;
        @FXML
        private TableColumn<Offre_de_recrutement, Integer> colIdu;
        @FXML
        private TableColumn<Offre_de_recrutement, String> colPosteRecherche;
        @FXML
        private TableColumn<Offre_de_recrutement, String> colNiveauRequis;
        @FXML
        private TableColumn<Offre_de_recrutement, Float> colSalairePropose;
        @FXML
        private TableColumn<Offre_de_recrutement, String> colStatus;
        @FXML
        private TableColumn<Offre_de_recrutement, String> colContrat;

        private ObservableList<Offre_de_recrutement> offresList = FXCollections.observableArrayList();

        @FXML
        public void initialize() {
            colIdo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdo()).asObject());
            colIdu.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdu()).asObject());
            colPosteRecherche.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPoste_recherche()));
            colNiveauRequis.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNiveu_requis()));
            colSalairePropose.setCellValueFactory(cellData -> new javafx.beans.property.SimpleFloatProperty(cellData.getValue().getSalaire_propose()).asObject());
            colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
            colContrat.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getContrat()));
            refreshOffresList();
        }

        public void refreshOffresList() {
            List<Offre_de_recrutement> offres = serviceOffre.getAll();
            offresList.setAll(offres);
            tableViewOffres.setItems(offresList);
        }

        @FXML
        private void ajouterOffre(ActionEvent event) {
            Offre_de_recrutement offre = new Offre_de_recrutement();
            try {
                offre.setIdu(Integer.parseInt(tfIdu.getText()));
                offre.setPoste_recherche(tfPosteRecherche.getText());
                offre.setNiveu_requis(tfNiveauRequis.getText());
                offre.setSalaire_propose(Float.parseFloat(tfSalairePropose.getText()));
                offre.setStatus(tfStatus.getText());
                offre.setContrat(tfContrat.getText());

                serviceOffre.add(offre);
                refreshOffresList();
                showAlert("Succès", "Offre ajoutée avec succès!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", "Erreur dans les champs de saisie.", Alert.AlertType.ERROR);
            }
        }

        @FXML
        private void modifierOffre(ActionEvent event) {
            Offre_de_recrutement selectedOffre = tableViewOffres.getSelectionModel().getSelectedItem();

            if (selectedOffre == null) {
                showAlert("Erreur", "Veuillez sélectionner une offre à modifier.", Alert.AlertType.ERROR);
                return;
            }

            try {
                selectedOffre.setIdu(Integer.parseInt(tfIdu.getText()));
                selectedOffre.setPoste_recherche(tfPosteRecherche.getText());
                selectedOffre.setNiveu_requis(tfNiveauRequis.getText());
                selectedOffre.setSalaire_propose(Float.parseFloat(tfSalairePropose.getText()));
                selectedOffre.setStatus(tfStatus.getText());
                selectedOffre.setContrat(tfContrat.getText());

                serviceOffre.update(selectedOffre);
                refreshOffresList();
                showAlert("Succès", "Offre modifiée avec succès!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", "Erreur dans la modification de l'offre.", Alert.AlertType.ERROR);
            }
        }

        @FXML
        private void supprimerOffre(ActionEvent event) {
            Offre_de_recrutement selectedOffre = tableViewOffres.getSelectionModel().getSelectedItem();
            if (selectedOffre == null) {
                showAlert("Erreur", "Veuillez sélectionner une offre à supprimer.", Alert.AlertType.ERROR);
                return;
            }

            serviceOffre.delete(selectedOffre);
            refreshOffresList();
            showAlert("Succès", "Offre supprimée avec succès!", Alert.AlertType.INFORMATION);
        }

        private void showAlert(String title, String message, Alert.AlertType alertType) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }


