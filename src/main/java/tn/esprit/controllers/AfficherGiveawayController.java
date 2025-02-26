package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Giveaway;
import tn.esprit.models.Giveaway;
import tn.esprit.interfaces.GiveawayService;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherGiveawayController {
    @FXML
    private TilePane giveawayTilePane;

    private GiveawayService giveawayService = new GiveawayService();

    @FXML
    public void initialize() {
        loadGiveaways();
    }

    @FXML
    public void loadGiveaways() {
        giveawayTilePane.getChildren().clear();
        giveawayTilePane.setHgap(50);
        giveawayTilePane.setVgap(120);

        try {
            List<Giveaway> giveaways = giveawayService.recupererGiveaways();

            for (Giveaway giveaway : giveaways) {
                VBox giveawayCard = new VBox(10);

                // Chargement de l'image depuis resources
                javafx.scene.image.Image backgroundImage = new Image(getClass().getResource("/ground.jpg").toExternalForm());
                BackgroundImage background = new BackgroundImage(
                        backgroundImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, false, true)
                );

                giveawayCard.setBackground(new Background(background)); // Appliquer l'image de fond

                giveawayCard.setStyle("-fx-border-radius: 10; -fx-border-color: #8a2be2; -fx-padding: 15px;");
                giveawayCard.setPrefSize(280, 320);

                // Éléments de la carte
                Label titreLabel = new Label("Titre: " + giveaway.getTitreg());
                Label descriptionLabel = new Label("Description: " + giveaway.getDescg());
                Label dateDebutLabel = new Label("Date Début: " + giveaway.getDatedg());
                Label dateFinLabel = new Label("Date Fin: " + giveaway.getDatefg());
                Label statusLabel = new Label("Statut: " + giveaway.getStatusg());

                // Bouton Supprimer (Effet Rouge Néon)
                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #ff4e50, #c0392b);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(gaussian, rgba(255, 0, 0, 0.7), 10, 0.5, 0, 0);" +
                                "-fx-cursor: hand;"
                );
                deleteButton.setOnMouseEntered(e -> deleteButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #e74c3c, #c0392b);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(gaussian, rgba(255, 0, 0, 1), 15, 0.7, 0, 0);" +
                                "-fx-cursor: hand;" +
                                "-fx-scale-x: 1.1;" +
                                "-fx-scale-y: 1.1;"
                ));
                deleteButton.setOnMouseExited(e -> deleteButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #ff4e50, #c0392b);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(gaussian, rgba(255, 0, 0, 0.7), 10, 0.5, 0, 0);" +
                                "-fx-cursor: hand;"
                ));
                deleteButton.setOnAction(event -> deleteGiveaway(giveaway)); // Méthode pour supprimer le giveaway

// Bouton Modifier (Effet Bleu Néon)
                Button modifButton = new Button("Modifier");
                modifButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #74ebd5, #6999d0);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.7), 10, 0.5, 0, 0);" +
                                "-fx-cursor: hand;"
                );
                modifButton.setOnMouseEntered(e -> modifButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #56ccf2, #2f80ed);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 1), 15, 0.7, 0, 0);" +
                                "-fx-cursor: hand;" +
                                "-fx-scale-x: 1.1;" +
                                "-fx-scale-y: 1.1;"
                ));
                modifButton.setOnMouseExited(e -> modifButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #74ebd5, #6999d0);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.7), 10, 0.5, 0, 0);" +
                                "-fx-cursor: hand;"
                ));
                modifButton.setOnAction(event -> {
                            if (giveaway.getTitreg() == null || giveaway.getTitreg().isEmpty()) {
                                showAlert(Alert.AlertType.ERROR, "Erreur", "Le titre du giveaway est vide.");
                                return;
                            }

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de modification");
                    alert.setHeaderText(null);
                    alert.setContentText("Voulez-vous vraiment modifier ce giveaway ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierGiveaway.fxml"));
                            Scene scene = new Scene(loader.load());

                            ModifierGiveawayController controller = loader.getController();
                            controller.initData(giveaway);

                            controller.setOnUpdateSuccess(() -> loadGiveaways());

                            Stage stage = new Stage();
                            stage.setTitle("Modifier Giveaway");
                            stage.setScene(scene);
                            stage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire de modification.");
                        }
                    }
                });

                // Ajouter les éléments à la carte
                giveawayCard.getChildren().addAll(
                        titreLabel, descriptionLabel, dateDebutLabel,
                        dateFinLabel, statusLabel, deleteButton, modifButton
                );

                giveawayTilePane.getChildren().add(giveawayCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les giveaways.");
        }
    }

    private void deleteGiveaway(Giveaway giveaway) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette formation ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    giveawayService.supprimerGiveaway(giveaway, giveaway.getTitreg());
                    loadGiveaways();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer la formation.");
                }
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
    }

