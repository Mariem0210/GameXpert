package tn.esprit.controllers;
import javafx.beans.property.SimpleStringProperty;

import tn.esprit.models.Panier;
import tn.esprit.models.Produit;
import tn.esprit.models.Commande;
import tn.esprit.services.ServicePanier;
import tn.esprit.services.ServiceCommande;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import java.util.List;
import java.util.Date;

public class PanierController {

    @FXML
    private TableView<Panier> tablePanier;
    @FXML
    private TableColumn<Panier, Integer> colUtilisateur;
    @FXML
    private TableColumn<Panier, String> colProduit; // Changer le type de la colonne pour String (Nom du produit)
    @FXML
    private TableColumn<Panier, Integer> colQuantite;
    @FXML
    private TableColumn<Panier, String> colDateAjout;
    @FXML
    private TableColumn<Panier, String> colActions; // Colonne pour les actions (supprimer, modifier)

    private final ServiceCommande serviceCommande = new ServiceCommande();

    private final ServicePanier servicePanier = new ServicePanier();


    @FXML
    public void initialize() {
        System.out.println("PanierController - initialize() exécuté !");

        // Liaison des colonnes avec les attributs du modèle Panier
        colUtilisateur.setCellValueFactory(new PropertyValueFactory<>("id_utilisateur"));

        // Affichage du nom du produit au lieu de l'ID
        colProduit.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduit().getNom()));  // Accéder au nom du produit

        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        // Affichage de la date d'ajout sous forme de String
        colDateAjout.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate_ajout().toString()));

        // Configuration de la colonne "Actions" pour afficher les boutons
        colActions.setCellFactory(param -> new TableCell<Panier, String>() {
            private final Button deleteButton = new Button("Supprimer");
            private final Button modifyButton = new Button("Modifier");

            {
                // Action pour le bouton de suppression
                deleteButton.setOnAction(event -> {
                    Panier panier = getTableRow().getItem();
                    if (panier != null) {
                        supprimerDuPanier(panier);
                    }
                });

                // Action pour le bouton de modification
                modifyButton.setOnAction(event -> {
                    Panier panier = getTableRow().getItem();
                    if (panier != null) {
                        showModifyDialog(panier);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    FlowPane flowPane = new FlowPane(deleteButton, modifyButton);
                    setGraphic(flowPane);
                }
            }
        });

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
            total += (panier.getQuantite() * panier.getProduit().getPrix() ) ;// Montant total = quantité * prix du produit
        }
        return total;
    }

    private void supprimerTousLesProduitsDuPanier() {
        List<Panier> panierList = servicePanier.getAll();
        for (Panier panier : panierList) {
            servicePanier.delete(panier);  // Supprimer chaque produit du panier
        }
    }


    // Méthode pour afficher un dialog de modification de la quantité
    private void showModifyDialog(Panier panier) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(panier.getQuantite()));
        dialog.setTitle("Modifier Quantité");
        dialog.setHeaderText("Modifiez la quantité du produit");
        dialog.setContentText("Quantité :");
        dialog.showAndWait().ifPresent(input -> {
            try {
                int newQuantity = Integer.parseInt(input);
                panier.setQuantite(newQuantity);  // Mise à jour de la quantité
                servicePanier.update(panier);     // Mise à jour dans la base de données
                afficherPanier();  // Rafraîchissement de l'affichage
                showAlert("Succès", "Quantité mise à jour!", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer un nombre valide!", Alert.AlertType.ERROR);
            }
        });
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
