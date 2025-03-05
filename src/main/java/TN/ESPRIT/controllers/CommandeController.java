package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.models.Commande;
import tn.esprit.services.ServiceCommande;

import java.io.IOException;
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

    private final ServiceCommande serviceCommande = new ServiceCommande();

    @FXML
    public void initialize() {
        // Liaison des colonnes avec les attributs du modèle Commande
        colIdCommande.setCellValueFactory(new PropertyValueFactory<>("id_commande"));
        colDateCommande.setCellValueFactory(new PropertyValueFactory<>("date_commande"));
        colMontantTotal.setCellValueFactory(new PropertyValueFactory<>("montant_total"));
        colUtilisateur.setCellValueFactory(new PropertyValueFactory<>("id_utilisateur"));
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
        Commande commande = tableCommandes.getSelectionModel().getSelectedItem();
        if (commande != null) {
            // Rediriger vers la scène de paiement Stripe
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaiementStripe.fxml"));
                Parent root = loader.load();
                StripeController stripeController = loader.getController();
                stripeController.setMontant(commande.getMontant_total()); // Passer le montant de la commande

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Paiement avec Stripe");
                stage.show();
            } catch (IOException e) {
                showAlert("Erreur", "Impossible de charger la page de paiement.", Alert.AlertType.ERROR);
            }
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