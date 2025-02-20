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
        String mdpu = mdp_tf.getText().trim(); // Assuming mdp_tf is your PasswordField

        // List of fields to check for emptiness
        List<String> fields = new ArrayList<>(Arrays.asList(mailu, mdpu));
        if (!us.areFieldsNotEmpty(fields)) {
            fieldserr.setVisible(true);
            return; // Exit the method if fields are empty
        } else {
            fieldserr.setVisible(false);
        }

        // Retrieve the user ID based on the email
        int userid = us.getUserId(mailu);
        if (userid == -1) {
            System.out.println("Email not found");
            errorField.setText("Email not found");
            errorField.setVisible(true);
            return;
        }

        // Verify login credentials
        if (us.verifierLogin(mailu, mdpu)) {
            System.out.println("Login successful");

            // User is logged in, proceed to load the appropriate dashboard
            UserDataManager.getInstance().setIdu(userid);

            // Load dashboard based on user type
            Utilisateur user = us.getUser(userid);
            if (user != null && "ADMIN".equals(user.getTypeu())) {


                Stage stage = (Stage) mail_tf.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashbord.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("AdminDashbord");

            } else {
                Stage stage = (Stage) mail_tf.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("gameXpert");

            }

            // Hide any error fields and show logged in message
            errorField.setVisible(false);
            loggedinfield.setVisible(true);
        } else {
            System.out.println("Invalid login");
            errorField.setText("Invalid email or password");
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
