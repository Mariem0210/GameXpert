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
    private int rating = 0;

    private final Image starFull = new Image(getClass().getResourceAsStream("/star_full.png"));
    private final Image starEmpty = new Image(getClass().getResourceAsStream("/star_empty.png"));


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

        if (clickedStar == star1) rating = 1;
        else if (clickedStar == star2) rating = 2;
        else if (clickedStar == star3) rating = 3;
        else if (clickedStar == star4) rating = 4;
        else if (clickedStar == star5) rating = 5;

        updateStars();
    }

    private void updateStars() {
        resetStars();
        for (int i = 1; i <= rating; i++) {
            if (i == 1) star1.setImage(starFull);
            else if (i == 2) star2.setImage(starFull);
            else if (i == 3) star3.setImage(starFull);
            else if (i == 4) star4.setImage(starFull);
            else if (i == 5) star5.setImage(starFull);
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
        if (rating == 0) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une note.");
            return;
        }

        RatingService.enregistrerNote(formation.getIdf(), rating);
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
