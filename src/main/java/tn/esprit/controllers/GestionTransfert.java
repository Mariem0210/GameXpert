package tn.esprit.controllers;

import tn.esprit.models.Transfert;
import tn.esprit.services.ServiceTransfert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class GestionTransfert {

    private ServiceTransfert serviceTransfert = new ServiceTransfert();

    @FXML
    private TextField tfIdtr;
    @FXML
    private TextField tfIdu;
    @FXML
    private TextField tfAncienneEquipe;
    @FXML
    private TextField tfNouvelleEquipe;
    @FXML
    private TextField tfMontantt;
    @FXML
    private DatePicker dpDateT;
    @FXML
    private TableView<Transfert> tableViewTransferts;
    @FXML
    private TableColumn<Transfert, Integer> colIdtr;
    @FXML
    private TableColumn<Transfert, Integer> colIdu;
    @FXML
    private TableColumn<Transfert, String> colAncienneEquipe;
    @FXML
    private TableColumn<Transfert, String> colNouvelleEquipe;
    @FXML
    private TableColumn<Transfert, Integer> colMontantt;
    @FXML
    private TableColumn<Transfert, String> colDateT;

    private ObservableList<Transfert> transfertsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colIdtr.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdtr()).asObject());
        colIdu.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdu()).asObject());
        colAncienneEquipe.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAncienne_equipe()));
        colNouvelleEquipe.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNouvelle_equipe()));
        colMontantt.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getMontantt()).asObject());
        colDateT.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDatet().toString()));
        refreshTransfertsList();
    }

    public void refreshTransfertsList() {
        List<Transfert> transferts = serviceTransfert.getAll();
        transfertsList.setAll(transferts);
        tableViewTransferts.setItems(transfertsList);
    }

    @FXML
    private void ajouterTransfert(ActionEvent event) {
        Transfert transfert = new Transfert();
        try {
            transfert.setIdu(Integer.parseInt(tfIdu.getText()));
            transfert.setAncienne_equipe(tfAncienneEquipe.getText());
            transfert.setNouvelle_equipe(tfNouvelleEquipe.getText());
            transfert.setMontantt(Integer.parseInt(tfMontantt.getText()));
            transfert.setDatet(java.sql.Date.valueOf(dpDateT.getValue()));

            serviceTransfert.add(transfert);
            refreshTransfertsList();
            showAlert("Succès", "Transfert ajouté avec succès!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur dans les champs de saisie.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void modifierTransfert(ActionEvent event) {
        Transfert selectedTransfert = tableViewTransferts.getSelectionModel().getSelectedItem();

        if (selectedTransfert == null) {
            showAlert("Erreur", "Veuillez sélectionner un transfert à modifier.", Alert.AlertType.ERROR);
            return;
        }

        try {
            selectedTransfert.setIdu(Integer.parseInt(tfIdu.getText()));
            selectedTransfert.setAncienne_equipe(tfAncienneEquipe.getText());
            selectedTransfert.setNouvelle_equipe(tfNouvelleEquipe.getText());
            selectedTransfert.setMontantt(Integer.parseInt(tfMontantt.getText()));
            selectedTransfert.setDatet(java.sql.Date.valueOf(dpDateT.getValue()));

            serviceTransfert.update(selectedTransfert);
            refreshTransfertsList();
            showAlert("Succès", "Transfert modifié avec succès!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur dans la modification du transfert.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void supprimerTransfert(ActionEvent event) {
        Transfert selectedTransfert = tableViewTransferts.getSelectionModel().getSelectedItem();
        if (selectedTransfert == null) {
            showAlert("Erreur", "Veuillez sélectionner un transfert à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        serviceTransfert.delete(selectedTransfert);
        refreshTransfertsList();
        showAlert("Succès", "Transfert supprimé avec succès!", Alert.AlertType.INFORMATION);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}