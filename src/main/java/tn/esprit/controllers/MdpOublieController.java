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

    private void send_SMS(int recipnum, int code){
        String ACCOUNT_SID = "AC69efe739a3ba6247ae6d72d9f48b9600";
        String AUTH_TOKEN = "aa1ff59b933917faac07cd2dc0ced175";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String recipientNumber = "+216"+recipnum;
        String message = "Greetings Artist ,\n"
                + "Your password reset code is : "+code;


        Message twilioMessage = Message.creator(
                new PhoneNumber(recipientNumber),
                new PhoneNumber("+16292586208"),message).create();

        System.out.println("SMS envoyé : " + twilioMessage.getSid());
    }
    @FXML
    void Cancel(ActionEvent event) throws IOException {
        Stage stage=(Stage) Button_cancel.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");

    }

    @FXML
    void Checklogin(ActionEvent event) throws InvalidAlgorithmParameterException, SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        if(usernametext.getText()=="")
        {
            fieldserr.setVisible(true);} else {
            fieldserr.setVisible(false);

        }

        if(!us.isEmailAvailable(usernametext.getText().toString()))
        {   errorField.setVisible(false);
            Utilisateur U=new Utilisateur(),u=new Utilisateur();
            U=us.getUser(us.getUserId(usernametext.getText().toString()));
            System.out.println(U);
            int code=0;
            System.out.println(U.getNumtelu());
            code=us.generer();
            System.out.println(code);
            send_SMS(U.getNumtelu(), code);
            Stage stage=(Stage) Button_continue.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Code.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Code");
            CodeController codecontroller=loader.getController();
            UserDataManager.getInstance().setUserId(U.getIdu());
            codecontroller.setCode(code);
        }
        else
        {
            errorField.setVisible(true);
        }


    }

}