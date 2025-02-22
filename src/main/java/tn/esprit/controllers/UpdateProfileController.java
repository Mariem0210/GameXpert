package tn.esprit.controllers;
import tn.esprit.models.Coach;
import tn.esprit.models.Joueur;
import tn.esprit.models.Utilisateur;

import tn.esprit.services.UserDataManager;
import tn.esprit.services.ServiceUtilisateur;
import tn.esprit.utils.MyDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class UpdateProfileController implements Initializable {

    public TextField passer;
    @FXML
    public ImageView photoProfile;
    @FXML
    private Button CoursesButton;

    @FXML
    private Button DeleteButton;

    @FXML
    private TextField Emailused;

    @FXML
    private Button EventsButton;

    @FXML
    private Button JobsButton;

    @FXML
    private ImageView Photo_de_profil;

    @FXML
    private Button PostsButton;

    @FXML
    private Button ProfileButton;

    @FXML
    private Button Refreshbutton;

    @FXML
    private Button Submit;

    @FXML
    private Button Uploadbutton;

    @FXML
    private Label UserType;

    @FXML
    private TextField alphabeticalErr;

    @FXML
    private DatePicker birthdate1;

    @FXML
    private TextField email;

    @FXML
    private TextField emailinv;

    @FXML
    private ImageView eyeclosed;

    @FXML
    private ImageView eyeclosed1;
    @FXML
    private ImageView  urlimage;

    @FXML
    private ImageView eyeopen;

    @FXML
    private ImageView eyeopen1;

    @FXML
    private TextField fieldserr;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Button hide;

    @FXML
    private Button hide1;

    @FXML
    private Button logoutButton;

    @FXML
    private TextField nom;

    @FXML
    private TextField num_telephone;

    @FXML
    private TextField weakPassword;


    @FXML
    private TextField password1;

    @FXML
    private TextField password2;

    @FXML
    private TextField phone_err;

    @FXML
    private TextField passerr;

    @FXML
    private TextField prenom_tf;

    @FXML
    private Button show;

    @FXML
    private Button show1;

    @FXML
    private Button HomeButton;


    ServiceUtilisateur us=new ServiceUtilisateur();


    // int CurrentUserId;
    private UserDataManager userDataManager = UserDataManager.getInstance();


    int CurrentUserId = userDataManager.getIdu();
    Utilisateur currentUser = us.getUser(CurrentUserId);
    public String old_email;


    public UpdateProfileController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (currentUser != null) {
            nom.setText(currentUser.getNomu() != null ? currentUser.getNomu() : "");
            prenom_tf.setText(currentUser.getPrenomu() != null ? currentUser.getPrenomu() : "");
            num_telephone.setText(currentUser.getNumtelu() != 0 ? String.valueOf(currentUser.getNumtelu()) : "");
            email.setText(currentUser.getMailu() != null ? currentUser.getMailu() : "");
            UserType.setText(currentUser.getTypeu() != null ? currentUser.getTypeu().toString() : "");
            old_email = currentUser.getMailu();
            Image photo_profile=us.loadImage(currentUser.getPhoto_de_profile());
            photoProfile.setImage(photo_profile);
        }
    }

    @FXML
    void DeleteMyAccount(ActionEvent event) throws IOException {
        Stage stage=(Stage) logoutButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/VerificationDelete.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

    }
    @FXML
    void EditCoursesbutton(ActionEvent event) {

    }

    @FXML
    void EditEventsbutton(ActionEvent event) {

    }

    @FXML
    void EditJobsbutton(ActionEvent event) {

    }

    @FXML
    void EditPostsbutton(ActionEvent event) {

    }

    @FXML
    void EditProfilebutton(ActionEvent event) {

    }

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
    }


    @FXML
    void SaveChanges(ActionEvent event) throws IOException {
        // Récupération des valeurs
        String nomu = nom.getText().trim();
        String prenomu = prenom_tf.getText().trim();
        String num = num_telephone.getText().trim();
        String mailu = email.getText().trim();
        String mdpu = password.getText().trim();
        String confirmMdp = confirmPassword.getText().trim();

        // Vérifier si un utilisateur est actuellement connecté
        if (currentUser == null) {
            System.out.println("Aucun utilisateur connecté");
            return;
        }

        List<String> fields = Arrays.asList(nomu, prenomu, num, mailu, mdpu, confirmMdp);

        // Validation des champs
        if (!validateFields(fields)) {
            return;
        }

        // Vérification que le numéro de téléphone est bien un entier
        int numtelu;
        try {
            numtelu = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            System.out.println("Numéro de téléphone invalide");
            phone_err.setVisible(true);
            return;
        }

        // Mise à jour de l'objet utilisateur existant
        currentUser.setNomu(nomu);
        currentUser.setPrenomu(prenomu);
        currentUser.setNumtelu(numtelu);
        currentUser.setMailu(mailu);
        currentUser.setMdpu(mdpu);

        // Exécuter la mise à jour dans la base de données
        us.update(currentUser);
        System.out.println("Mise à jour effectuée avec succès !");

        // Changer de scène après la mise à jour
        Stage stage = (Stage) nom.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateProfile.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private boolean validateFields(List<String> fields) {
        boolean valid = true;

        if (fields.stream().anyMatch(String::isEmpty)) {
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


        if (!us.isAlpha(nom.getText()) || !us.isAlpha(prenom_tf.getText())) {
            alphabeticalErr.setVisible(true);
            valid = false;
        } else {
            alphabeticalErr.setVisible(false);
        }

        return valid;
    }







    @FXML
    void Hidepassword11(ActionEvent event) {
        confirmPassword.setText(password2.getText());
        confirmPassword.setVisible(true);
        password2.setVisible(false);
        hide1.setVisible(false);
        show1.setVisible(true);
    }

    @FXML
    void Showpassword(ActionEvent event) {
        password1.setText(password.getText());
        password1.setVisible(true);
        password.setVisible(false);
        hide.setVisible(true);
        show.setVisible(false);
    }
    @FXML
    void Hidepassword(ActionEvent event) {
        password.setText(password1.getText());
        password.setVisible(true);
        password1.setVisible(false);
        hide.setVisible(false);
        show.setVisible(true);

    }

    @FXML
    void Showpassword11(ActionEvent event) {
        password2.setText(confirmPassword.getText());
        password2.setVisible(true);
        confirmPassword.setVisible(false);
        hide1.setVisible(true);
        show1.setVisible(false);
    }

  /*  @FXML
    void UploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            urlimage.setText(imagePath);
            // Mettre à jour l'image dans l'interface graphique
            Image image = new Image(selectedFile.toURI().toString());
            Photo_de_profil.setImage(image);
            Photo_de_profil.setVisible(true);

        } else {
            System.out.println("Aucun fichier sélectionné");
        }
    }*/
    @FXML
    void RefreshPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateProfile.fxml"));
        Parent root = loader.load();

        // Get the current stage
        Stage stage = (Stage) Refreshbutton.getScene().getWindow();
        UserDataManager.getInstance().setIdu(currentUser.getIdu());
        // Set the new FXML file on the scene
        Scene scene = new Scene(root);
        stage.setScene(scene);

    }

    @FXML
    void goToHome(ActionEvent event) throws IOException {
        Stage stage = (Stage) HomeButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Home");
    }



}