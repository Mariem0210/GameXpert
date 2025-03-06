package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.services.ServiceUtilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class CodeController {

    // Les éléments FXML existants
    @FXML private Button Button_Login;
    @FXML private Button Button_cancel;
    @FXML private TextField CodeErr;
    @FXML private TextField fieldserr;
    @FXML private TextField usernametext;
    @FXML private Label label_user_id;
    @FXML private Label CodeLabel;

    private final ServiceUtilisateur userService = new ServiceUtilisateur();
    private String userEmail; // Variable pour stocker l'email

    @FXML
    void Cancel(ActionEvent event) throws IOException {
        Stage stage = (Stage) Button_cancel.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
    }

    @FXML
    void CheckCode(ActionEvent event) {
        String enteredCode = usernametext.getText().trim();
        this.userEmail = label_user_id.getText();

        try {
            if (userEmail == null || userEmail.isEmpty()) {
                showCodeError("Email non valide");
                return;
            }

            boolean isValidCode = userService.verifyResetCode(userEmail, enteredCode);

            if (isValidCode) {
                loadNewPasswordScreen();
            } else {
                showCodeError("Code invalide ou expiré");
            }
        } catch (SQLException e) {
            showCodeError("Erreur base de données : " + e.getMessage()); // Afficher le message réel
            e.printStackTrace();
        } catch (IOException e) {
            showCodeError("Erreur de navigation");
            e.printStackTrace();
        }
    }

    private void loadNewPasswordScreen() throws IOException {
        Stage stage = (Stage) usernametext.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewPassword.fxml"));
        Parent root = loader.load();

        NewPasswordController controller = loader.getController();
        controller.setUserEmail(userEmail);

        stage.setScene(new Scene(root));
        stage.setTitle("Nouveau mot de passe");
    }



    private void showCodeError(String message) {
        CodeErr.setText(message);
        CodeErr.setVisible(true);
        fieldserr.setVisible(false);
    }

    public void setCode(int code) {
        CodeLabel.setText(Integer.toString(code));
    }

    // Méthode pour recevoir l'email depuis ForgotPasswordController
    public void setUserEmail(String email) {
        label_user_id.setText(email);
        this.userEmail = email;
    }
}