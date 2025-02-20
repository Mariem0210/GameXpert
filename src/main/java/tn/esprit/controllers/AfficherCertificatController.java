package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Certificat;
import tn.esprit.interfaces.CertificatService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherCertificatController {
    @FXML
    private TilePane certificatTilePane;

    private CertificatService certificatService = new CertificatService();

    @FXML
    public void initialize() {
        loadCertificats();
    }

    @FXML
    public void loadCertificats() {
        certificatTilePane.getChildren().clear();
        certificatTilePane.setHgap(50);
        certificatTilePane.setVgap(120);

        try {
            List<Certificat> certificats = certificatService.recupererCertificats();

            for (Certificat certificat : certificats) {
                VBox certificatCard = new VBox(5);
                certificatCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #1e1e2e;");
                certificatCard.setPrefSize(200, 250);

                // Éléments de la carte
                Label nomcLabel = new Label("Nom: " + certificat.getNomc());
                Label typecLabel = new Label("Type: " + certificat.getTypec());
                Label scorecLabel = new Label("Score: " + certificat.getScorec());
                Label etatcLabel = new Label("État: " + certificat.getEtatc());
                Label dateExpirationcLabel = new Label("Expiration: " + certificat.getDateExpirationc());

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
                deleteButton.setOnAction(event -> deleteCertificat(certificat)); // Méthode pour supprimer le certificat

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
                    if (certificat.getNomc() == null || certificat.getNomc().isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom du certificat est vide.");
                        return;
                    }

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de modification");
                    alert.setHeaderText(null);
                    alert.setContentText("Voulez-vous vraiment modifier ce certificat ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCertificat.fxml"));
                            Scene scene = new Scene(loader.load());

                            ModifierCertificatController controller = loader.getController();
                            controller.initData(certificat);

                            controller.setOnUpdateSuccess(() -> loadCertificats());

                            Stage stage = new Stage();
                            stage.setTitle("Modifier Certificat");
                            stage.setScene(scene);
                            stage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire de modification.");
                        }
                    }
                });

                // Ajouter les éléments à la carte
                certificatCard.getChildren().addAll(
                        nomcLabel, typecLabel, scorecLabel,
                        etatcLabel, dateExpirationcLabel, deleteButton, modifButton
                );

                certificatTilePane.getChildren().add(certificatCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les certificats.");
        }
    }

    private void deleteCertificat(Certificat certificat) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce certificat ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    certificatService.supprimerCertificat(certificat, certificat.getNomc());
                    loadCertificats();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le certificat.");
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
