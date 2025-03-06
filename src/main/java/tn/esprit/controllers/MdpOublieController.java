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
import javafx.scene.control.TextField;
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
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class MdpOublieController {

    @FXML
    private Button Button_cancel;

    @FXML
    private Button Button_continue;

    @FXML
    private TextField activateErr;

    @FXML
    private TextField errorField;

    @FXML
    private TextField fieldserr;

    @FXML
    private TextField usernametext;
    ServiceUtilisateur us = new ServiceUtilisateur();
    private String userEmail;
    private void send_SMS(int recipnum, int code){
      String ACCOUNT_SID = "AC0fcd6048de3c6bacd548c34f6dd605d0";
        String AUTH_TOKEN = "5a8b5d4cd090cdf050f48cc7076b5d2e";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String recipientNumber = "+216"+recipnum;
        String message = "Greetings Artist ,\n"
                + "Your password reset code is : "+code;


        Message twilioMessage = Message.creator(
                new PhoneNumber(recipientNumber),
                new PhoneNumber("+16168014581"),message).create();

        System.out.println("SMS envoyé : " + twilioMessage.getSid());
    }

        @FXML
        void Cancel(ActionEvent event) throws IOException {
            Stage stage = (Stage) Button_cancel.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Login.fxml"))));
            stage.setTitle("Login");
        }

        @FXML
        void Checklogin(ActionEvent event) throws IOException {
            String email = usernametext.getText().trim();

            if(email.isEmpty()) {
                fieldserr.setVisible(true);
                return;
            }

            try {
                if(us.isEmailAvailable(email)) {
                    errorField.setVisible(true);
                    return;
                }

                // Générer et sauvegarder le code
                int code = us.generer();
                us.updateResetCode(email, code);

                // Envoyer le code par SMS
                Utilisateur user = us.getUserByEmail(email);
                send_SMS(user.getNumtelu(), code);

                // Passer à l'écran de code
                Stage stage = (Stage) Button_continue.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Code.fxml"));
                Parent root = loader.load();
                CodeController controller = loader.getController();
                controller.setUserEmail(email);
                stage.setScene(new Scene(root));
                stage.setTitle("Code");

            } catch (SQLException e) {
                errorField.setText("Database error");
                errorField.setVisible(true);
                e.printStackTrace();
            }
        }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }


}