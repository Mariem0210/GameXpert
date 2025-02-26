package tn.esprit.controllers;

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

public class GestionTournoi {

    @FXML private TextField tfNomt;
    @FXML private TextField tfDescriptiont;
    @FXML private DatePicker dpDateDebutt;
    @FXML private DatePicker dpDateFint;
    @FXML private TextField tfNbrEquipes;
    @FXML private TextField tfPrixt;
    @FXML private TextField tfStatutt;
    @FXML private VBox cardContainer;
    private Tournoi selectedTournoi = null;

    private final IService<Tournoi> st = new ServiceTournoi();

    @FXML
    public void initialize() {
        refreshTournoisList();
        addInputRestrictions();

        cardContainer.setOnMouseClicked(event -> {
            if (selectedTournoi != null) {
                remplirChamps(selectedTournoi);
            }
        });
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
    public void ajouterTournoi(ActionEvent actionEvent) {
        if (!validateFields()) {
            return;
        }
        try {
            Tournoi t = new Tournoi();
            t.setNomt(tfNomt.getText());
            t.setDescriptiont(tfDescriptiont.getText());
            t.setDate_debutt(dpDateDebutt.getValue());
            t.setDate_fint(dpDateFint.getValue());
            t.setNbr_equipes(Integer.parseInt(tfNbrEquipes.getText()));
            t.setPrixt(Float.parseFloat(tfPrixt.getText()));
            t.setStatutt(tfStatutt.getText());

            st.add(t);
            refreshTournoisList();
            showAlert("Succès", "Tournoi ajouté avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
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
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        for (Tournoi t : tournois) {
            data.add(new PieChart.Data(t.getNomt(), t.getNbr_equipes()));
        }

        PieChart pieChart = new PieChart(data);
        pieChart.setTitle("Nombre d'équipes par tournoi");

        // Affichage dans une nouvelle fenêtre
        Stage stage = new Stage();
        VBox vbox = new VBox(pieChart);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 500, 500);
        stage.setTitle("Statistiques des Tournois");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void searchTournoi(ActionEvent event) {
        String searchQuery = tfNomt.getText().toLowerCase();  // Assuming you want to search by the tournament name

        if (searchQuery.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un terme de recherche.", Alert.AlertType.ERROR);
            return;
        }

        List<Tournoi> filteredTournois = st.getAll().stream()
                .filter(t -> t.getNomt().toLowerCase().contains(searchQuery) || t.getDescriptiont().toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());

        // If no results are found
        if (filteredTournois.isEmpty()) {
            showAlert("Aucun résultat", "Aucun tournoi trouvé pour ce critère.", Alert.AlertType.INFORMATION);
        }

        // Refresh the list with the filtered tournaments
        cardContainer.getChildren().clear();
        HBox currentRow = new HBox(10);
        currentRow.setAlignment(Pos.TOP_LEFT);

        int cardCount = 0;

        for (Tournoi t : filteredTournois) {
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
}
