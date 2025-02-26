package tn.esprit.controllers;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import tn.esprit.models.Formation;
import tn.esprit.interfaces.FormationService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherFormationController {
    @FXML
    private TilePane formationTilePane;
    @FXML
    private Button certificatButton;

    private FormationService formationService = new FormationService();

    @FXML
    public void initialize() {
        loadFormations();
    }

    @FXML
    public void loadFormations() {
        formationTilePane.getChildren().clear();
        formationTilePane.setHgap(50);
        formationTilePane.setVgap(120);

        try {
            List<Formation> formations = formationService.recupererFormations();

            for (Formation formation : formations) {
                VBox formationCard = new VBox(10);

                // Chargement de l'image depuis resources
                Image backgroundImage = new Image(getClass().getResource("/ground.jpg").toExternalForm());
                BackgroundImage background = new BackgroundImage(
                        backgroundImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, false, true)
                );

                formationCard.setBackground(new Background(background)); // Appliquer l'image de fond

                formationCard.setStyle("-fx-border-radius: 10; -fx-border-color: #8a2be2; -fx-padding: 15px;");
                formationCard.setPrefSize(280, 320);

                // Éléments de la carte

                Label nomLabel = new Label("Nom: " + formation.getNomf());
                Label descriptionLabel = new Label("Description: " + formation.getDescriptionf());
                Label niveauLabel = new Label("Niveau: " + formation.getNiveauf());
                Label dateDebutLabel = new Label("Date Début: " + formation.getDateDebutf());
                Label dateFinLabel = new Label("Date Fin: " + formation.getDateFinf());
                Label capaciteLabel = new Label("Capacité: " + formation.getCapacitef());
                Label prixLabel = new Label("Prix: " + formation.getPrixf());
                Label iduLabel = new Label("ID Utilisateur: " + formation.getIdu());

                // Bouton Supprimer (effet rouge néon)
                // Bouton Certificat
                Button certificatButton = new Button("Certificat");
                certificatButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #8e44ad, #663399);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(gaussian, rgba(138, 43, 226, 0.7), 10, 0.5, 0, 0);" +
                                "-fx-cursor: hand;"
                );
                certificatButton.setOnMouseEntered(e -> certificatButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #9b59b6, #8e44ad);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(gaussian, rgba(138, 43, 226, 1), 15, 0.7, 0, 0);" +
                                "-fx-cursor: hand;" +
                                "-fx-scale-x: 1.1;" +
                                "-fx-scale-y: 1.1;"
                ));
                certificatButton.setOnMouseExited(e -> certificatButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #8e44ad, #663399);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(gaussian, rgba(138, 43, 226, 0.7), 10, 0.5, 0, 0);" +
                                "-fx-cursor: hand;"
                ));

// Action lors du clic sur le bouton "Certificat"
                certificatButton.setOnAction(event -> ouvrirFormulaireCertificat(formation));

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
                deleteButton.setOnAction(event -> deleteFormation(formation));

// Bouton Modifier (effet bleu néon)
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
                    if (formation.getNomf() == null || formation.getNomf().isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom de la formation est vide.");
                        return;
                    }

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de modification");
                    alert.setHeaderText(null);
                    alert.setContentText("Voulez-vous vraiment modifier cette formation ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierFormation.fxml"));
                            Scene scene = new Scene(loader.load());

                            ModifierFormationController controller = loader.getController();
                            controller.initData(formation);

                            controller.setOnUpdateSuccess(() -> loadFormations());

                            Stage stage = new Stage();
                            stage.setTitle("Modifier Formation");
                            stage.setScene(scene);
                            stage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire de modification.");
                        }
                    }
                });

                // Ajouter les éléments à la carte
                formationCard.getChildren().addAll(
                        nomLabel, descriptionLabel, niveauLabel, dateDebutLabel,
                        dateFinLabel, capaciteLabel, prixLabel, iduLabel,
                        deleteButton, modifButton , certificatButton
                );

                formationTilePane.getChildren().add(formationCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les formations.");
        }
    }

    private void deleteFormation(Formation formation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette formation ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    formationService.supprimerFormation(formation, formation.getNomf());
                    loadFormations();
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

    private void ouvrirFormulaireCertificat(Formation formation) {
        try {
            // Charger l'interface AjouterCertificat.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCertificat.fxml"));
            Parent root = loader.load();

            // Passer les données de la formation au contrôleur du certificat
            AjouterCertificatController controller = loader.getController();
            controller.setFormation(formation); // Méthode à créer dans AjouterCertificatController

            // Créer une nouvelle scène
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajout Certificat - " + formation.getNomf());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire de certificat.");
        }
    }

}