package tn.esprit.controllers;
import javafx.geometry.Pos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tn.esprit.models.Match;
import tn.esprit.services.ServiceMatch;
import tn.esprit.models.Tournoi;
import javafx.application.Platform;

import java.util.Comparator;
import java.util.stream.Collectors;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Tournoi;
import tn.esprit.services.ServiceTournoi;
import javafx.scene.chart.PieChart;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.scene.layout.StackPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import java.util.stream.Collectors;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import com.google.api.client.auth.oauth2.Credential;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MatchFront implements Initializable {

    @FXML
    private VBox cardContainer;

    private final ServiceMatch sm = new ServiceMatch();
    private Tournoi tournoiActuel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        afficherMatchs();
    }

    public void setTournoi(Tournoi tournoi) {
        this.tournoiActuel = tournoi;
        System.out.println("Tournoi sélectionné : " + tournoi.getNomt());
        afficherMatchs();
    }

    private void afficherMatchs() {
        Platform.runLater(() -> {
            cardContainer.getChildren().clear();
            HBox currentRow = new HBox(10);
            currentRow.setAlignment(Pos.TOP_LEFT);

            int cardCount = 0;

            if (tournoiActuel == null) {
                System.out.println("❌ Aucun tournoi sélectionné !");
                return;
            }

            int tournoiId = tournoiActuel.getIdt();
            System.out.println("Tournoi sélectionné: " + tournoiId);

            for (Match m : sm.getAll()) {
                StackPane card = new StackPane();

                if (m.getIdt() == tournoiId) {
                    card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-padding: 20px; -fx-max-width: 300px; -fx-spacing: 15px; -fx-background-radius: 20px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 10); -fx-opacity: 0.95; -fx-transition: transform 0.3s ease, opacity 0.3s ease;");
                    ImageView backgroundImage = new ImageView();
                    backgroundImage.setFitWidth(200);
                    backgroundImage.setFitHeight(400);
                    Image image = new Image("lol1.jpg");
                    backgroundImage.setImage(image);
                    backgroundImage.setOpacity(0.3);
                    card.getChildren().add(backgroundImage);

                    VBox content = new VBox(10);
                    content.setAlignment(Pos.CENTER);

                    String formattedScore = m.getScore();
                    if (formattedScore.length() == 2) {
                        formattedScore = formattedScore.charAt(0) + "-" + formattedScore.charAt(1);
                    }

                    Label teamsLabel = new Label(m.getEquipe1() + " vs " + m.getEquipe2());
                    teamsLabel.setStyle("-fx-text-fill: #ffcc00; -fx-font-family: 'Courier New', monospace; -fx-font-size: 24px; -fx-font-weight: bold;");
                    Label dateLabel = new Label("Date: " + m.getDate_debutm());
                    dateLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-family: 'Courier New', monospace; -fx-font-size: 18px;");
                    Label scoreLabel = new Label("Score: " + formattedScore);
                    scoreLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-family: 'Courier New', monospace; -fx-font-size: 18px;");
                    Label statusLabel = new Label("Statut: " + m.getStatus());
                    statusLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-family: 'Courier New', monospace; -fx-font-size: 18px;");

                    content.getChildren().addAll(teamsLabel, dateLabel, scoreLabel, statusLabel);
                    card.getChildren().add(content);
                    card.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) { // Double-clic

                            // Ouvrir une nouvelle fenêtre avec le stream DLive
                            Stage stage = new Stage();
                            DLiveStreamViewer.startStream(stage); // Appel de la méthode statique
                        } else if (event.getClickCount() == 1) { // Simple clic

                        }
                    });
                    currentRow.getChildren().add(card);
                    cardCount++;

                    if (cardCount >= 4) {
                        cardContainer.getChildren().add(currentRow);
                        currentRow = new HBox(10);
                        currentRow.setAlignment(Pos.TOP_LEFT);
                        cardCount = 0;
                    }
                }
            }

            if (cardCount > 0) {
                cardContainer.getChildren().add(currentRow);
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}