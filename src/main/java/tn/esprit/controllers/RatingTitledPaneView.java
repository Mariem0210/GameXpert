package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import tn.esprit.interfaces.RatingService;
import tn.esprit.models.Rating;

import java.util.List;

public class RatingTitledPaneView {

    private RatingService ratingService = new RatingService();

    @FXML
    private ListView<String> ratingListView;  // Assurez-vous que ListView est bien défini ici

    // Méthode appelée lors de l'initialisation du contrôleur
    @FXML
    public void initialize() {
        // Récupérer la liste des notations depuis la base de données
        List<Rating> ratings = ratingService.getAllRatings();

        if (ratings.isEmpty()) {
            ratingListView.getItems().add("Aucune notation disponible.");
        } else {
            for (Rating rating : ratings) {
                // Ajouter chaque notation à la ListView sous forme de texte
                String ratingText = /*"ID: " + rating.getId() +*/
                        " | ID Formation: " + rating.getIdf() +
                        " | Note: " + rating.getNote();
                ratingListView.getItems().add(ratingText);
            }
        }
    }
}
