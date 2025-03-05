package tn.esprit.controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import tn.esprit.models.Game;
import javafx.stage.Stage;

import javafx.fxml.FXML;

import java.util.List;

public class GameListView {
    @FXML
    private FlowPane gameFlowPane; // Conteneur pour afficher les jeux

    @FXML
    public void initialize() {
        List<Game> games = RawgAPI.getGames(); // Récupérer les jeux via l'API

        // Ajouter les jeux à la vue
        for (Game game : games) {
            // Création d'un VBox pour chaque jeu
            VBox gameBox = new VBox();
            gameBox.setSpacing(10);
            gameBox.setPadding(new Insets(10));

            // Titre du jeu
            Text titleText = new Text(game.getName());
            titleText.setFont(new Font(16));

            // Note du jeu
            Text ratingText = new Text("⭐ Note: " + game.getRating());
            ratingText.setFont(new Font(12));

            // Image du jeu
            ImageView imageView = new ImageView();
            imageView.setImage(new Image(game.getImageUrl(), 100, 100, true, true));

            // Bouton pour en savoir plus
            Button infoButton = new Button("Voir plus");
            infoButton.setOnAction(event -> {
                // Action pour afficher plus d'infos sur le jeu
                showGameDetails(game);
            });

            // Ajouter les éléments au VBox
            gameBox.getChildren().addAll(imageView, titleText, ratingText, infoButton);

            // Ajouter le VBox au FlowPane
            gameFlowPane.getChildren().add(gameBox);
        }
    }

   /* private void showGameDetails(Game game) {
        // Méthode pour afficher plus de détails sur le jeu
        System.out.println("Détails du jeu : " + game.getName());
        // Ajoutez ici la logique pour afficher une nouvelle fenêtre ou une vue détaillée
    }*/
   private void showGameDetails(Game game) {
       Stage detailsStage = new Stage();
       detailsStage.setTitle("Détails du jeu");

       VBox vbox = new VBox(10);
       vbox.setPadding(new Insets(10));

       Text titleText = new Text("Nom: " + game.getName());
       Text ratingText = new Text("Note: " + game.getRating());
       //Text descriptionText = new Text("Description: " + game.getDescription()); // Assure-toi que `getDescription()` existe

       ImageView imageView = new ImageView(new Image(game.getImageUrl(), 200, 200, true, true));

       vbox.getChildren().addAll(imageView, titleText, ratingText);

       Scene scene = new Scene(vbox, 400, 300);
       detailsStage.setScene(scene);
       detailsStage.show();
   }
}
