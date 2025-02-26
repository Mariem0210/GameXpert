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

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class LoginController {
    @FXML
    public CheckBox rememberMeCheckbox;
    @FXML
    public Hyperlink hlpassword;
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

    private static final String CREDENTIALS_FILE = "credentials.properties";

    ServiceUtilisateur us = new ServiceUtilisateur();

    @FXML
    public void initialize() {
        loadRememberedCredentials();

        // Auto-login si les identifiants sont enregistrés et la case est cochée
        if (!mail_tf.getText().isEmpty() && !mdp_tf.getText().isEmpty() && rememberMeCheckbox.isSelected()) {
            autoLogin();
        }
    }

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

        int userid = us.getUserId(mailu);
        if (userid == -1) {
            errorField.setText("Email non trouvé");
            errorField.setVisible(true);
            return;
        }

        if (us.verifierLogin(mailu, mdpu)) {
            UserDataManager.getInstance().setIdu(userid);
            Utilisateur user = us.getUser(userid);

            if (user == null) {
                errorField.setText("Erreur interne");
                errorField.setVisible(true);
                return;
            }

            String userType = user.getTypeu();
            String fxmlFile;
            String title;

            if ("ADMIN".equals(userType)) {
                fxmlFile = "/AdminDashbord.fxml";
                title = "Admin Dashboard";
            } else if ("JOUEUR".equals(userType) || "COACH".equals(userType)) {
                fxmlFile = "/HomePage.fxml";
                title = "GameXpert";
            } else {
                errorField.setText("Accès refusé");
                errorField.setVisible(true);
                return;
            }

            if (rememberMeCheckbox.isSelected()) {
                saveCredentials(mailu, mdpu);
            } else {
                clearSavedCredentials();
            }

            loadScene(fxmlFile, title);

            errorField.setVisible(false);
            loggedinfield.setVisible(true);
        } else {
            errorField.setText("Email ou mot de passe incorrect");
            errorField.setVisible(true);
            loggedinfield.setVisible(false);
        }
    }

    private void autoLogin() {
        String mailu = mail_tf.getText();
        String mdpu = mdp_tf.getText();

        try {
            int userid = us.getUserId(mailu);
            if (userid == -1 || !us.verifierLogin(mailu, mdpu)) {
                return;
            }

            // Si les identifiants sont valides, déclencher l'événement de clic
            Button_Login.fire();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveCredentials(String email, String password) {
        try (FileOutputStream output = new FileOutputStream(CREDENTIALS_FILE)) {
            Properties prop = new Properties();
            prop.setProperty("email", email);
            prop.setProperty("password", password);
            prop.setProperty("rememberMe", "true");
            prop.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRememberedCredentials() {
        try (FileInputStream input = new FileInputStream(CREDENTIALS_FILE)) {
            Properties prop = new Properties();
            prop.load(input);
            mail_tf.setText(prop.getProperty("email", ""));
            mdp_tf.setText(prop.getProperty("password", ""));
            rememberMeCheckbox.setSelected("true".equals(prop.getProperty("rememberMe")));
        } catch (IOException e) {
            System.out.println("Aucune information de connexion enregistrée.");
        }
    }

    private void clearSavedCredentials() {
        File file = new File(CREDENTIALS_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @FXML
    void CreateAccount(ActionEvent event) throws IOException {
        loadScene("/CreateAccount.fxml", "Create Account");
    }

    private void loadScene(String fxmlPath, String title) throws IOException {
        Stage stage = (Stage) Button_Login.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }
    @FXML
    void mdpoublier(ActionEvent event) throws IOException {
        Stage stage=(Stage) hlpassword.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MdpOublie.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Forgot Password");
    }
}
