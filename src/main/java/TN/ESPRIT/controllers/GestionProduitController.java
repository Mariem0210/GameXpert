package tn.esprit.controllers;
import javafx.scene.control.ComboBox;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import tn.esprit.models.Produit;
import tn.esprit.services.ServiceProduit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GestionProduitController {

    @FXML private TextField tfNom;
    @FXML private TextField tfDescription;
    @FXML private TextField tfPrix;
    @FXML private TextField tfStock;
    @FXML private TextField tfCategorie;
    @FXML private TextField tfImageProduit;
    @FXML private TextField tfRecherche;
    @FXML private VBox productContainer;
    @FXML private ComboBox<String> cbTri;

    private final ServicePanier servicePanier = new ServicePanier();
    private final ServiceProduit serviceProduits = new ServiceProduit();
    private Produit selectedProduit = null;

    @FXML
    public void initialize() {
        afficherProduits(serviceProduits.getAll());
    }
    @FXML
    private void trierProduits(ActionEvent event) {
        String critere = cbTri.getValue();
        List<Produit> produitsTries = new ArrayList<>(serviceProduits.getAll());

        switch (critere) {
            case "Nom":
                produitsTries.sort(Comparator.comparing(Produit::getNom));
                break;
            case "Catégorie":
                produitsTries.sort(Comparator.comparing(Produit::getCategorie));
                break;
            case "Prix":
                produitsTries.sort(Comparator.comparingDouble(Produit::getPrix));
                break;
            case "Stock":
                produitsTries.sort(Comparator.comparingInt(Produit::getStock));
                break;
        }

        afficherProduits(produitsTries);
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
            afficherProduits(serviceProduits.getAll());
            showAlert("Succès", "Produit ajouté avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void rechercherProduit(String query) {
        List<Produit> produitsFiltres = serviceProduits.getAll().stream()
                .filter(p -> p.getNom().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        afficherProduits(produitsFiltres);
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
            afficherProduits(serviceProduits.getAll());
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
            // Charger la vue du panier depuis le fichier Panier.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Panier.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre (stage) pour afficher le panier
            Stage stage = new Stage();
            stage.setTitle("Panier"); // Titre de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre du panier
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le panier.", Alert.AlertType.ERROR);
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

            afficherProduits(serviceProduits.getAll());
            showAlert("Succès", "Produit modifié avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void handleRecherche() {
        String query = tfRecherche.getText();
        rechercherProduit(query);
    }
    private boolean validateFields() {
        if (tfNom.getText().isEmpty() || tfDescription.getText().isEmpty() ||
                tfPrix.getText().isEmpty() || tfStock.getText().isEmpty() ||
                tfCategorie.getText().isEmpty() || tfImageProduit.getText().isEmpty()) {

            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            Float.parseFloat(tfPrix.getText());
            Integer.parseInt(tfStock.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Prix et Stock doivent être des nombres valides.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    @FXML

    public void afficherProduits(List<Produit> produits) {
        productContainer.getChildren().clear();
        HBox currentRow = new HBox(20);
        currentRow.setStyle("-fx-background-color: transparent;");
        currentRow.setAlignment(Pos.TOP_LEFT);
        int cardCount = 0;

        for (Produit produit : produits) {
            VBox productCard = createProductCard(produit);

            currentRow.getChildren().add(productCard);
            cardCount++;

            if (cardCount >= 4) {
                productContainer.getChildren().add(currentRow);
                currentRow = new HBox(20);
                currentRow.setStyle("-fx-background-color: transparent;");
                currentRow.setAlignment(Pos.TOP_LEFT);
                cardCount = 0;
            }
        }

        if (cardCount > 0) {
            productContainer.getChildren().add(currentRow);
        }
    }

    private VBox createProductCard(Produit produit) {
        VBox productCard = new VBox(10);
        productCard.getStyleClass().add("product-card");
        productCard.setStyle(
                "-fx-background-color: #473E66;" +
                        "-fx-border-color: #6B5B95;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 15px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);"
        );
        productCard.setPrefWidth(180);
        productCard.setPrefHeight(220);
        productCard.setAlignment(Pos.CENTER);

        // Product Name
        Label nameLabel = new Label(produit.getNom());
        nameLabel.setStyle(
                "-fx-text-fill: #F0F0F0;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;"
        );

        // Price Label
        Label priceLabel = new Label(String.format("%.2f €", produit.getPrix()));
        priceLabel.setStyle(
                "-fx-text-fill: #FF6B6B;" +
                        "-fx-font-size: 14px;"
        );

        // Stock Label
        Label stockLabel = new Label("Stock: " + produit.getStock());
        stockLabel.setStyle(
                "-fx-text-fill: #9B8194;" +
                        "-fx-font-size: 12px;"
        );

        // Hover and Click Effects
        productCard.setOnMouseEntered(event -> {
            productCard.setStyle(
                    "-fx-background-color: #6B5B95;" +
                            "-fx-border-color: #FF6B6B;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 10px;" +
                            "-fx-background-radius: 10px;" +
                            "-fx-padding: 15px;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(255,107,107,0.5), 10, 0, 0, 2);"
            );
        });

        productCard.setOnMouseExited(event -> {
            productCard.setStyle(
                    "-fx-background-color: #473E66;" +
                            "-fx-border-color: #6B5B95;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 10px;" +
                            "-fx-background-radius: 10px;" +
                            "-fx-padding: 15px;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);"
            );
        });

        productCard.setOnMouseClicked(event -> {
            selectedProduit = produit;
            remplirChamps(produit);

            // Visual feedback on selection
            productCard.setStyle(
                    "-fx-background-color: #FF6B6B;" +
                            "-fx-border-color: #1E1A2F;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 10px;" +
                            "-fx-background-radius: 10px;" +
                            "-fx-padding: 15px;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 2);"
            );
        });

        productCard.getChildren().addAll(nameLabel, priceLabel, stockLabel);

        return productCard;
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