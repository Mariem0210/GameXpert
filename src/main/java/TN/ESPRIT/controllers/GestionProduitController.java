package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import tn.esprit.models.Produit;
import tn.esprit.models.Panier;
import tn.esprit.services.ServiceProduit;
import tn.esprit.services.ServicePanier;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GestionProduitController {

    @FXML private TextField tfNom;
    @FXML private TextField tfDescription;
    @FXML private TextField tfPrix;
    @FXML private TextField tfStock;
    @FXML private TextField tfCategorie;
    @FXML private TextField tfImageProduit;
    @FXML private VBox productContainer;
    private final ServicePanier servicePanier = new ServicePanier();
    private final ServiceProduit serviceProduits = new ServiceProduit();
    private Produit selectedProduit = null;

    @FXML
    public void initialize() {
        afficherProduits();
    }

    @FXML
    public void ajouterProduit(ActionEvent actionEvent) {
        if (!validateFields()) {
            return;
        }
        try {
            Produit produit = new Produit(
                    tfNom.getText(),
                    tfDescription.getText(),
                    Float.parseFloat(tfPrix.getText()),
                    Integer.parseInt(tfStock.getText()),
                    new Date(), // Génération automatique de la date de création
                    tfCategorie.getText(),
                    tfImageProduit.getText()
            );
            serviceProduits.add(produit);
            afficherProduits();
            showAlert("Succès", "Produit ajouté avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void supprimerProduit(ActionEvent event) {
        if (selectedProduit == null) {
            showAlert("Erreur", "Veuillez sélectionner un produit à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce produit ?");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            serviceProduits.delete(selectedProduit);
            afficherProduits();
            showAlert("Succès", "Produit supprimé avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        }
    }
    @FXML
    public void ajouterAuPanier(ActionEvent actionEvent) {
        if (selectedProduit == null) {
            showAlert("Erreur", "Veuillez sélectionner un produit à ajouter au panier.", Alert.AlertType.ERROR);
            return;
        }
        Panier panier = new Panier(1, selectedProduit.getId_produit(), 1, new Date());
        servicePanier.add(panier);
        showAlert("Succès", "Produit ajouté au panier!", Alert.AlertType.INFORMATION);
    }
    @FXML
    private void ouvrirPanier(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Panier.fxml"));
            System.out.println("Panier.fxml chargé avec succès !");
            Parent root = loader.load();



            Stage stage = new Stage();
            stage.setTitle("Panier");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();


        }
    }
    @FXML
    private void modifierProduit(ActionEvent event) {
        if (selectedProduit == null) {
            showAlert("Erreur", "Veuillez sélectionner un produit à modifier.", Alert.AlertType.ERROR);
            return;
        }
        if (!validateFields()) {
            return;
        }
        try {
            selectedProduit.setNom(tfNom.getText());
            selectedProduit.setDescription(tfDescription.getText());
            selectedProduit.setPrix(Float.parseFloat(tfPrix.getText()));
            selectedProduit.setStock(Integer.parseInt(tfStock.getText()));
            selectedProduit.setCategorie(tfCategorie.getText());
            selectedProduit.setImage_produit(tfImageProduit.getText());

            serviceProduits.update(selectedProduit);

            afficherProduits();
            showAlert("Succès", "Produit modifié avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void afficherProduits() {
        productContainer.getChildren().clear();
        HBox currentRow = new HBox(10);
        currentRow.setAlignment(Pos.TOP_LEFT);
        int cardCount = 0;

        for (Produit produit : serviceProduits.getAll()) {
            VBox productCard = new VBox(10);
            productCard.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-width: 2px; -fx-padding: 20px; -fx-background-radius: 10px;");
            Label nameLabel = new Label("Nom: " + produit.getNom());
            Label priceLabel = new Label("Prix: " + produit.getPrix() + "€");
            Label stockLabel = new Label("Stock: " + produit.getStock());

            productCard.getChildren().addAll(nameLabel, priceLabel, stockLabel);
            productCard.setOnMouseClicked(event -> {
                selectedProduit = produit;
                remplirChamps(produit);
            });

            currentRow.getChildren().add(productCard);
            cardCount++;

            if (cardCount >= 4) {
                productContainer.getChildren().add(currentRow);
                currentRow = new HBox(10);
                currentRow.setAlignment(Pos.TOP_LEFT);
                cardCount = 0;
            }
        }
        if (cardCount > 0) {
            productContainer.getChildren().add(currentRow);
        }
    }

    private boolean validateFields() {
        if (tfNom.getText().isEmpty() || tfDescription.getText().isEmpty() ||
                tfPrix.getText().isEmpty() || tfStock.getText().isEmpty() || tfCategorie.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void remplirChamps(Produit p) {
        tfNom.setText(p.getNom());
        tfDescription.setText(p.getDescription());
        tfPrix.setText(String.valueOf(p.getPrix()));
        tfStock.setText(String.valueOf(p.getStock()));
        tfCategorie.setText(p.getCategorie());
        tfImageProduit.setText(p.getImage_produit());
    }

    private void clearFields() {
        tfNom.clear();
        tfDescription.clear();
        tfPrix.clear();
        tfStock.clear();
        tfCategorie.clear();
        tfImageProduit.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
