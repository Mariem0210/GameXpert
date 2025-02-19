package tn.esprit.controllers;

import tn.esprit.models.Coach;
import tn.esprit.models.Joueur;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CreateAccController implements Initializable {

    @FXML
    private TextField nom, prenom_tf, num_telephone, email;
    @FXML
    private PasswordField password, confirmPassword;
    @FXML
    private DatePicker birthdate;
    @FXML
    private ChoiceBox<String> Choicebox;

    // Champs d'erreur
    @FXML
    private TextField fieldserr, phone_err, emailinv, Emailused, passerr, weakPassword, alphabeticalErr;

    private final ServiceUtilisateur us = new ServiceUtilisateur();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    void Submitdonnes(ActionEvent event) throws IOException {
        // Récupération des valeurs
        String nomu = nom.getText().trim();
        String prenomu = prenom_tf.getText().trim();
        String num = num_telephone.getText().trim();
        String mailu = email.getText().trim();
        String mdpu = password.getText().trim();
        String confirmMdp = confirmPassword.getText().trim();
        LocalDate datenaissanceu = birthdate.getValue();
        String typeu = Choicebox.getValue();

        // Vérification du numéro de téléphone
        int numtelu;
        try {
            numtelu = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            phone_err.setVisible(true);
            return;
        }

        // Vérification des champs obligatoires
        List<String> fields = Arrays.asList(nomu, prenomu, num, mailu, mdpu, confirmMdp);
        if (fields.stream().anyMatch(String::isEmpty) || datenaissanceu == null || typeu == null) {
            fieldserr.setVisible(true);
            return;
        } else {
            fieldserr.setVisible(false);
        }

        // Vérification des erreurs
        phone_err.setVisible(!us.isValidPhoneNumber(num));
        emailinv.setVisible(!us.validateEmail(mailu));
        passerr.setVisible(!mdpu.equals(confirmMdp));
        weakPassword.setVisible(!us.isMdp(mdpu));
        alphabeticalErr.setVisible(!us.isAlpha(nomu) || !us.isAlpha(prenomu));

        // Si toutes les validations sont OK
        if (us.isValidPhoneNumber(num) && us.validateEmail(mailu) &&
                  mdpu.equals(confirmMdp) &&
                us.isMdp(mdpu) && us.isAlpha(nomu) && us.isAlpha(prenomu)) {

             if(typeu.equals("JOUEUR")) {
                us.add(new Joueur( nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu));
            } else {
                us.add(new Coach( nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu));
            }




            // Changer de scène
            Stage stage = (Stage) nom.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Bienvenue !");
        }
    }

    private boolean validateFields(List<String> fields, LocalDate dateNaissance, String typeu) {
        boolean valid = true;

        if (!us.areFieldsNotEmpty(fields)) {
            fieldserr.setVisible(true);
            valid = false;
        } else {
            fieldserr.setVisible(false);
        }

        if (!us.isValidPhoneNumber(num_telephone.getText())) {
            phone_err.setVisible(true);
            valid = false;
        } else {
            phone_err.setVisible(false);
        }

        if (!us.validateEmail(email.getText())) {
            emailinv.setVisible(true);
            valid = false;
        } else {
            emailinv.setVisible(false);
        }

        if (!password.getText().equals(confirmPassword.getText())) {
            passerr.setVisible(true);
            valid = false;
        } else {
            passerr.setVisible(false);
        }

        if (!us.isMdp(password.getText())) {
            weakPassword.setVisible(true);
            valid = false;
        } else {
            weakPassword.setVisible(false);
        }

        boolean emailAvailable;
        try {
            emailAvailable = us.isEmailAvailable(email.getText());
            Emailused.setVisible(!emailAvailable);
            if (!emailAvailable) valid = false;
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la vérification de l'email : " + e.getMessage());
            Emailused.setVisible(false);
            valid = false;
        }

        if (!us.isAlpha(nom.getText())) {
            alphabeticalErr.setVisible(true);
            valid = false;
        } else {
            alphabeticalErr.setVisible(false);
        }

        if (!us.isAlpha(prenom_tf.getText())) {
            alphabeticalErr.setVisible(true);
            valid = false;
        } else {
            alphabeticalErr.setVisible(false);
        }

        return valid;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Choicebox.getItems().addAll("COACH", "JOUEUR");
    }

    @FXML
    void goToLoginPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Login.fxml"))));
    }
}
