package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Tournoi;
import tn.esprit.services.ServiceTournoi;

import java.sql.SQLException;
import java.util.Optional;

public class GestionTournoi {
    @FXML private TextField tfNomt;
    @FXML private TextField tfDescriptiont;
    @FXML private DatePicker dpDateDebutt;
    @FXML private DatePicker dpDateFint;
    @FXML private TextField tfNbrEquipes;
    @FXML private TextField tfPrixt;
    @FXML private TextField tfStatutt;
    @FXML private ListView<String> listViewTournois; // ListView pour afficher les tournois
    private Tournoi selectedTournoi = null; // Variable pour stocker le tournoi sélectionné

    private final IService<Tournoi> st = new ServiceTournoi();

    @FXML
    public void initialize() {
        refreshTournoisList();

        // Ajoute un listener pour détecter la sélection d'un tournoi
        listViewTournois.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                remplirChamps(newVal);
            }
        });
    }

    @FXML
    public void ajouterTournoi(ActionEvent actionEvent) {
        try {
            Tournoi t = new Tournoi();
            t.setNomt(tfNomt.getText());
            t.setDescriptiont(tfDescriptiont.getText());
            t.setDate_debutt(dpDateDebutt.getValue());
            t.setDate_fint(dpDateFint.getValue());
            t.setNbr_equipes(Integer.parseInt(tfNbrEquipes.getText()));
            t.setPrixt(Float.parseFloat(tfPrixt.getText()));
            t.setStatutt(tfStatutt.getText());

            st.add(t);
            refreshTournoisList();
            showAlert("Succès", "Tournoi ajouté avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void supprimerTournoi(ActionEvent event) {
        String selectedTournoiInfo = listViewTournois.getSelectionModel().getSelectedItem();

        if (selectedTournoiInfo == null) {
            showAlert("Erreur", "Veuillez sélectionner un tournoi à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        // Extraire uniquement le nom du tournoi
        String selectedTournoiName = selectedTournoiInfo.split(",")[0].replace("Nom: ", "").trim();

        Optional<Tournoi> tournoiASupprimer = st.getAll().stream()
                .filter(t -> t.getNomt().equals(selectedTournoiName))
                .findFirst();

        if (tournoiASupprimer.isPresent()) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation");
            confirmDialog.setHeaderText(null);
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce tournoi ?");
            Optional<ButtonType> result = confirmDialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                st.delete(tournoiASupprimer.get());
                refreshTournoisList();
                showAlert("Succès", "Tournoi supprimé avec succès!", Alert.AlertType.INFORMATION);
                clearFields();
            }
        } else {
            showAlert("Erreur", "Tournoi introuvable.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void modifierTournoi(ActionEvent event) {
        if (selectedTournoi == null) {
            showAlert("Erreur", "Veuillez sélectionner un tournoi à modifier.", Alert.AlertType.ERROR);
            return;
        }

        try {
            selectedTournoi.setDescriptiont(tfDescriptiont.getText());
            selectedTournoi.setDate_debutt(dpDateDebutt.getValue());
            selectedTournoi.setDate_fint(dpDateFint.getValue());
            selectedTournoi.setNbr_equipes(Integer.parseInt(tfNbrEquipes.getText()));
            selectedTournoi.setPrixt(Float.parseFloat(tfPrixt.getText()));
            selectedTournoi.setStatutt(tfStatutt.getText());

            st.update(selectedTournoi);
            refreshTournoisList();
            showAlert("Succès", "Tournoi modifié avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void refreshTournoisList() {
        listViewTournois.getItems().clear();

        for (Tournoi t : st.getAll()) {
            String tournoiInfo = "Nom: " + t.getNomt() +
                    ", Description: " + t.getDescriptiont() +
                    ", Début: " + t.getDate_debutt() +
                    ", Fin: " + t.getDate_fint() +
                    ", Équipes: " + t.getNbr_equipes() +
                    ", Prix: " + t.getPrixt() +
                    ", Statut: " + t.getStatutt();

            listViewTournois.getItems().add(tournoiInfo);
        }
    }


    private void remplirChamps(String tournoiInfo) {
        String nomTournoi = tournoiInfo.split(",")[0].replace("Nom: ", "").trim();

        Optional<Tournoi> tournoi = st.getAll().stream()
                .filter(t -> t.getNomt().equals(nomTournoi))
                .findFirst();

        if (tournoi.isPresent()) {
            selectedTournoi = tournoi.get();
            tfNomt.setText(selectedTournoi.getNomt());
            tfDescriptiont.setText(selectedTournoi.getDescriptiont());
            dpDateDebutt.setValue(selectedTournoi.getDate_debutt());
            dpDateFint.setValue(selectedTournoi.getDate_fint());
            tfNbrEquipes.setText(String.valueOf(selectedTournoi.getNbr_equipes()));
            tfPrixt.setText(String.valueOf(selectedTournoi.getPrixt()));
            tfStatutt.setText(selectedTournoi.getStatutt());
        }
    }


    private void clearFields() {
        tfNomt.clear();
        tfDescriptiont.clear();
        dpDateDebutt.setValue(null);
        dpDateFint.setValue(null);
        tfNbrEquipes.clear();
        tfPrixt.clear();
        tfStatutt.clear();
        selectedTournoi = null;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
