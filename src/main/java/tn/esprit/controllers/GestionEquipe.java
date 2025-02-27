package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.application.Platform;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Equipe;
import tn.esprit.services.ServiceEquipe;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class GestionEquipe {

    @FXML private TextField tfNomEquipe;
    @FXML private TextField tfRecherche;  // Champ de recherche
    @FXML private DatePicker dpDateCreation;
    @FXML private VBox equipeContainer;

    private Equipe selectedEquipe = null;
    private final IService<Equipe> se = new ServiceEquipe();

    @FXML
    public void initialize() {
        refreshEquipesList(se.getAll());  // Afficher toutes les équipes au début
    }

    private boolean validateInputs() {
        if (tfNomEquipe.getText().isEmpty()) {
            showAlert("Erreur", "Le nom de l'équipe est obligatoire.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private Image generateLogoForTeam(String teamName) {
        try {
            // Utilisation de Robohash pour générer un logo unique basé sur le nom de l'équipe
            String urlString = "https://robohash.org/" + teamName + "?set=set2"; // set2 est un style pour générer un logo
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Récupération de l'image en entrée
            InputStream inputStream = connection.getInputStream();
            Image logo = new Image(inputStream);

            return logo;
        } catch (Exception e) {
            System.out.println("Erreur lors de la génération du logo : " + e.getMessage());
            return null;
        }
    }

    @FXML
    public void ajouterEquipe(ActionEvent actionEvent) {
        if (!validateInputs()) return;

        String nomEquipe = tfNomEquipe.getText().trim().toLowerCase();
        boolean existe = se.getAll().stream()
                .anyMatch(e -> e.getNom_equipe().trim().toLowerCase().equals(nomEquipe));

        if (existe) {
            showAlert("Erreur", "Impossible de créer cette équipe, ce nom existe déjà.", Alert.AlertType.ERROR);
            return;
        }

        try {
            Equipe e = new Equipe();
            e.setNom_equipe(tfNomEquipe.getText().trim());
            e.setDate_creation(new Date());
            se.add(e);
            refreshEquipesList(se.getAll());
            showAlert("Succès", "Équipe ajoutée avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout de l'équipe.", Alert.AlertType.ERROR);
        }
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
            contentStream.showText("Liste des equipes");
            contentStream.newLineAtOffset(0, -20);

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            for (Equipe e : se.getAll()) {
                String line = "equipe " + e.getIdeq() + " : " + e.getNom_equipe() + " crée le " + e.getDate_creation() ;

                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15);
            }

            contentStream.endText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            document.save(new File("liste d'equipe.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimerEquipe(ActionEvent event) {
        if (selectedEquipe == null) {
            showAlert("Erreur", "Veuillez sélectionner une équipe à supprimer.", Alert.AlertType.ERROR);
            return;
        }
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer cette équipe ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            se.delete(selectedEquipe);
            refreshEquipesList(se.getAll());
            showAlert("Succès", "Équipe supprimée avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        }
    }

    @FXML
    public void refreshEquipesList(List<Equipe> equipes) {
        Platform.runLater(() -> {
            equipeContainer.getChildren().clear();

            HBox currentRow = new HBox(10);
            currentRow.setAlignment(Pos.TOP_LEFT);
            int cardCount = 0;

            for (Equipe e : equipes) {
                StackPane card = new StackPane();
                card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-radius: 20px; -fx-padding: 20px;");

                // Ajout de l'image de fond générée par Robohash
                Image logo = generateLogoForTeam(e.getNom_equipe());
                if (logo != null) {
                    ImageView backgroundImage = new ImageView(logo);
                    backgroundImage.setFitWidth(200);
                    backgroundImage.setFitHeight(300);
                    backgroundImage.setOpacity(0.3);
                    card.getChildren().add(backgroundImage);
                }

                // Créer le contenu de la carte
                VBox content = new VBox(10);
                content.setAlignment(Pos.CENTER);

                // Affichage du nom de l'équipe
                Label lblNom = new Label(e.getNom_equipe());
                lblNom.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 18px;");

                // Affichage de l'ID de l'équipe
                Label lblId = new Label("ID: " + e.getIdeq());
                lblId.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 14px;");

                // Affichage de la date de création de l'équipe
                String dateString = Instant.ofEpochMilli(e.getDate_creation().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString();
                Label lblDate = new Label("Créé le: " + dateString);
                lblDate.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 14px;");

                // Ajouter les informations à la VBox
                content.getChildren().addAll(lblNom, lblId, lblDate);

                // Ajouter le contenu dans la carte
                card.getChildren().add(content);

                // Lorsque l'utilisateur clique sur la carte, remplir les champs
                card.setOnMouseClicked(event -> remplirChamps(e));

                // Ajouter la carte à la ligne courante
                currentRow.getChildren().add(card);
                cardCount++;

                // Si on a atteint 4 cartes, ajouter la ligne dans le conteneur
                if (cardCount >= 4) {
                    equipeContainer.getChildren().add(currentRow);
                    currentRow = new HBox(10);
                    cardCount = 0;
                }
            }

            // Ajouter la dernière ligne si elle contient des cartes
            if (cardCount > 0) equipeContainer.getChildren().add(currentRow);
        });
    }



    public void remplirChamps(Equipe e) {
        selectedEquipe = e;
        tfNomEquipe.setText(e.getNom_equipe());
        dpDateCreation.setValue(e.getDate_creation().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    @FXML
    private void modifierEquipe(ActionEvent event) {
        if (selectedEquipe == null || !validateInputs()) {
            showAlert("Erreur", "Veuillez sélectionner une équipe à modifier.", Alert.AlertType.ERROR);
            return;
        }
        try {
            selectedEquipe.setNom_equipe(tfNomEquipe.getText());
            se.update(selectedEquipe);
            refreshEquipesList(se.getAll());
            showAlert("Succès", "Équipe modifiée avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification de l'équipe.", Alert.AlertType.ERROR);
        }
    }

    private void clearFields() {
        tfNomEquipe.clear();
        dpDateCreation.setValue(null);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode de recherche dynamique
    @FXML
    private void rechercherEquipe() {
        String rechercheText = tfRecherche.getText().toLowerCase();  // Récupère le texte de recherche et le convertit en minuscule
        List<Equipe> equipesFiltered = se.getAll().stream()
                .filter(e -> e.getNom_equipe().toLowerCase().contains(rechercheText)) // Filtrer les équipes par nom
                .collect(Collectors.toList());
        refreshEquipesList(equipesFiltered);  // Mettre à jour la liste avec les équipes filtrées
    }
}