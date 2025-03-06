package tn.esprit.controllers;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

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
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory; // Correct import
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar; // Correct import
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
public class GestionTournoi {

    @FXML private TextField tfNomt;
    @FXML private TextField tfNomt1;
    @FXML private TextField tfDescriptiont;
    @FXML private DatePicker dpDateDebutt;
    @FXML private DatePicker dpDateFint;
    @FXML private TextField tfNbrEquipes;
    @FXML private TextField tfPrixt;
    @FXML private TextField tfStatutt;
    @FXML private VBox cardContainer;
    @FXML private Button btnDateAscend; // Bouton pour trier par date ascendant
    @FXML private Button btnDateDescend;
    private Tournoi selectedTournoi = null;

    private final IService<Tournoi> st = new ServiceTournoi();

    @FXML
    public void initialize() {
        tfNomt1.textProperty().addListener((observable, oldValue, newValue) -> searchTournoi());
        refreshTournoisList();
        addInputRestrictions();
        btnDateAscend.setOnAction(event -> trierParDateAscend());
        btnDateDescend.setOnAction(event -> trierParDateDescend());

        cardContainer.setOnMouseClicked(event -> {
            if (selectedTournoi != null) {
                remplirChamps(selectedTournoi);
            }
        });
    }

    public void searchTournoi() {
        String searchQuery = tfNomt1.getText().toLowerCase();  // Get search query

        // Clear previous results
        cardContainer.getChildren().clear();

        if (searchQuery.isEmpty()) {
            // If the search field is empty, show all tournaments again
            List<Tournoi> allTournois = st.getAll();
            displayTournois(allTournois); // Display the full list of tournaments
            return; // Exit the method
        }

        // Filter tournaments dynamically based on the search query
        List<Tournoi> filteredTournois = st.getAll().stream()
                .filter(t -> t.getNomt().toLowerCase().contains(searchQuery) || t.getDescriptiont().toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());

        if (filteredTournois.isEmpty()) {
        } else {
            displayTournois(filteredTournois);
        }
    }

