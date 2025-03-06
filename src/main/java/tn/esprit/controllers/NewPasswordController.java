package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.services.ServiceUtilisateur;
import java.io.IOException;
import java.sql.SQLException;

public class NewPasswordController {

    // Éléments FXML
    @FXML
    private PasswordField hiddenpassword1;
    @FXML
    private PasswordField hiddenpassword2;
    @FXML
    private TextField password1;
    @FXML
    private TextField password2;
    @FXML
    private Button hide, hide1, show, show1;

    private ServiceUtilisateur userService = new ServiceUtilisateur();
    private String userEmail; // Stocke l'email de l'utilisateur



    @FXML
    void CheckCode(ActionEvent event) {
        String newPassword = getCurrentPassword();
        String confirmPassword = getConfirmPassword();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Erreur", "Les champs ne doivent pas être vides");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas");
            return;
        }

        if (!userService.isMdp(newPassword)) {
            showAlert("Erreur", "Le mot de passe doit contenir au moins 6 caractères");
            return;
        }

        try {
            // Mettre à jour le mot de passe via l'email
            userService.updatePassword(userEmail, newPassword);
            showAlert("Succès", "Mot de passe mis à jour !");
            redirectToLogin();
        } catch (SQLException e) {
            showAlert("Erreur", "Échec de la mise à jour");
            e.printStackTrace();
        }
    }

    private String getCurrentPassword() {
        return hiddenpassword1.isVisible() ?
                hiddenpassword1.getText() :
                password1.getText();
    }

    private String getConfirmPassword() {
        return hiddenpassword2.isVisible() ?
                hiddenpassword2.getText() :
                password2.getText();
    }

    private void redirectToLogin() {
        try {
            Stage stage = (Stage) hiddenpassword1.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Login.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void Hidepassword11(ActionEvent event) {
        hiddenpassword2.setText(password2.getText());
        hiddenpassword2.setVisible(true);
        password2.setVisible(false);
        hide1.setVisible(false);
        show1.setVisible(true);
    }

    @FXML
    void ShowPassword(ActionEvent event) {
        password1.setText(hiddenpassword1.getText());
        password1.setVisible(true);
        hiddenpassword1.setVisible(false);
        hide.setVisible(true);
        show.setVisible(false);
    }

    @FXML
    void hidePassword(ActionEvent event) {
        hiddenpassword1.setText(password1.getText());
        hiddenpassword1.setVisible(true);
        password1.setVisible(false);
        hide.setVisible(false);
        show.setVisible(true);
    }

    @FXML
    void Showpassword11(ActionEvent event) {
        password2.setText(hiddenpassword2.getText());
        password2.setVisible(true);
        hiddenpassword2.setVisible(false);
        hide1.setVisible(true);
        show1.setVisible(false);
    }
    public void setUserEmail(String email) {
        this.userEmail = email;
    }

}