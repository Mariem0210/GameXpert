package tn.esprit.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import tn.esprit.models.Panier;
import tn.esprit.models.Produit;
import tn.esprit.models.Commande;
import tn.esprit.services.ServicePanier;
import tn.esprit.services.ServiceCommande;

import java.util.Date;
import java.util.List;

public class PanierController {

    @FXML
    private TableView<Panier> tablePanier;

    @FXML
    private TableColumn<Panier, String> colProduit;

    @FXML
    private TableColumn<Panier, String> colQuantite; // Changé en String pour afficher les boutons

    @FXML
    private TableColumn<Panier, String> colDateAjout;

    private final ServiceCommande serviceCommande = new ServiceCommande();
    private final ServicePanier servicePanier = new ServicePanier();

    @FXML
    public void initialize() {
        System.out.println("PanierController - initialize() exécuté !");

        // Affichage du nom du produit au lieu de l'ID
        colProduit.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduit().getNom()));

        // Configuration de la colonne "Quantité" pour afficher les boutons et la quantité
        colQuantite.setCellFactory(param -> new TableCell<Panier, String>() {
            private final Button incrementButton = new Button("+");
            private final Button decrementButton = new Button("-");
            private final Button deleteButton = new Button("🗑️"); // Bouton de suppression
            private final Label quantityLabel = new Label();

            {
                // Style des boutons + et -
                incrementButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                decrementButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                // Style du bouton de suppression (rouge)
                deleteButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");

                // Action pour le bouton d'incrémentation
                incrementButton.setOnAction(event -> {
                    Panier panier = getTableRow().getItem();
                    if (panier != null) {
                        panier.setQuantite(panier.getQuantite() + 1); // Incrémenter la quantité
                        servicePanier.update(panier); // Mettre à jour dans la base de données
                        afficherPanier(); // Rafraîchir l'affichage
                    }
                });

                // Action pour le bouton de décrémentation
                decrementButton.setOnAction(event -> {
                    Panier panier = getTableRow().getItem();
                    if (panier != null) {
                        if (panier.getQuantite() > 1) {
                            panier.setQuantite(panier.getQuantite() - 1); // Décrémenter la quantité
                            servicePanier.update(panier); // Mettre à jour dans la base de données
                            afficherPanier(); // Rafraîchir l'affichage
                        } else {
                            showAlert("Attention", "La quantité ne peut pas être inférieure à 1.", Alert.AlertType.WARNING);
                        }
                    }
                });

                // Action pour le bouton de suppression
                deleteButton.setOnAction(event -> {
                    Panier panier = getTableRow().getItem();
                    if (panier != null) {
                        supprimerDuPanier(panier);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Panier panier = getTableRow().getItem();
                    quantityLabel.setText(String.valueOf(panier.getQuantite()));

                    // Organisation des boutons et de la quantité dans un HBox
                    HBox hbox = new HBox(decrementButton, quantityLabel, incrementButton, deleteButton);
                    hbox.setSpacing(10); // Espacement entre les éléments
                    setGraphic(hbox);
                }
            }
        });

        // Affichage de la date d'ajout sous forme de String
        colDateAjout.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate_ajout().toString()));

        afficherPanier();
    }

    // Méthode pour afficher les produits du panier
    @FXML
    private void afficherPanier() {
        tablePanier.getItems().clear();
        List<Panier> panierList = servicePanier.getAll();
        tablePanier.getItems().addAll(panierList);
    }

    // Méthode pour ajouter un produit au panier
    @FXML
    public void ajouterAuPanier(Produit produit) {
        Panier panier = new Panier(1, produit.getId_produit(), 1, new Date());
        servicePanier.add(panier);
        afficherPanier();
        showAlert("Succès", "Produit ajouté au panier!", Alert.AlertType.INFORMATION);
    }

    // Méthode pour supprimer un produit du panier
    private void supprimerDuPanier(Panier item) {
        servicePanier.delete(item);
        afficherPanier();
        showAlert("Succès", "Produit supprimé du panier!", Alert.AlertType.INFORMATION);
    }

    // Méthode pour valider le panier
    @FXML
    public void validerPanier() {
        System.out.println("Validation du panier en cours...");

        // Calculer le montant total du panier
        float montantTotal = calculerMontantTotal();

        // Ajouter la commande
        Commande commande = new Commande(new Date(), montantTotal, 1);  // 1 est l'ID de l'utilisateur
        serviceCommande.add(commande);

        // Supprimer tous les produits du panier
        supprimerTousLesProduitsDuPanier();

        showAlert("Succès", "Votre commande a été validée et le panier vidé!", Alert.AlertType.INFORMATION);
    }

    private float calculerMontantTotal() {
        List<Panier> panierList = servicePanier.getAll();
        float total = 0;
        for (Panier panier : panierList) {
            total += (panier.getQuantite() * panier.getProduit().getPrix());
        }
        return total;
    }

    private void supprimerTousLesProduitsDuPanier() {
        List<Panier> panierList = servicePanier.getAll();
        for (Panier panier : panierList) {
            servicePanier.delete(panier);
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}