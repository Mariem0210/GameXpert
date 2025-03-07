package tn.esprit.controllers;

import javafx.geometry.Pos;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;
import javafx.geometry.Insets;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Tournoi;
import tn.esprit.services.ServiceTournoi;

import java.io.IOException;
import java.util.List;

public class TournoiFront {

    @FXML private VBox cardContainer;
    private final IService<Tournoi> st = new ServiceTournoi();

    @FXML
    public void initialize() {
        refreshTournoisList();
    }

    @FXML
    public void onRefreshClick() {
        refreshTournoisList();
    }

    private void refreshTournoisList() {
        cardContainer.getChildren().clear();

        for (Tournoi t : st.getAll()) {
            // Create the tournament card with the cyberpunk/gaming theme
            StackPane card = createTournamentCard(t);
            cardContainer.getChildren().add(card);
        }
    }

    private StackPane createTournamentCard(Tournoi t) {
        // Main card container with background image
        StackPane cardStack = new StackPane();
        cardStack.getStyleClass().add("team-card");
        cardStack.setPrefWidth(1100);

        // Background image
        ImageView backgroundImage = new ImageView();
        backgroundImage.setFitWidth(1100);
        backgroundImage.setFitHeight(200);
        try {
            Image image = new Image("lol.jpg");
            backgroundImage.setImage(image);
            backgroundImage.setOpacity(0.3); // Partially transparent
            backgroundImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Erreur de chargement de l'image: " + e.getMessage());
        }

        // Content container
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.CENTER_LEFT);

        // Header with tournament name
        Label nameLabel = new Label(t.getNomt());
        nameLabel.getStyleClass().add("team-name");

        // Tournament details in a grid format
        HBox detailsBox = new HBox(30);
        detailsBox.setAlignment(Pos.CENTER_LEFT);

        // Left column - dates and teams
        VBox leftDetails = new VBox(5);
        Label startDateLabel = new Label("Début: " + t.getDate_debutt());
        startDateLabel.getStyleClass().add("team-info");
        Label endDateLabel = new Label("Fin: " + t.getDate_fint());
        endDateLabel.getStyleClass().add("team-info");
        Label teamsLabel = new Label("Équipes: " + t.getNbr_equipes());
        teamsLabel.getStyleClass().add("team-info");
        leftDetails.getChildren().addAll(startDateLabel, endDateLabel, teamsLabel);

        // Middle column - description
        VBox middleDetails = new VBox(5);
        Label descriptionLabel = new Label("Description: " + t.getDescriptiont());
        descriptionLabel.getStyleClass().add("team-info");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setPrefWidth(600);
        middleDetails.getChildren().add(descriptionLabel);

        // Right column - price and status
        VBox rightDetails = new VBox(5);
        Label priceLabel = new Label("Prix: " + t.getPrixt());
        priceLabel.getStyleClass().add("team-info");
        Label statusLabel = new Label("Statut: " + t.getStatutt());
        statusLabel.getStyleClass().add("team-info");
        // Style the status label based on tournament status
        if (t.getStatutt().equalsIgnoreCase("En cours")) {
            statusLabel.setTextFill(Color.GREEN);
        } else if (t.getStatutt().equalsIgnoreCase("Terminé")) {
            statusLabel.setTextFill(Color.RED);
        } else {
            statusLabel.setTextFill(Color.YELLOW);
        }
        rightDetails.getChildren().addAll(priceLabel, statusLabel);

        // Add all columns to the details box
        detailsBox.getChildren().addAll(leftDetails, middleDetails, rightDetails);

        // Add everything to the content container
        content.getChildren().addAll(nameLabel, detailsBox);

        // Add background and content to the stack
        cardStack.getChildren().addAll(backgroundImage, content);

        // Add hover effects
        cardStack.setOnMouseEntered(e -> {
            cardStack.setCursor(Cursor.HAND);
            cardStack.setStyle("-fx-background-color: rgba(50, 0, 75, 0.8);");
            DropShadow glow = new DropShadow();
            glow.setColor(Color.valueOf("#b14fff"));
            glow.setWidth(20);
            glow.setHeight(20);
            cardStack.setEffect(glow);
        });

        cardStack.setOnMouseExited(e -> {
            cardStack.setStyle(null);
            cardStack.getStyleClass().add("team-card");
            cardStack.setEffect(null);
        });

        // Set double-click event to open match management
        cardStack.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ouvrirGestionMatch(t);
            }
        });

        return cardStack;
    }

    private void ouvrirGestionMatch(Tournoi tournoi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchFront.fxml"));
            Parent root = loader.load();

            MatchFront controller = loader.getController();
            controller.setTournoi(tournoi);

            Stage stage = new Stage();
            stage.setTitle("Gestion des Matchs - " + tournoi.getNomt());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface GestionMatch.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}