    // Method to display tournaments in the card container
    private void displayTournois(List<Tournoi> tournois) {
        HBox currentRow = new HBox(10);
        currentRow.setAlignment(Pos.TOP_LEFT);

        int cardCount = 0;

        for (Tournoi t : tournois) {
            StackPane card = new StackPane();
            card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-padding: 20px; -fx-max-width: 300px; -fx-spacing: 15px; -fx-background-radius: 20px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 10); -fx-opacity: 0.95; -fx-transition: transform 0.3s ease, opacity 0.3s ease;");

            ImageView backgroundImage = new ImageView();
            backgroundImage.setFitWidth(200);
            backgroundImage.setFitHeight(400);
            Image image = new Image("file:C:/Users/amine debbich/IdeaProjects/gameXpert/src/main/resources/lol.jpg");
            backgroundImage.setImage(image);
            backgroundImage.setOpacity(0.3);

            card.getChildren().add(backgroundImage);

            VBox content = new VBox(10);
            content.setAlignment(Pos.CENTER);

            Label nameLabel = new Label("Nom: " + t.getNomt());
            nameLabel.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 16px; -fx-font-family: 'Courier New', monospace; -fx-font-weight: bold;");

            Label descriptionLabel = new Label("Description: " + t.getDescriptiont());
            descriptionLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace; -fx-line-spacing: 4px;");

            Label startDateLabel = new Label("Début: " + t.getDate_debutt());
            startDateLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label endDateLabel = new Label("Fin: " + t.getDate_fint());
            endDateLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label teamsLabel = new Label("Équipes: " + t.getNbr_equipes());
            teamsLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label priceLabel = new Label("Prix: " + t.getPrixt());
            priceLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label statusLabel = new Label("Statut: " + t.getStatutt());
            statusLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            content.getChildren().addAll(nameLabel, descriptionLabel, startDateLabel, endDateLabel, teamsLabel, priceLabel, statusLabel);

            card.getChildren().add(content);

            // Set mouse click event for the card
            card.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    ouvrirGestionMatch(t);  // Double-click to open match management
                } else {
                    selectedTournoi = t;  // Single-click to select the tournament
                    remplirChamps(t);  // Fill fields with the selected tournament details
                }
            });

            currentRow.getChildren().add(card);
            cardCount++;

            // Add the current row when it's full
            if (cardCount >= 4) {
                cardContainer.getChildren().add(currentRow);
                currentRow = new HBox(10);
                currentRow.setAlignment(Pos.TOP_LEFT);
                cardCount = 0;
            }
        }

        // If there are remaining cards in the current row, add them to the container
        if (cardCount > 0) {
            cardContainer.getChildren().add(currentRow);
        }
    }

    private void addInputRestrictions() {
        tfNbrEquipes.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfNbrEquipes.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tfPrixt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                tfPrixt.setText(oldValue);
            }
        });
    }
    @FXML
    private void trierParDateAscend() {
        List<Tournoi> tournois = st.getAll();
        tournois.sort((t1, t2) -> t1.getDate_debutt().compareTo(t2.getDate_debutt())); // Tri croissant
        afficherTournois(tournois);
    }
    @FXML
    // Méthode pour trier les tournois par date descendant
    private void trierParDateDescend() {
        List<Tournoi> tournois = st.getAll();
        tournois.sort((t1, t2) -> t2.getDate_debutt().compareTo(t1.getDate_debutt())); // Tri décroissant
        afficherTournois(tournois);
    }

    @FXML  // Méthode pour afficher les tournois triés
    private void afficherTournois(List<Tournoi> tournois) {
        cardContainer.getChildren().clear();

        HBox currentRow = new HBox(10);
        currentRow.setAlignment(Pos.TOP_LEFT);

        int cardCount = 0;

        for (Tournoi t : tournois) {
            StackPane card = new StackPane();
            card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-padding: 20px; -fx-max-width: 300px; -fx-spacing: 15px; -fx-background-radius: 20px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 10); -fx-opacity: 0.95; -fx-transition: transform 0.3s ease, opacity 0.3s ease;");
            ImageView backgroundImage = new ImageView();
            backgroundImage.setFitWidth(200);
            backgroundImage.setFitHeight(400);
            Image image = new Image("lol.jpg");
            backgroundImage.setImage(image);
            backgroundImage.setOpacity(0.3);

            card.getChildren().add(backgroundImage);
            // Ajoutez le contenu de la carte ici (comme vous l'avez déjà fait)
            VBox content = new VBox(10);
            content.setAlignment(Pos.CENTER);

            Label nameLabel = new Label("Nom: " + t.getNomt());
            nameLabel.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 16px; -fx-font-family: 'Courier New', monospace; -fx-font-weight: bold;");

            // Ajoutez ici le reste des labels comme vous avez déjà fait

            nameLabel.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 16px; -fx-font-family: 'Courier New', monospace; -fx-font-weight: bold;");

            Label descriptionLabel = new Label("Description: " + t.getDescriptiont());
            descriptionLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace; -fx-line-spacing: 4px;");

            Label startDateLabel = new Label("Début: " + t.getDate_debutt());
            startDateLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label endDateLabel = new Label("Fin: " + t.getDate_fint());
            endDateLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label teamsLabel = new Label("Équipes: " + t.getNbr_equipes());
            teamsLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label priceLabel = new Label("Prix: " + t.getPrixt());
            priceLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label statusLabel = new Label("Statut: " + t.getStatutt());
            statusLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            content.getChildren().addAll(nameLabel, descriptionLabel, startDateLabel, endDateLabel, teamsLabel, priceLabel, statusLabel);// Ajoutez les autres labels ici

            card.getChildren().add(content);

            card.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    ouvrirGestionMatch(t);
                } else {
                    selectedTournoi = t;
                    remplirChamps(t);
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

        if (cardCount > 0) {
            cardContainer.getChildren().add(currentRow);
        }
    }
    @FXML
    public void ajouterTournoi(ActionEvent actionEvent) {
        if (!validateFields()) {
            return;
        }

        // Get the selected dates
        LocalDate dateDebut = dpDateDebutt.getValue();
        LocalDate dateFin = dpDateFint.getValue();

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Check if the start date is in the past
        if (dateDebut.isBefore(currentDate)) {
            showAlert("Erreur", "La date de début ne peut pas être dans le passé.", Alert.AlertType.ERROR);
            return;
        }

        // Check if the start date is after the end date
        if (dateDebut.isAfter(dateFin)) {
            showAlert("Erreur", "La date de début ne peut pas être après la date de fin.", Alert.AlertType.ERROR);
            return;
        }

        // Vérifier si le tournoi existe déjà
        String nomTournoi = tfNomt.getText();
        if (tournoiExiste(nomTournoi)) {
            showAlert("Erreur", "Un tournoi avec ce nom existe déjà.", Alert.AlertType.ERROR);
            return;
        }

        try {
            Tournoi t = new Tournoi();
            t.setNomt(nomTournoi);
            t.setDescriptiont(tfDescriptiont.getText());
            t.setDate_debutt(dateDebut);
            t.setDate_fint(dateFin);
            t.setNbr_equipes(Integer.parseInt(tfNbrEquipes.getText()));
            t.setPrixt(Float.parseFloat(tfPrixt.getText()));
            t.setStatutt(tfStatutt.getText());

            st.add(t);
            refreshTournoisList();
            showAlert("Succès", "Tournoi ajouté avec succès!", Alert.AlertType.INFORMATION);
            clearFields();

            // Ajouter le tournoi dans Challonge
            ajouterTournoiChallonge(nomTournoi, t.getDescriptiont(), dateDebut.toString(), dateFin.toString());

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }

    private void ajouterTournoiChallonge(String nomTournoi, String description, String dateDebut, String dateFin) {
        String apiKey = "gAxjGk8jFHLck2nDKvsPunKBqsuike4q18d58Mn0";
        String url = "https://api.challonge.com/v1/tournaments.json";

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("api_key", apiKey));
            params.add(new BasicNameValuePair("tournament[name]", nomTournoi));
            params.add(new BasicNameValuePair("tournament[description]", description));
            params.add(new BasicNameValuePair("tournament[start_at]", dateDebut));
            params.add(new BasicNameValuePair("tournament[end_at]", dateFin));

            post.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line;
            StringBuilder result = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("Tournoi ajouté avec succès dans Challonge: " + result.toString());
            } else {
                System.err.println("Erreur lors de l'ajout du tournoi dans Challonge: " + result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Méthode pour vérifier si un tournoi avec le même nom existe déjà
    private boolean tournoiExiste(String nomTournoi) {
        return st.getAll().stream().anyMatch(t -> t.getNomt().equalsIgnoreCase(nomTournoi));
    }



    private boolean validateFields() {
        if (tfNomt.getText().isEmpty() || tfDescriptiont.getText().isEmpty() ||
                dpDateDebutt.getValue() == null || dpDateFint.getValue() == null ||
                tfNbrEquipes.getText().isEmpty() || tfPrixt.getText().isEmpty() || tfStatutt.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            int nbrEquipes = Integer.parseInt(tfNbrEquipes.getText());
            if (nbrEquipes <= 0) {
                showAlert("Erreur", "Le nombre d'équipes doit être un entier positif.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le nombre d'équipes doit être un entier positif.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            float prix = Float.parseFloat(tfPrixt.getText());
            if (prix < 0) {
                showAlert("Erreur", "Le prix doit être un nombre positif.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    @FXML
    private void supprimerTournoi(ActionEvent event) {
        String selectedTournoiInfo = selectedTournoi != null ? selectedTournoi.getNomt() : null;

        if (selectedTournoiInfo == null) {
            showAlert("Erreur", "Veuillez sélectionner un tournoi à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        Optional<Tournoi> tournoiASupprimer = st.getAll().stream()
                .filter(t -> t.getNomt().equals(selectedTournoiInfo))
                .findFirst();

        if (tournoiASupprimer.isPresent()) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation");
            confirmDialog.setHeaderText(null);
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce tournoi ?");
            Optional<ButtonType> result = confirmDialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                st.delete(tournoiASupprimer.get());
                refreshTournoisList();
                showAlert("Succès", "Tournoi supprimé avec succès!", Alert.AlertType.INFORMATION);
                clearFields();
            }
        } else {
            showAlert("Erreur", "Tournoi introuvable.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void modifierTournoi(ActionEvent event) {
        if (selectedTournoi == null) {
            showAlert("Erreur", "Veuillez sélectionner un tournoi à modifier.", Alert.AlertType.ERROR);
            return;
        }
        if (!validateFields()) {
            return;
        }
        try {
            selectedTournoi.setNomt(tfNomt.getText());
            selectedTournoi.setDescriptiont(tfDescriptiont.getText());
            selectedTournoi.setDate_debutt(dpDateDebutt.getValue());
            selectedTournoi.setDate_fint(dpDateFint.getValue());
            selectedTournoi.setNbr_equipes(Integer.parseInt(tfNbrEquipes.getText()));
            selectedTournoi.setPrixt(Float.parseFloat(tfPrixt.getText()));
            selectedTournoi.setStatutt(tfStatutt.getText());

            st.update(selectedTournoi);
            refreshTournoisList();
            showAlert("Succès", "Tournoi modifié avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void refreshTournoisList() {
        cardContainer.getChildren().clear();

        HBox currentRow = new HBox(10);
        currentRow.setAlignment(Pos.TOP_LEFT);

        int cardCount = 0;

        for (Tournoi t : st.getAll()) {

            StackPane card = new StackPane();
            card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-padding: 20px; -fx-max-width: 300px; -fx-spacing: 15px; -fx-background-radius: 20px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 10); -fx-opacity: 0.95; -fx-transition: transform 0.3s ease, opacity 0.3s ease;");

            ImageView backgroundImage = new ImageView();
            backgroundImage.setFitWidth(200);
            backgroundImage.setFitHeight(400);
            Image image = new Image("lol.jpg");
            backgroundImage.setImage(image);
            backgroundImage.setOpacity(0.3);

            card.getChildren().add(backgroundImage);

            VBox content = new VBox(10);
            content.setAlignment(Pos.CENTER);

            Label nameLabel = new Label("Nom: " + t.getNomt());
            nameLabel.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 16px; -fx-font-family: 'Courier New', monospace; -fx-font-weight: bold;");

            Label descriptionLabel = new Label("Description: " + t.getDescriptiont());
            descriptionLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace; -fx-line-spacing: 4px;");

            Label startDateLabel = new Label("Début: " + t.getDate_debutt());
            startDateLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label endDateLabel = new Label("Fin: " + t.getDate_fint());
            endDateLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label teamsLabel = new Label("Équipes: " + t.getNbr_equipes());
            teamsLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label priceLabel = new Label("Prix: " + t.getPrixt());
            priceLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            Label statusLabel = new Label("Statut: " + t.getStatutt());
            statusLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");

            content.getChildren().addAll(nameLabel, descriptionLabel, startDateLabel, endDateLabel, teamsLabel, priceLabel, statusLabel);

            card.getChildren().add(content);

            card.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    ouvrirGestionMatch(t);
                } else {
                    selectedTournoi = t;
                    remplirChamps(t);
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

        if (cardCount > 0) {
            cardContainer.getChildren().add(currentRow);
        }
    }

    private void ouvrirGestionMatch(Tournoi tournoi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionMatch.fxml"));
            Parent root = loader.load();

            GestionMatch controller = loader.getController();
            controller.setTournoi(tournoi);

            Stage stage = new Stage();
            stage.setTitle("Gestion des Matchs - " + tournoi.getNomt());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface GestionMatch.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void afficherStatistiques(ActionEvent event) {
        List<Tournoi> tournois = st.getAll();

        if (tournois.isEmpty()) {
            showAlert("Information", "Aucun tournoi trouvé.", Alert.AlertType.INFORMATION);
            return;
        }

        // Création du PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Tournoi t : tournois) {
            pieChartData.add(new PieChart.Data(t.getNomt(), t.getNbr_equipes()));
        }
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Nombre d'équipes par tournoi");

        // Création du BarChart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tournoi");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'équipes");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Comparaison des Tournois");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nombre d'équipes");

        for (Tournoi t : tournois) {
            series.getData().add(new XYChart.Data<>(t.getNomt(), t.getNbr_equipes()));
        }

        barChart.getData().add(series);

        // Affichage des graphiques dans une fenêtre
        Stage stage = new Stage();
        HBox hbox = new HBox(20, pieChart, barChart);
        hbox.setPadding(new Insets(10));

        Scene scene = new Scene(hbox, 900, 500);
        stage.setTitle("Statistiques des Tournois");
        stage.setScene(scene);
        stage.show();
    }



    public void remplirChamps(Tournoi t) {
        tfNomt.setText(t.getNomt());
        tfDescriptiont.setText(t.getDescriptiont());
        dpDateDebutt.setValue(t.getDate_debutt());
        dpDateFint.setValue(t.getDate_fint());
        tfNbrEquipes.setText(String.valueOf(t.getNbr_equipes()));
        tfPrixt.setText(String.valueOf(t.getPrixt()));
        tfStatutt.setText(t.getStatutt());
    }

    private void clearFields() {
        tfNomt.clear();
        tfDescriptiont.clear();
        dpDateDebutt.setValue(null);
        dpDateFint.setValue(null);
        tfNbrEquipes.clear();
        tfPrixt.clear();
        tfStatutt.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void ouvrirChat(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chatbot.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("ChatBot");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showTournamentCalendar() {
        try {
            // Initialiser le service Google Calendar
            Calendar service = GoogleCalendarService.getCalendarService();

            // Récupérer et trier les tournois par date_debutt
            List<Tournoi> tournois = st.getAll().stream()
                    .sorted(Comparator.comparing(Tournoi::getDate_debutt))
                    .collect(Collectors.toList());

            // Ajouter les tournois au calendrier
            for (Tournoi t : tournois) {
                GoogleCalendarService.addEvent(service, t.getNomt(), t.getDate_debutt());
            }

            // Afficher un message de succès
            showAlert("Succès", "Les tournois ont été ajoutés au calendrier selon la date de début.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ajouter les tournois au calendrier : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
