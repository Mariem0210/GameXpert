package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.models.Formation;
import tn.esprit.interfaces.RatingService;

public class RatingController {

    @FXML private ImageView star1;
    @FXML private ImageView star2;
    @FXML private ImageView star3;
    @FXML private ImageView star4;
    @FXML private ImageView star5;

    private Formation formation;
    private double rating = 0;  // Utilisez un double pour permettre les demi-étoiles.

    private final Image starFull = new Image(getClass().getResourceAsStream("/star_full.png"));
    private final Image starEmpty = new Image(getClass().getResourceAsStream("/star_empty.png"));
    private final Image starHalf = new Image(getClass().getResourceAsStream("/star_half.png"));

    @FXML
    public void initialize() {
        resetStars();
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    @FXML
    public void handleStarClick(javafx.scene.input.MouseEvent event) {
        ImageView clickedStar = (ImageView) event.getSource();

        // Définir la note en fonction de l'étoile cliquée
        if (clickedStar == star1) rating = 1.0;
        else if (clickedStar == star2) rating = 2.0;
        else if (clickedStar == star3) rating = 3.0;
        else if (clickedStar == star4) rating = 4.0;
        else if (clickedStar == star5) rating = 5.0;

        // Vérifier si l'étoile cliquée est pleine ou vide
        if (clickedStar.getImage().equals(starFull)) {
            // Si l'étoile est pleine, cela signifie que l'utilisateur veut une demi-étoile
            if (rating < 5.0) {
                rating += 0.5;  // Ajouter une demi-étoile si ce n'est pas déjà 5
            }
        } else if (clickedStar.getImage().equals(starEmpty)) {
            // Si l'étoile est vide, retirer une demi-étoile
            rating -= 0.5;
        }

        updateStars(); // Mettre à jour l'affichage des étoiles
    }


    private void updateStars() {
        resetStars();
        for (int i = 1; i <= (int) rating; i++) {
            if (i == 1) star1.setImage(starFull);
            else if (i == 2) star2.setImage(starFull);
            else if (i == 3) star3.setImage(starFull);
            else if (i == 4) star4.setImage(starFull);
            else if (i == 5) star5.setImage(starFull);
        }
        if (rating % 1 != 0) {
            // Ajoute une demi-étoile
            if ((int) rating + 1 <= 5) {
                ImageView star = getStarByIndex((int) rating + 1);
                if (star != null) star.setImage(starHalf);
            }
        }
    }

    private ImageView getStarByIndex(int index) {
        switch (index) {
            case 1: return star1;
            case 2: return star2;
            case 3: return star3;
            case 4: return star4;
            case 5: return star5;
            default: return null;
        }
    }

    private void resetStars() {
        star1.setImage(starEmpty);
        star2.setImage(starEmpty);
        star3.setImage(starEmpty);
        star4.setImage(starEmpty);
        star5.setImage(starEmpty);
    }

    @FXML
    public void submitRating() {
        if (formation == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "La formation est introuvable.");
            return;
        }

        if (rating == 0) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une note.");
            return;
        }

        RatingService.enregistrerNote(formation.getIdf(), rating);  // Assurez-vous que 'rating' est un double

        // Enregistrer une note avec des valeurs décimales.
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Votre note a été enregistrée !");

        // Fermer la fenêtre
        Stage stage = (Stage) star1.getScene().getWindow();
        stage.close();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
