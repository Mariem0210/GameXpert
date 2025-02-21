package tn.esprit.controllers;

import tn.esprit.models.Utilisateur;
import tn.esprit.services.UserDataManager;
import tn.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginController {

    @FXML
    private Button Button_Login;
    @FXML
    private PasswordField mdp_tf;
    @FXML
    private TextField mail_tf;
    @FXML
    private TextField errorField;
    @FXML
    private TextField loggedinfield;
    @FXML
    private TextField fieldserr;

    ServiceUtilisateur us = new ServiceUtilisateur();


    @FXML
    void Checklogin(ActionEvent event) throws SQLException, IOException {
        String mailu = mail_tf.getText().trim();
        String mdpu = mdp_tf.getText().trim();

        if (mailu.isEmpty() || mdpu.isEmpty()) {
            fieldserr.setVisible(true);
            return;
        } else {
            fieldserr.setVisible(false);
        }

        // Récupérer l'ID de l'utilisateur
        int userid = us.getUserId(mailu);
        System.out.println("UserID récupéré: " + userid);

        if (userid == -1) {
            System.out.println("Email non trouvé !");
            errorField.setText("Email non trouvé");
            errorField.setVisible(true);
            return;
        }

        // Vérifier le mot de passe
        if (us.verifierLogin(mailu, mdpu)) {
            System.out.println("Connexion réussie");

            // Récupérer les infos de l'utilisateur
            UserDataManager.getInstance().setIdu(userid);
            Utilisateur user = us.getUser(userid);
            System.out.println("Utilisateur récupéré: " + user);

            if (user == null) {
                System.out.println("Erreur : utilisateur introuvable !");
                errorField.setText("Erreur interne");
                errorField.setVisible(true);
                return;
            }

            // Vérifier le type de l'utilisateur
            String userType = user.getTypeu();
            System.out.println("Type utilisateur: " + userType);

            String fxmlFile;
            String title;

            if ("ADMIN".equals(userType)) {
                fxmlFile = "/AdminDashbord.fxml";
                title = "Admin Dashboard";
            } else if ("JOUEUR".equals(userType) || "COACH".equals(userType)) {
                fxmlFile = "/HomePage.fxml";
                title = "GameXpert";
            } else {
                System.out.println("Type d'utilisateur inconnu !");
                errorField.setText("Accès refusé");
                errorField.setVisible(true);
                return;
            }

            // Changer de page
            Stage stage = (Stage) mail_tf.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);

            errorField.setVisible(false);
            loggedinfield.setVisible(true);
        } else {
            System.out.println("Login invalide !");
            errorField.setText("Email ou mot de passe incorrect");
            errorField.setVisible(true);
            loggedinfield.setVisible(false);
        }
    }


    // Method to load a new scene
    private void loadScene(String fxmlPath, String title) throws IOException {
        Stage stage = (Stage) Button_Login.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }

    @FXML
    void CreateAccount(ActionEvent event) throws IOException {
        loadScene("/CreateAccount.fxml", "Create Account");
    }
}