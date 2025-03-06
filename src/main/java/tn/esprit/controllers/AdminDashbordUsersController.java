package tn.esprit.controllers;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.esprit.services.AuthService;
import tn.esprit.services.UserDataManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.geometry.Pos;

import javax.print.DocFlavor;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class AdminDashbordUsersController implements Initializable {

    private final IService<Utilisateur> su = new ServiceUtilisateur();
    @FXML
    public Button refreshButton;
    @FXML
    public Button logoutButton;
    @FXML
    public Label usernameOld;
    @FXML
    public Label UserType;
    @FXML
    public ImageView photoProfile;
    @FXML
    private VBox cardContainers;
    @FXML
    private final UserDataManager userDataManager = UserDataManager.getInstance();
    @FXML
    private Button btnSupprimer; // Assure-toi que le bouton est bien lié à ton fichier FXML
    @FXML
    private int CurrentUserId = userDataManager.getIdu();
    Utilisateur currentUser = su.getUser(CurrentUserId);
    ServiceUtilisateur us=new ServiceUtilisateur();
    private final Utilisateur selectedUtilisateur = null;




    @FXML
    public void refreshUtilisateursList() {
        cardContainers.getChildren().clear();

        HBox currentRow = new HBox(10);
        currentRow.setAlignment(Pos.TOP_LEFT);
        int cardCount = 0;

        for (Utilisateur u : su.afficher()) {  // Afficher TOUS les utilisateurs (Coach & Joueur)
            System.out.println("Ajout dans UI - Nom: " + u.getNomu() + " | Type: " + u.getTypeu()); // Vérification




            VBox card = new VBox(10);
            card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-width: 2px; "
                    + "-fx-border-radius: 20px; -fx-padding: 20px; -fx-max-width: 300px; "
                    + "-fx-spacing: 15px; -fx-background-radius: 20px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 10); "
                    + "-fx-opacity: 0.95;");

            Label nameLabel = new Label("Nom: " + u.getNomu());
            nameLabel.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 16px; -fx-font-family: 'Cambria', serif; -fx-font-weight: bold;");

            Label prenomLabel = new Label("Prénom: " + u.getPrenomu());
            prenomLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px;");

            Label emailLabel = new Label("Email: " + u.getMailu());
            emailLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px;");

            Label numTelLabel = new Label("Téléphone: " + u.getNumtelu());
            numTelLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px;");

            Label typeLabel = new Label("Type: " + Utilisateur.getTypeu()); // Affiche le type utilisateur
            typeLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px;");

            Label dateInscriptionLabel = new Label("Date d'inscription: " + u.getDateinscriu());
            dateInscriptionLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px;");

            Label dateNaissanceLabel = new Label("Date de naissance: " + u.getDatenaissanceu());
            dateNaissanceLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px;");

            card.getChildren().addAll(nameLabel, prenomLabel, emailLabel, numTelLabel, typeLabel, dateInscriptionLabel, dateNaissanceLabel);

            currentRow.getChildren().add(card);
            cardCount++;

            if (cardCount >= 4) {
                cardContainers.getChildren().add(currentRow);
                currentRow = new HBox(10);
                currentRow.setAlignment(Pos.TOP_LEFT);
                cardCount = 0;
            }
        }

        if (cardCount > 0) {
            cardContainers.getChildren().add(currentRow);
        }

        // Forcer une mise à jour de l'affichage
        cardContainers.requestLayout();
    }

    @FXML
    void LogOut(ActionEvent event) {
        try {
            AuthService authService = new AuthService();
            authService.supprimerToken();
        } catch (IOException e) {
            Platform.runLater(() -> {
                new Alert(Alert.AlertType.WARNING,
                        "Impossible de supprimer les tokens Google\n" + e.getMessage())
                        .show();
            });
        }

        try {
            userDataManager.logout();
            CurrentUserId = 0;
            currentUser = null;

            Stage stage = (Stage) logoutButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();

            // Création d'une nouvelle scène vierge
            Scene newScene = new Scene(root);

            // Remplacement complet de la scène
            stage.setScene(newScene);
            stage.setTitle("Login");

            // Nettoyage mémoire explicite
            root = null;
            loader = null;

        } catch (IOException e) {
            Platform.runLater(() -> {
                new Alert(Alert.AlertType.ERROR,
                        "Erreur lors de la déconnexion\nVeuillez fermer l'application manuellement")
                        .show();
            });
        }
    }

    private VBox selectedCard = null;
    @FXML
    private void highlightSelectedCard(VBox card) {
        if (selectedCard != null) {
            selectedCard.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-width: 2px; "
                    + "-fx-border-radius: 20px; -fx-padding: 20px; -fx-max-width: 300px; "
                    + "-fx-spacing: 15px; -fx-background-radius: 20px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 10); "
                    + "-fx-opacity: 0.95;");
        }

        selectedCard = card;
        selectedCard.setStyle("-fx-background-color: #444466; -fx-border-color: #ffcc00; -fx-border-width: 3px; "
                + "-fx-border-radius: 20px; -fx-padding: 20px; -fx-max-width: 300px; "
                + "-fx-spacing: 15px; -fx-background-radius: 20px; -fx-effect: dropshadow(gaussian, rgba(255, 255, 0, 0.5), 15, 0, 0, 10); "
                + "-fx-opacity: 1;");
    }



    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {UserType.setText(Utilisateur.getTypeu());
        usernameOld.setText(currentUser.getMailu());
        Image photo_profile=us.loadImage(currentUser.getPhoto_de_profile());
        photoProfile.setImage(photo_profile);
        refreshUtilisateursList();



    }

}
