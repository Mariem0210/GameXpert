package tn.esprit.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.models.Produit;
import tn.esprit.services.ServiceProduit;
import tn.esprit.services.ServicePanier;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import tn.esprit.models.Panier;

import java.util.Date;

public class ClientInterfaceController {

    @FXML private TextField tfRecherche;
    @FXML private VBox productContainer;
    @FXML
    private TextArea taChat;
    private final ServiceProduit serviceProduits = new ServiceProduit();
    private final ServicePanier servicePanier = new ServicePanier();
    private final CheapSharkAPI cheapSharkAPI = new CheapSharkAPI();
    @FXML
    public void initialize() {
        afficherProduits(serviceProduits.getAll());
    }
    @FXML
    private void ouvrirChatIA(ActionEvent event) {
        try {
            // Charger la vue du chat IA depuis le fichier ChatIA.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatIA.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur du chat IA
            ChatIAController chatIAController = loader.getController();

            // Passer la liste des produits au contrôleur du chat IA
            chatIAController.setProduits(serviceProduits.getAll());

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre (stage) pour afficher le chat IA
            Stage stage = new Stage();
            stage.setTitle("Chat IA"); // Titre de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre du chat IA
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le chat IA.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void rechercherOffres() {
        try {
            // Récupérer les offres de l'API
            String reponseAPI = cheapSharkAPI.searchDeals("");

            // Charger la nouvelle page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OffresDuJour.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la nouvelle page
            OffresDuJourController offresController = loader.getController();
            offresController.afficherOffres(reponseAPI); // Afficher les offres

            // Créer une nouvelle scène
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Offres du Jour");
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir les offres du jour.", Alert.AlertType.ERROR);
        }
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
    private void displayDeals(String jsonResponse) {
        System.out.println("Réponse JSON reçue : " + jsonResponse); // Log la réponse JSON
        Gson gson = new Gson();
        JsonArray deals = gson.fromJson(jsonResponse, JsonArray.class);

        if (deals == null || deals.size() == 0) {
            taChat.appendText("Aucune offre trouvée.\n");
            return;
        }

        for (int i = 0; i < deals.size(); i++) {
            JsonObject deal = deals.get(i).getAsJsonObject();
            String title = deal.has("title") ? deal.get("title").getAsString() : "Titre non disponible";
            String salePrice = deal.has("salePrice") ? deal.get("salePrice").getAsString() : "N/A";
            String normalPrice = deal.has("normalPrice") ? deal.get("normalPrice").getAsString() : "N/A";
            String savings = deal.has("savings") ? String.format("%.2f", deal.get("savings").getAsDouble()) + " %" : "N/A";

            taChat.appendText("Titre : " + title + "\n");
            taChat.appendText("Prix promotionnel : " + salePrice + " €\n");
            taChat.appendText("Prix normal : " + normalPrice + " €\n");
            taChat.appendText("Économie : " + savings + "\n");
            taChat.appendText("-----------------------------\n");
        }
    }

    @FXML
    private void handleRecherche() {
        String query = tfRecherche.getText();
        rechercherProduit(query);
    }

    @FXML
    private void rechercherProduit(String query) {
        List<Produit> produitsFiltres = serviceProduits.getAll().stream()
                .filter(p -> p.getNom().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        afficherProduits(produitsFiltres);
    }

    @FXML
    private void afficherProduits(List<Produit> produits) {
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

        // Product Image
        ImageView imageView = new ImageView();
        if (produit.getImage_produit() != null && !produit.getImage_produit().isEmpty()) {
            Image image = new Image(produit.getImage_produit());
            imageView.setImage(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
        }

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

        // Quantity Selector
        HBox quantityBox = new HBox(10);
        quantityBox.setAlignment(Pos.CENTER);

        Button btnDecrease = new Button("-");
        btnDecrease.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white;");

        Label quantityLabel = new Label("1");
        quantityLabel.setStyle("-fx-text-fill: white;");

        Button btnIncrease = new Button("+");
        btnIncrease.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white;");

        btnDecrease.setOnAction(event -> {
            int quantity = Integer.parseInt(quantityLabel.getText());
            if (quantity > 1) {
                quantityLabel.setText(String.valueOf(quantity - 1));
            }
        });

        btnIncrease.setOnAction(event -> {
            int quantity = Integer.parseInt(quantityLabel.getText());
            quantityLabel.setText(String.valueOf(quantity + 1));
        });

        quantityBox.getChildren().addAll(btnDecrease, quantityLabel, btnIncrease);

        // Add to Cart Button
        Button btnAddToCart = new Button("Ajouter au panier");
        btnAddToCart.setStyle("-fx-background-color: #6B5B95; -fx-text-fill: white;");
        btnAddToCart.setOnAction(event -> {
            int quantity = Integer.parseInt(quantityLabel.getText());
            // Ajouter le produit au panier avec la quantité sélectionnée
            servicePanier.add(new Panier(1, produit.getId_produit(), quantity, new Date()));
            showAlert("Succès", "Produit ajouté au panier!", Alert.AlertType.INFORMATION);
        });

        productCard.getChildren().addAll(imageView, nameLabel, priceLabel, quantityBox, btnAddToCart);

        return productCard;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}