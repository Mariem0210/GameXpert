package tn.esprit.controllers;


import tn.esprit.models.Utilisateur;
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

public class HomePageController {
    @FXML
    private Button logoutButton;
    @FXML
    private Button ProfileButton;
    private UserDataManager userDataManager = UserDataManager.getInstance();
    ServiceUtilisateur us=new ServiceUtilisateur();
    int CurrentUserId = userDataManager.getIdu();
    Utilisateur currentUser = us.getUser(CurrentUserId);
    @FXML
    void LogOut(ActionEvent event) throws IOException {
        userDataManager.logout();
        CurrentUserId =0;
        currentUser=null;
        Stage stage=(Stage) logoutButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
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
}
