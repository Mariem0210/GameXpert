package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Giveaway;
import tn.esprit.models.Giveaway;
import tn.esprit.interfaces.GiveawayService;

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
                VBox giveawayCard = new VBox(5);
                giveawayCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f4f4f4;");
                giveawayCard.setPrefSize(200, 250);

                // Éléments de la carte
                Label titreLabel = new Label("Titre: " + giveaway.getTitreg());
                Label descriptionLabel = new Label("Description: " + giveaway.getDescg());
                Label dateDebutLabel = new Label("Date Début: " + giveaway.getDatedg());
                Label dateFinLabel = new Label("Date Fin: " + giveaway.getDatefg());
                Label statusLabel = new Label("Statut: " + giveaway.getStatusg());

                // Boutons
                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> deleteGiveaway(giveaway));

                Button modifButton = new Button("Modifier");
                modifButton.setStyle("-fx-background-color: #6999d0; -fx-text-fill: white;");
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

