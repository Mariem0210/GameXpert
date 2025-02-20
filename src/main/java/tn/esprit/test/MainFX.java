package tn.esprit.test;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {


    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML
           // FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterGiveaway.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCertificat.fxml"));
            Parent root = loader.load();

            // Configurer la scène
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ajouter Produit");
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'interface : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}