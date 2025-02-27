package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import tn.esprit.controllers.PanierController;
import tn.esprit.models.Commande;
import tn.esprit.services.ServiceCommande;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;

import java.util.List;

public class CommandeController {

    @FXML
    private TableView<Commande> tableCommandes;
    @FXML
    private TableColumn<Commande, Integer> colIdCommande;
    @FXML
    private TableColumn<Commande, String> colDateCommande;
    @FXML
    private TableColumn<Commande, Float> colMontantTotal;
    @FXML
    private TableColumn<Commande, Integer> colUtilisateur;
    @FXML
    private WebView webView;

    private final ServiceCommande serviceCommande = new ServiceCommande();

    @FXML
    public void initialize() {
        // Liaison des colonnes avec les attributs du modèle Commande
        colIdCommande.setCellValueFactory(new PropertyValueFactory<>("id_commande"));
        colDateCommande.setCellValueFactory(new PropertyValueFactory<>("date_commande"));
        colMontantTotal.setCellValueFactory(new PropertyValueFactory<>("montant_total"));
        colUtilisateur.setCellValueFactory(new PropertyValueFactory<>("id_utilisateur"));
        webView.getEngine().load(getClass().getResource("/paymentPage.html").toExternalForm());
        afficherCommandes();
    }

    @FXML
    private void afficherCommandes() {
        tableCommandes.getItems().clear();
        List<Commande> commandesList = serviceCommande.getAll();
        tableCommandes.getItems().addAll(commandesList);
    }

    @FXML
    public void ajouterCommande() {
        Commande commande = new Commande(new java.util.Date(), 100.0f, 1);  // Exemple d'ajout
        serviceCommande.add(commande);
        afficherCommandes();
        showAlert("Succès", "Commande ajoutée!", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void supprimerCommande() {
        Commande commande = tableCommandes.getSelectionModel().getSelectedItem();
        if (commande != null) {
            serviceCommande.delete(commande);
            afficherCommandes();
            showAlert("Succès", "Commande supprimée!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Aucune commande sélectionnée", Alert.AlertType.ERROR);
        }
    }
    @FXML
    public void handlePayerCommande(ActionEvent event) {
        // Vérifier si la commande est prête à être payée
        Commande commande = tableCommandes.getSelectionModel().getSelectedItem();
        if (commande != null) {
            // Appeler StripeController pour initier le paiement
            StripeController stripeController = new StripeController();
            stripeController.processPayment();  // Lance le processus de paiement via Stripe

            showAlert("Paiement", "Le paiement a été traité.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Aucune commande sélectionnée pour le paiement", Alert.AlertType.ERROR);
        }
    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
