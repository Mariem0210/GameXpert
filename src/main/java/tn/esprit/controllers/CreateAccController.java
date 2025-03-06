package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import tn.esprit.models.Coach;
import tn.esprit.models.Joueur;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.AuthService;
import tn.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CreateAccController implements Initializable {
    @FXML
    private TextField urlimage, nom, prenom_tf, num_telephone, email;
    @FXML
    private PasswordField password, confirmPassword;
    @FXML
    private DatePicker birthdate;
    @FXML
    private ChoiceBox<String> Choicebox;
    @FXML
    private ImageView eyeclosed, eyeclosed1, eyeopen, eyeopen1, Photo_de_profil;

    @FXML
    private TextField fieldserr, phone_err, emailinv, Emailused, passerr, weakPassword, alphabeticalErr;

    private final ServiceUtilisateur us = new ServiceUtilisateur();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Choicebox.getItems().addAll("COACH", "JOUEUR");
    }

    @FXML
    void Submitdonnes(ActionEvent event) throws IOException {
        if (!validateInputFields()) return;

        String nomu = nom.getText().trim();
        String prenomu = prenom_tf.getText().trim();
        String num = num_telephone.getText().trim();
        String mailu = email.getText().trim();
        String mdpu = password.getText().trim();
        LocalDate datenaissanceu = birthdate.getValue();
        String typeu = Choicebox.getValue();
        String path = urlimage.getText();

        int numtelu = Integer.parseInt(num); // This should already be validated.

        // Adding the user based on type
        if (typeu.equals("JOUEUR")) {
            us.add(new Joueur(nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu, path));
        } else {
            us.add(new Coach(nomu, prenomu, mailu, mdpu, numtelu, datenaissanceu, path));
        }

        // Change scene to login
        changeScene("/Login.fxml", "Bienvenue !");
    }

    private boolean validateInputFields() {
        String nomu = nom.getText().trim();
        String prenomu = prenom_tf.getText().trim();
        String num = num_telephone.getText().trim();
        String mailu = email.getText().trim();
        String mdpu = password.getText().trim();
        String confirmMdp = confirmPassword.getText().trim();
        LocalDate datenaissanceu = birthdate.getValue();
        String typeu = Choicebox.getValue();
        String path = urlimage.getText();

        // Check for required fields
        List<String> fields = Arrays.asList(nomu, prenomu, num, mailu, mdpu, confirmMdp, path);
        if (fields.stream().anyMatch(String::isEmpty) || datenaissanceu == null || typeu == null) {
            fieldserr.setVisible(true);
            return false;
        } else {
            fieldserr.setVisible(false);
        }

        // Validate email uniqueness
        if (!validateEmailUniqueness(mailu)) return false;

        // Check input validity
        if (!validatePhoneNumber(num) || !us.validateEmail(mailu) || !mdpu.equals(confirmMdp) || !us.isMdp(mdpu)
                || !us.isAlpha(nomu) || !us.isAlpha(prenomu)) {
            return false;
        }

        return true;
    }

    private boolean validateEmailUniqueness(String mailu) {
        try {
            if (us.emailExists(mailu)) {
                emailinv.setVisible(true);
                emailinv.setText("Cet email est déjà utilisé !");
                return false;
            } else {
                emailinv.setVisible(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            emailinv.setVisible(true);
            emailinv.setText("Erreur lors de la vérification de l'email.");
            return false;
        }
        return true;
    }

    private boolean validatePhoneNumber(String num) {
        if (!us.isValidPhoneNumber(num)) {
            phone_err.setVisible(true);
            return false;
        } else {
            phone_err.setVisible(false);
        }
        return true;
    }

    private void changeScene(String fxmlPath, String title) throws IOException {
        Stage stage = (Stage) nom.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
    }

    @FXML
    void goToLoginPage(ActionEvent event) throws IOException {
        changeScene("/Login.fxml", "Login Page");
    }

    @FXML
    void UploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            urlimage.setText(selectedFile.getAbsolutePath());
            Image image = new Image(selectedFile.toURI().toString());
            Photo_de_profil.setImage(image);
            Photo_de_profil.setVisible(true);
        } else {
            System.out.println("file is not valid");
        }
    }
    public void prefillGoogleUser(String nom, String prenom, String email, String photoUrl) {
        Platform.runLater(() -> {
            this.nom.setText(nom);
            this.prenom_tf.setText(prenom);
            this.email.setText(email);
            this.urlimage.setText(photoUrl);

            // Valeurs par défaut
            this.num_telephone.setText("00000000");
            this.birthdate.setValue(LocalDate.now().minusYears(18));
            this.Choicebox.setValue("JOUEUR");

            if (photoUrl != null && !photoUrl.isBlank()) {
                try {
                    Image image = new Image(photoUrl, true);
                    Photo_de_profil.setImage(image);
                } catch (Exception e) {
                    System.out.println("Erreur image: " + e.getMessage());
                }
            }
        });
    }


}
