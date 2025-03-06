package tn.esprit.controllers;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.AuthService;
import tn.esprit.services.UserDataManager;
import tn.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.ResourceBundle;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class HomePageController implements Initializable {
    @FXML
    public Label usernameOld;
    @FXML
    public Label UserType;
    @FXML
    public ImageView photoProfile;
    @FXML
    private Button logoutButton;
    @FXML
    private Button ProfileButton;
    private UserDataManager userDataManager = UserDataManager.getInstance();
    ServiceUtilisateur us=new ServiceUtilisateur();
    int CurrentUserId = userDataManager.getIdu();
    Utilisateur currentUser = us.getUser(CurrentUserId);
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
    @FXML
    void EditProfilebutton(ActionEvent event) throws IOException {
        Stage stage=(Stage) ProfileButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateProfile.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Profile Management");
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserType.setText(currentUser.getTypeu().toString());
        usernameOld.setText(currentUser.getMailu());
        Image photo_profile=us.loadImage(currentUser.getPhoto_de_profile());
        photoProfile.setImage(photo_profile);



    }
}
