package tn.esprit.controllers;

import tn.esprit.models.Joueur;
import tn.esprit.models.Coach;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.UserDataManager;
import tn.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewPasswordController {

    @FXML
    private Button Button_Login;

    @FXML
    private Button Button_cancel;

    @FXML
    private TextField CodeErr;

    @FXML
    private Label CodeLabel;

    @FXML
    private ImageView eyeclosed, eyeclosed1, eyeopen, eyeopen1;

    @FXML
    private TextField fieldserr, passerr, password1, password2, weakPassword;

    @FXML
    private PasswordField hiddenpassword1, hiddenpassword2;

    @FXML
    private Button hide, hide1, show, show1;

    @FXML
    private Label label_user_id;

    private ServiceUtilisateur us = new ServiceUtilisateur();
    private UserDataManager userDataManager = UserDataManager.getInstance();
    int CurrentUserId = userDataManager.getIdu();
    Utilisateur currentUser = us.getUser(CurrentUserId);

    @FXML
    void Cancel(ActionEvent event) throws IOException {
        Stage stage = (Stage) Button_cancel.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
    }



    @FXML
    void CheckCode(ActionEvent event) throws SQLException, IOException {
        String mdpu = hiddenpassword1.getText().trim();
        String confirmMdp = hiddenpassword2.getText().trim();

        // Vérifier si un utilisateur est actuellement connecté
        if (currentUser == null) {
            System.out.println("Aucun utilisateur connecté");
            return;
        }

        // Vérifier si les champs sont vides
        if (mdpu.isEmpty() || confirmMdp.isEmpty()) {
            System.out.println("ERREUR : Les champs du mot de passe ne doivent pas être vides.");
            return;
        }

        // Vérifier si les mots de passe correspondent
        if (!mdpu.equals(confirmMdp)) {
            System.out.println("ERREUR : Les mots de passe ne correspondent pas.");
            return;
        }

        // Vérifier si le mot de passe est sécurisé
        if (!us.isMdp(mdpu)) {
            System.out.println("ERREUR : Le mot de passe n'est pas sécurisé.");
            return;
        }

        // Mise à jour du mot de passe de l'utilisateur
        currentUser.setMdpu(mdpu);
        us.update(currentUser);

        System.out.println("Mot de passe mis à jour avec succès !");

        // Changer de scène après la mise à jour
        Stage stage = (Stage) hiddenpassword1.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
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
}
