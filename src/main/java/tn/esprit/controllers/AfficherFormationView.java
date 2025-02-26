package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tn.esprit.models.Formation;
import tn.esprit.interfaces.FormationService; // Assurez-vous du bon import

import java.sql.SQLException;
import java.util.List;

public class AfficherFormationView {

    @FXML
    private VBox formationVBox; // Assurez-vous que l'ID correspond à celui du FXML

    private final FormationService formationService = new FormationService(); // Instanciation du service

    @FXML
    public void initialize() {
        loadFormations();
    }

    @FXML
    public void loadFormations() {
        formationVBox.getChildren().clear();

        try {
            List<Formation> formations = formationService.recupererFormations(); // Appel via instance

            for (Formation formation : formations) {
                VBox formationCard = new VBox(5);
                formationCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #1e1e2e;");
                formationCard.setPrefWidth(550);

                // Labels des formations
                Label nomLabel = new Label("Nom Du Formation: " + formation.getNomf());
                Label descriptionLabel = new Label("Description: " + formation.getDescriptionf());
                Label niveauLabel = new Label("Niveau: " + formation.getNiveauf());
                Label dateDebutLabel = new Label("Date Début: " + formation.getDateDebutf());
                Label dateFinLabel = new Label("Date Fin: " + formation.getDateFinf());
                Label capaciteLabel = new Label("Capacité(Personnes): " + formation.getCapacitef());
                Label prixLabel = new Label("Prix(dt): " + formation.getPrixf());

                // Ajouter les labels à la carte
                formationCard.getChildren().addAll(nomLabel, descriptionLabel, niveauLabel, dateDebutLabel, dateFinLabel, capaciteLabel, prixLabel);

                // Ajouter la carte au VBox principal
                formationVBox.getChildren().add(formationCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les formations.");
        }
    }

    // Méthode utilitaire pour afficher une alerte
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
