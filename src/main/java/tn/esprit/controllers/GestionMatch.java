package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Match;
import tn.esprit.services.ServiceMatch;
import tn.esprit.models.Tournoi;
import javafx.application.Platform;

import java.time.LocalDate;
import java.util.Optional;

public class GestionMatch {

    @FXML private TextField tfIdm;
    @FXML private TextField tfIdt;
    @FXML private TextField tfEquipe1;
    @FXML private TextField tfEquipe2;
    @FXML private DatePicker dpDateDebutm;
    @FXML private TextField tfScore;
    @FXML private TextField tfStatus;
    @FXML private VBox cardContainer;
    private Match selectedMatch = null;
    private Tournoi tournoiActuel;

    private final IService<Match> sm = new ServiceMatch();
    @FXML
    public void setTournoi(Tournoi tournoi) {
        this.tournoiActuel = tournoi;
        System.out.println("Tournoi sélectionné : " + tournoi.getNomt());
        tfIdt.setText(String.valueOf(tournoi.getIdt())); // Afficher l'ID du tournoi

    }
    @FXML
    public void initialize() {
        refreshMatchesList();
        addInputRestrictions();
    }
    private void addInputRestrictions() {
        // Restreindre tfIdt aux nombres entiers positifs uniquement
        tfIdt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfIdt.setText(newValue.replaceAll("[^\\d]", "")); // Supprime tout caractère non numérique
            }
        });

        // Restreindre tfScore aux nombres uniquement
        tfScore.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfScore.setText(newValue.replaceAll("[^\\d]", "")); // Supprime tout caractère non numérique
            }
        });
    }

    private boolean validateInputs() {
        if (tfEquipe1.getText().isEmpty() || tfEquipe2.getText().isEmpty() || dpDateDebutm.getValue() == null ||
                tfScore.getText().isEmpty() || tfStatus.getText().isEmpty() || tfIdt.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return false;
        }

        // Vérification que l'ID du tournoi est un entier positif
        try {
            int idTournoi = Integer.parseInt(tfIdt.getText());
            if (idTournoi <= 0) {
                showAlert("Erreur", "L'ID du tournoi doit être un entier positif.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID du tournoi doit être un nombre entier.", Alert.AlertType.ERROR);
            return false;
        }

        // Vérification que le score contient uniquement des chiffres
        if (!tfScore.getText().matches("\\d+")) {
            showAlert("Erreur", "Le score ne peut contenir que des nombres.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    @FXML
    public void ajouterMatch(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }
        if ( tfEquipe1.getText().isEmpty() || tfEquipe2.getText().isEmpty() || dpDateDebutm.getValue() == null || tfScore.getText().isEmpty() || tfStatus.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        try {
            Match m = new Match();
            //m.setIdm(Integer.parseInt(tfIdm.getText()));
            m.setIdt(Integer.parseInt(tfIdt.getText()));
            m.setEquipe1(tfEquipe1.getText());
            m.setEquipe2(tfEquipe2.getText());
            m.setDate_debutm(dpDateDebutm.getValue());
            m.setScore(tfScore.getText());
            m.setStatus(tfStatus.getText());

            sm.add(m);
            refreshMatchesList();
            showAlert("Succès", "Match ajouté avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides pour les identifiants.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout du match.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void supprimerMatch(ActionEvent event) {
        if (selectedMatch == null) {
            showAlert("Erreur", "Veuillez sélectionner un match à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        Optional<Match> matchASupprimer = sm.getAll().stream()
                .filter(m -> m.getIdm() == selectedMatch.getIdm())
                .findFirst();

        if (matchASupprimer.isPresent()) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation");
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce match ?");
            Optional<ButtonType> result = confirmDialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                sm.delete(matchASupprimer.get());
                refreshMatchesList();
                showAlert("Succès", "Match supprimé avec succès!", Alert.AlertType.INFORMATION);
                clearFields();
            }
        } else {
            showAlert("Erreur", "Match introuvable.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void modifierMatch(ActionEvent event) {
        if (selectedMatch == null) {
            showAlert("Erreur", "Veuillez sélectionner un match à modifier.", Alert.AlertType.ERROR);
            return;
        }
        if (!validateInputs()) {
            return;
        }
        try {
            selectedMatch.setEquipe1(tfEquipe1.getText());
            selectedMatch.setEquipe2(tfEquipe2.getText());
            selectedMatch.setDate_debutm(dpDateDebutm.getValue());
            selectedMatch.setScore(tfScore.getText());
            selectedMatch.setStatus(tfStatus.getText());

            sm.update(selectedMatch);
            refreshMatchesList();
            showAlert("Succès", "Match modifié avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void refreshMatchesList() {
        Platform.runLater(() -> { // Exécuter sur le thread UI
            cardContainer.getChildren().clear(); // Efface les cartes précédentes
            HBox currentRow = new HBox(10);
            currentRow.setAlignment(Pos.TOP_LEFT);

            int cardCount = 0;

            // Vérifie si un tournoi est sélectionné
            if (tournoiActuel == null) {
                System.out.println("❌ Aucun tournoi sélectionné !");
                return;
            }

            int tournoiId = tournoiActuel.getIdt();
            System.out.println("✅ Tournoi sélectionné IDT: " + tournoiId);

            for (Match m : sm.getAll()) {
                if (m.getIdt() == tournoiId) { // Vérifie que le match appartient au tournoi sélectionné
                    VBox card = new VBox(10);
                    card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-padding: 20px; -fx-max-width: 300px; -fx-spacing: 15px; -fx-background-radius: 20px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 10); -fx-opacity: 0.95; -fx-transition: transform 0.3s ease, opacity 0.3s ease;");

                    Label team1Label = new Label("Equipe 1: " + m.getEquipe1());
                    team1Label.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 16px; -fx-font-family: 'Cambria', serif; -fx-font-weight: bold;");

                    Label team2Label = new Label("Equipe 2: " + m.getEquipe2());
                    team2Label.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 14px; -fx-font-family: 'Arial', sans-serif;");

                    Label dateLabel = new Label("Date: " + m.getDate_debutm());
                    dateLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Arial', sans-serif;");

                    Label scoreLabel = new Label("Score: " + m.getScore());
                    scoreLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Arial', sans-serif;");

                    Label statusLabel = new Label("Statut: " + m.getStatus());
                    statusLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 12px; -fx-font-family: 'Arial', sans-serif;");

                    card.getChildren().addAll(team1Label, team2Label, dateLabel, scoreLabel, statusLabel);

                    card.setOnMouseClicked(event -> {
                        selectedMatch = m;
                        remplirChamps(m);
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

    public void remplirChamps(Match m) {
        tfIdm.setText(String.valueOf(m.getIdm()));
        tfIdt.setText(String.valueOf(m.getIdt()));
        tfEquipe1.setText(m.getEquipe1());
        tfEquipe2.setText(m.getEquipe2());
        dpDateDebutm.setValue(m.getDate_debutm());
        tfScore.setText(m.getScore());
        tfStatus.setText(m.getStatus());
    }

    private void clearFields() {
        tfIdm.clear();
        tfIdt.clear();
        tfEquipe1.clear();
        tfEquipe2.clear();
        dpDateDebutm.setValue(null);
        tfScore.clear();
        tfStatus.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}