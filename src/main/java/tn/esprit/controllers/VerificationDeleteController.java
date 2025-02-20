package tn.esprit.controllers;

import tn.esprit.models.Utilisateur;
import tn.esprit.services.UserDataManager;
import tn.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class VerificationDeleteController {

    @FXML
    private Button Delete_Button;

    @FXML
    private PasswordField Hiddenpassword;

    @FXML
    private TextField Visiblepassword;

    @FXML
    private CheckBox checkbox;

    @FXML
    private TextField errorField;

    @FXML
    private TextField loggedinfield;

    @FXML
    private TextField usernametext;
    @FXML
    private Button Cancel_Button;
    private UserDataManager userDataManager = UserDataManager.getInstance();
    ServiceUtilisateur us =new ServiceUtilisateur();

    @FXML
    void ChangeVisibility(ActionEvent event) {
        if (checkbox.isSelected()) {
            Visiblepassword.setText(Hiddenpassword.getText());
            Visiblepassword.setVisible(true);
            Hiddenpassword.setVisible(false);
            return;
        }
        Hiddenpassword.setText(Visiblepassword.getText());
        Hiddenpassword.setVisible(true);
        Visiblepassword.setVisible(false);
    }

    @FXML
    void Cancel(ActionEvent event) throws IOException {
        Stage stage = (Stage) Cancel_Button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/UpdateProfile.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void Checklogin(ActionEvent event) throws SQLException, IOException {
        String mail = usernametext.getText();
        String password = us.getPassword(Visiblepassword,Hiddenpassword);
        int userId = us.getUserId(mail);
        int CurrentId=userDataManager.getIdu();
        Utilisateur user1=us.getUser(userId);
        Utilisateur user=us.getUser(CurrentId);


        if (us.verifierLogin(mail,password)&&user1.getMailu().equals(user.getMailu())){
            us.delete(user);
            errorField.setVisible(false);
            userDataManager.logout();
            user1=null;
            userId=0;
            Stage stage = (Stage) Delete_Button.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);



        }else {
            System.out.println("invalid !");
            errorField.setVisible(true);

        }

    }

}