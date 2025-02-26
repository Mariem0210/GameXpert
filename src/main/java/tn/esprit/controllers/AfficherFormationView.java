package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Formation;
import tn.esprit.interfaces.FormationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherFormationView {

    @FXML
    private VBox formationVBox;

    private final FormationService formationService = new FormationService();

    @FXML
    public void initialize() {
        loadFormations();
    }

    @FXML
    public void loadFormations() {
        formationVBox.getChildren().clear();

        try {
            List<Formation> formations = formationService.recupererFormations();

            for (Formation formation : formations) {
                VBox formationCard = new VBox(5);
                formationCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #1e1e2e;");
                formationCard.setPrefWidth(550);

                // Labels des formations
                Label nomLabel = new Label("Nom: " + formation.getNomf());
                Label descriptionLabel = new Label("Description: " + formation.getDescriptionf());
                Label niveauLabel = new Label("Niveau: " + formation.getNiveauf());
                Label dateDebutLabel = new Label("Date Début: " + formation.getDateDebutf());
                Label dateFinLabel = new Label("Date Fin: " + formation.getDateFinf());
                Label capaciteLabel = new Label("Capacité: " + formation.getCapacitef());
                Label prixLabel = new Label("Prix: " + formation.getPrixf());

                // Bouton de notation
                Button rateButton = new Button("Noter");
                rateButton.setOnAction(event -> ouvrirInterfaceNotation(formation));

                // Ajouter les labels et le bouton à la carte
                formationCard.getChildren().addAll(nomLabel, descriptionLabel, niveauLabel, dateDebutLabel, dateFinLabel, capaciteLabel, prixLabel, rateButton);

                // Ajouter la carte au VBox principal
                formationVBox.getChildren().add(formationCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les formations.");
        }
    }

    private void ouvrirInterfaceNotation(Formation formation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RatingView.fxml"));



            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            RatingController controller = loader.getController();
            controller.setFormation(formation);

            stage.setTitle("Noter la formation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
