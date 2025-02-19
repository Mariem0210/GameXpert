package tn.esprit.controllers;

import tn.esprit.models.Utilisateur;
import tn.esprit.services.UserDataManager;
import tn.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML
    private Button CoursesButton;

    @FXML
    private Button EventsButton;

    @FXML
    private Button ForumButton;

    @FXML
    private Button JobsButton;

    @FXML
    private Button MarketButton;

    @FXML
    private Button ProfileButton;

    @FXML
    private Label UserType;

    @FXML
    private Button UsersButton;

    @FXML
    private Button logoutButton;

    @FXML
    private ImageView photoProfile;

    @FXML
    private Label usernameOld;
    private UserDataManager userDataManager = UserDataManager.getInstance();
    private int CurrentUserId = userDataManager.getIdu();
    ServiceUtilisateur us=new ServiceUtilisateur();

    Utilisateur currentUser = us.getUser(CurrentUserId);

    public AdminDashboardController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {UserType.setText(currentUser.getTypeu().toString());
        usernameOld.setText(currentUser.getNomu());

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
    void goToCourses(ActionEvent event) {

    }

    @FXML
    void goToEvents(ActionEvent event) {

    }

    @FXML
    void goToForum(ActionEvent event) {

    }

    @FXML
    void goToJobs(ActionEvent event) {

    }

    @FXML
    void goToMarket(ActionEvent event) {

    }

    @FXML
    void goToUsers(ActionEvent event) throws IOException {
        Stage stage = (Stage) UsersButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/AdminDashboardUsers.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);stage.setTitle("Users management");
    }

}