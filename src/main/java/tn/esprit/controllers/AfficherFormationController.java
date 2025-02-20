package tn.esprit.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Formation;
import tn.esprit.interfaces.FormationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherFormationController {
    @FXML
    private TilePane formationTilePane;

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
                VBox formationCard = new VBox(5);
                formationCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f4f4f4;");
                formationCard.setPrefSize(200, 250);

                // Éléments de la carte
                Label idLabel = new Label("ID: " + formation.getIdf());
                Label nomLabel = new Label("Nom: " + formation.getNomf());
                Label descriptionLabel = new Label("Description: " + formation.getDescriptionf());
                Label niveauLabel = new Label("Niveau: " + formation.getNiveauf());
                Label dateDebutLabel = new Label("Date Début: " + formation.getDateDebutf());
                Label dateFinLabel = new Label("Date Fin: " + formation.getDateFinf());
                Label capaciteLabel = new Label("Capacité: " + formation.getCapacitef());
                Label prixLabel = new Label("Prix: " + formation.getPrixf());
                Label iduLabel = new Label("ID: " + formation.getIdu());


                // Boutons
                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> deleteFormation( formation)); // Passer l'ID de la formation


                Button modifierButton = new Button("Modifier");
                modifierButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                modifierButton.setOnAction(event -> modifierFormation(formation));

                // Conteneur pour les boutons
                HBox buttonContainer = new HBox(10);
                buttonContainer.getChildren().addAll(deleteButton, modifierButton);

                // Ajout des éléments à la carte
                formationCard.getChildren().addAll(
                        idLabel, nomLabel, descriptionLabel, niveauLabel,
                        dateDebutLabel, dateFinLabel, capaciteLabel, prixLabel,
                        buttonContainer
                );

                formationTilePane.getChildren().add(formationCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                    formationService.supprimerFormation(formation, formation.getNomf()); // Passer le nom de la formation
                    loadFormations(); // Rafraîchir après suppression
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer la formation.");
                }
            }
        });
    }




    private void modifierFormation(Formation formation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de modification");
        alert.setHeaderText("Êtes-vous sûr de vouloir modifier cette formation ?");
        alert.setContentText("Assurez-vous que toutes les informations sont correctes.");

        // Action lorsque l'utilisateur clique sur le bouton "OK"
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierFormation.fxml"));
                    Parent root = loader.load();

                    ModifierFormationController controller = loader.getController();
                    controller.initData(formation);

                    // Définir le callback de rafraîchissement
                    controller.setOnUpdateSuccess(() -> {
                        loadFormations(); // Rafraîchir après modification
                    });

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Modifier Formation");
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Pas d'en-tête
        alert.setContentText(message);
        alert.showAndWait(); // Afficher l'alerte et attendre la fermeture
    }

}