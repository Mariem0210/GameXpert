package tn.esprit.controllers;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

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
        tfIdt.setText(String.valueOf(tournoi.getIdt()));
    }

    @FXML
    public void initialize() {
        refreshMatchesList();
        addInputRestrictions();
    }

    private void addInputRestrictions() {
        tfIdt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfIdt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tfScore.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfScore.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private boolean validateInputs() {
        if (tfEquipe1.getText().isEmpty() || tfEquipe2.getText().isEmpty() || dpDateDebutm.getValue() == null ||
                tfScore.getText().isEmpty() || tfStatus.getText().isEmpty() || tfIdt.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return false;
        }

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

        try {
            int idTournoi = Integer.parseInt(tfIdt.getText());
            String equipe1 = tfEquipe1.getText();
            String equipe2 = tfEquipe2.getText();
            LocalDate dateMatch = dpDateDebutm.getValue();
            LocalDate currentDate = LocalDate.now();

            // Vérifier si la date du match est dans le passé
            if (dateMatch.isBefore(currentDate)) {
                showAlert("Erreur", "La date du match ne peut pas être dans le passé.", Alert.AlertType.ERROR);
                return;
            }

            // Vérifier si le match existe déjà
            if (matchExiste(idTournoi, equipe1, equipe2)) {
                showAlert("Erreur", "Ce match entre ces deux équipes existe déjà.", Alert.AlertType.ERROR);
                return;
            }

            Match m = new Match();
            m.setIdt(idTournoi);
            m.setEquipe1(equipe1);
            m.setEquipe2(equipe2);
            m.setDate_debutm(dateMatch);
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

    // Méthode pour vérifier si un match entre ces deux équipes existe déjà dans le même tournoi
    private boolean matchExiste(int idTournoi, String equipe1, String equipe2) {
        return sm.getAll().stream()
                .anyMatch(m -> m.getIdt() == idTournoi &&
                        ((m.getEquipe1().equalsIgnoreCase(equipe1) && m.getEquipe2().equalsIgnoreCase(equipe2)) ||
                                (m.getEquipe1().equalsIgnoreCase(equipe2) && m.getEquipe2().equalsIgnoreCase(equipe1))));
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

    @FXML
    private void genererPDF(ActionEvent event) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("Liste des Matchs:");
            contentStream.newLineAtOffset(0, -20);

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            for (Match m : sm.getAll()) {
                String line = "Match " + m.getIdm() + ": " + m.getEquipe1() + " vs " + m.getEquipe2() +
                        " | Score: " + m.getScore() + " | Date: " + m.getDate_debutm();
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15);
            }

            contentStream.endText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            document.save(new File("match_list.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void remplirChamps(Match m) {
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

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
