package tn.esprit.controllers;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.interfaces.RatingService;
import tn.esprit.models.Rating;
import java.util.List;

public class RatingTitledPaneView {
    private RatingService ratingService = new RatingService();


    public void start(Stage primaryStage) {
        // Récupérer la liste des notations depuis la base de données
        List<Rating> ratings = ratingService.getAllRatings();

        // Conteneur pour afficher les notations
        VBox ratingBox = new VBox(10); // Espacement de 10px entre les labels

        if (ratings.isEmpty()) {
            ratingBox.getChildren().add(new Label("Aucune notation disponible."));
        } else {
            for (Rating rating : ratings) {
                Label ratingLabel = new Label("ID: " + rating.getId() +
                        " | ID Formation: " + rating.getIdf() +
                        " | Note: " + rating.getNote());
                ratingBox.getChildren().add(ratingLabel);
            }
        }

        // Création du TitledPane
        TitledPane titledPane = new TitledPane("Liste des Notations", ratingBox);
        titledPane.setCollapsible(false); // Empêcher la fermeture

        Scene scene = new Scene(titledPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Notations des Formations");
        primaryStage.show();
    }



}
