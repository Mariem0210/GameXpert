package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

public class OffresDuJourController {

    @FXML
    private FlowPane offresContainer; // Conteneur pour les offres

    private CheapSharkAPI cheapSharkAPI = new CheapSharkAPI(); // Instance de l'API

    // Méthode pour afficher les offres
    public void afficherOffres(String jsonResponse) {
        Gson gson = new Gson();
        JsonArray deals = gson.fromJson(jsonResponse, JsonArray.class);

        if (deals == null || deals.size() == 0) {
            showAlert("Aucune offre trouvée", "Il n'y a aucune offre disponible pour le moment.", Alert.AlertType.INFORMATION);
            return;
        }

        for (int i = 0; i < deals.size(); i++) {
            JsonObject deal = deals.get(i).getAsJsonObject();

            // Extraire les informations du jeu
            String title = deal.has("title") ? deal.get("title").getAsString() : "Titre non disponible";
            String imageUrl = deal.has("thumb") ? deal.get("thumb").getAsString() : "";
            String salePrice = deal.has("salePrice") ? deal.get("salePrice").getAsString() : "N/A";
            String normalPrice = deal.has("normalPrice") ? deal.get("normalPrice").getAsString() : "N/A";
            String savings = deal.has("savings") ? String.format("%.2f", deal.get("savings").getAsDouble()) + " %" : "N/A";

            // Créer une carte d'offre
            AnchorPane offreCard = createOffreCard(title, imageUrl, salePrice, normalPrice, savings);
            offresContainer.getChildren().add(offreCard);
        }
    }

    // Méthode pour créer une carte d'offre
    private AnchorPane createOffreCard(String title, String imageUrl, String salePrice, String normalPrice, String savings) {
        AnchorPane card = new AnchorPane();
        card.setPrefSize(180, 220);
        card.setStyle("-fx-background-color: #2E3440; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Image du jeu
        ImageView imageView = new ImageView();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            imageView.setImage(image);
            imageView.setFitWidth(160);
            imageView.setFitHeight(120);
            imageView.setLayoutX(10);
            imageView.setLayoutY(10);
        }

        // Titre du jeu
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        titleLabel.setLayoutX(10);
        titleLabel.setLayoutY(140);

        // Prix promotionnel
        Label salePriceLabel = new Label("Prix promo: " + salePrice + " €");
        salePriceLabel.setStyle("-fx-text-fill: #88C0D0;");
        salePriceLabel.setLayoutX(10);
        salePriceLabel.setLayoutY(160);

        // Prix normal
        Label normalPriceLabel = new Label("Prix normal: " + normalPrice + " €");
        normalPriceLabel.setStyle("-fx-text-fill: #88C0D0;");
        normalPriceLabel.setLayoutX(10);
        normalPriceLabel.setLayoutY(180);

        // Économie
        Label savingsLabel = new Label("Économie: " + savings);
        savingsLabel.setStyle("-fx-text-fill: #A3BE8C;");
        savingsLabel.setLayoutX(10);
        savingsLabel.setLayoutY(200);

        // Ajouter les éléments à la carte
        card.getChildren().addAll(imageView, titleLabel, salePriceLabel, normalPriceLabel, savingsLabel);

        return card;
    }

    // Méthode pour fermer la fenêtre
    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) offresContainer.getScene().getWindow();
        stage.close();
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