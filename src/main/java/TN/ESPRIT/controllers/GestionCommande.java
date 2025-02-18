package TN.ESPRIT.controllers;

import TN.ESPRIT.models.Commande;
import TN.ESPRIT.services.ServiceCommande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class GestionCommande {

    private ServiceCommande serviceCommande = new ServiceCommande();

    @FXML
    private DatePicker tfDateCommande;

    @FXML
    private TextField tfMontantTotale;
    @FXML
    private TextField tfQuantite;
    @FXML
    private TextField tfIdProduit;
    @FXML
    private TextField tfId;
    @FXML
    private TableView<Commande> tableViewCommandes;
    @FXML
    private TableColumn<Commande, Integer> colId;
    @FXML
    private TableColumn<Commande, String> colDate;
    @FXML
    private TableColumn<Commande, Float> colMontant;
    @FXML
    private TableColumn<Commande, Integer> colQuantite;
    @FXML
    private TableColumn<Commande, Integer> colProduit;

    private ObservableList<Commande> commandesList = FXCollections.observableArrayList();

    // Initialisation de la liste des commandes
    @FXML
    public void initialize() {
        // Initialiser les colonnes de la TableView
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId_commande()).asObject());
        colDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate_commande().toString()));
        colMontant.setCellValueFactory(cellData -> new javafx.beans.property.SimpleFloatProperty(cellData.getValue().getMontant_totale()).asObject());
        colQuantite.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantite()).asObject());
        colProduit.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId_produit()).asObject());

        // Rafraîchir la liste des commandes
        refreshCommandesList();
    }

    // Rafraîchir la liste des commandes
    public void refreshCommandesList() {
        List<Commande> commandes = serviceCommande.getAll();
        commandesList.setAll(commandes); // Mettre à jour la liste
        tableViewCommandes.setItems(commandesList);
    }

    // Ajouter une commande
    @FXML
    private void ajouterCommande(ActionEvent event) {
        Commande commande = new Commande();
        try {
            // Tentative de conversion de la date
            commande.setDate_commande(java.sql.Date.valueOf(tfDateCommande.getValue()));
            commande.setMontant_totale(Float.parseFloat(tfMontantTotale.getText()));
            commande.setQuantite(Integer.parseInt(tfQuantite.getText()));
            commande.setId_produit(Integer.parseInt(tfIdProduit.getText()));
            commande.setId(Integer.parseInt(tfId.getText()));

            serviceCommande.add(commande);
            refreshCommandesList();
            showAlert("Succès", "Commande ajoutée avec succès!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur dans les champs de saisie.", Alert.AlertType.ERROR);
        }
    }

    // Modifier une commande
    @FXML
    private void modifierCommande(ActionEvent event) {
        Commande selectedCommande = tableViewCommandes.getSelectionModel().getSelectedItem();

        if (selectedCommande == null) {
            showAlert("Erreur", "Veuillez sélectionner une commande à modifier.", Alert.AlertType.ERROR);
            return;
        }

        try {
            selectedCommande.setDate_commande(java.sql.Date.valueOf(tfDateCommande.getValue()));
            selectedCommande.setMontant_totale(Float.parseFloat(tfMontantTotale.getText()));
            selectedCommande.setQuantite(Integer.parseInt(tfQuantite.getText()));
            selectedCommande.setId_produit(Integer.parseInt(tfIdProduit.getText()));
            selectedCommande.setId(Integer.parseInt(tfId.getText()));

            serviceCommande.update(selectedCommande);
            refreshCommandesList();
            showAlert("Succès", "Commande modifiée avec succès!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur dans la modification de la commande.", Alert.AlertType.ERROR);
        }
    }

    // Supprimer une commande
    @FXML
    private void supprimerCommande(ActionEvent event) {
        Commande selectedCommande = tableViewCommandes.getSelectionModel().getSelectedItem();
        if (selectedCommande == null) {
            showAlert("Erreur", "Veuillez sélectionner une commande à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        serviceCommande.delete(selectedCommande);
        refreshCommandesList();
        showAlert("Succès", "Commande supprimée avec succès!", Alert.AlertType.INFORMATION);
    }

    // Afficher une alerte
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
