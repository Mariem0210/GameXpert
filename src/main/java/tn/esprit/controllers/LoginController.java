package tn.esprit.controllers;

import tn.esprit.services.UserDataManager;
import tn.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginController {

    @FXML
    private Button Button_Login;
    @FXML
    private PasswordField mdp_tf; // Renommé et simplifié
    @FXML
    private TextField mail_tf;
    @FXML
    private TextField errorField;
    @FXML
    private TextField loggedinfield;
    @FXML
    private Hyperlink CreateAccHL;
    @FXML
    private TextField fieldserr;
    @FXML
    private Hyperlink hlpassword;
    @FXML
    private Button Qrcode_Button;

    ServiceUtilisateur us = new ServiceUtilisateur();

    @FXML
    void Checklogin(ActionEvent event) throws SQLException, IOException, InvalidAlgorithmParameterException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException,
            InvalidKeyException {

        String mailu = mail_tf.getText();
        String mdpu = mdp_tf.getText(); // Récupération directe depuis PasswordField

        List<String> fields = new ArrayList<>(Arrays.asList(mailu, mdpu));
        if (!us.areFieldsNotEmpty(fields)) {
            fieldserr.setVisible(true);
        } else {
            fieldserr.setVisible(false);
            int userid = us.getUserId(mailu);

            if (us.verifierLogin(mailu, mdpu)) {
                System.out.println("Login successful");
                loggedinfield.setVisible(true);
                errorField.setVisible(false);

                int userId = us.getUserId(mailu);

                if (us.getUser(userId).getTypeu().equals("ADMIN")) {
                    UserDataManager.getInstance().setIdu(userId);
                    loadScene("/AdminDashboard.fxml", "Admin Dashboard");
                } else {
                    UserDataManager.getInstance().setIdu(userId);
                    loadScene("/HomePage.fxml", "Home");
                }
            } else {
                System.out.println("Invalid login");
                errorField.setVisible(true);
                loggedinfield.setVisible(false);
            }
        }
    }

    private void loadScene(String fxmlPath, String title) throws IOException {
        Stage stage = (Stage) Button_Login.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage.setScene(new Scene(root));
        stage.setTitle(title);
    }

    @FXML
    void CreateAccount(ActionEvent event) throws IOException {
        loadScene("/CreateAccount.fxml", "Create Account");
    }

    @FXML
    void mdpoublier(ActionEvent event) throws IOException {
        loadScene("/MdpOublie.fxml", "Forgot Password");
    }

    @FXML
    void LoginQrcode(ActionEvent event) throws IOException {
        loadScene("/QRLogin.fxml", "QR code Login");
    }
}