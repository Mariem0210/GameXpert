package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class LoadingController {

    @FXML
    private AnchorPane rootPane; // Injected via FXML

    @FXML
    public void initialize() {
        new Thread(() -> {
            try {
                // Simulate loading time
                Thread.sleep(3000);

                Platform.runLater(() -> {
                    try {
                        // Load the login screen
                        Parent loginRoot = FXMLLoader.load(getClass().getResource("/Login.fxml"));
                        Scene loginScene = new Scene(loginRoot);

                        // Get the current stage using the rootPane's scene
                        Stage currentStage = (Stage) rootPane.getScene().getWindow();
                        currentStage.setScene(loginScene);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}