package tn.esprit.controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.SnapshotResult;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import com.itextpdf.text.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Certificat;
import tn.esprit.interfaces.CertificatService;
import java.sql.SQLException;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.List;
import java.util.Optional;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.stage.FileChooser;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;






public class AfficherCertificatController {
    @FXML
    private TilePane certificatTilePane;


    private CertificatService certificatService = new CertificatService();

    @FXML
    public void initialize() {
        loadCertificats();
    }

    @FXML
    public void loadCertificats() {
        certificatTilePane.getChildren().clear(); // Vider le conteneur avant d'ajouter de nouveaux éléments

        HBox currentRow = new HBox(10); // Créer une ligne pour les cartes
        currentRow.setAlignment(Pos.TOP_LEFT); // Aligner les cartes en haut à gauche

        int cardCount = 0; // Compteur pour le nombre de cartes dans la ligne actuelle

        try {
            List<Certificat> certificats = certificatService.recupererCertificats();

            for (Certificat certificat : certificats) {
                VBox certificatCard = new VBox(10); // Créer une carte sous forme de VBox
                certificatCard.setStyle("-fx-border-radius: 10; -fx-border-color: #8a2be2; -fx-padding: 15px;");
                certificatCard.setPrefSize(280, 320);


                // Ajouter les informations du certificat
                Label nomcLabel = new Label("Nom: " + certificat.getNomc());
                Label typecLabel = new Label("Type: " + certificat.getTypec());
                Label scorecLabel = new Label("Score: " + certificat.getScorec());
                Label etatcLabel = new Label("État: " + certificat.getEtatc());
                Label dateExpirationcLabel = new Label("Expiration: " + certificat.getDateExpirationc());

                // Bouton Exporter PDF
                Button exportPDFButton = new Button("Exporter PDF");
                exportPDFButton.setStyle("-fx-background-color: linear-gradient(to bottom, #4CAF50, #2E7D32);" +
                        "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 255, 0, 0.7), 10, 0.5, 0, 0); -fx-cursor: hand;");
                exportPDFButton.setOnAction(event -> exportCertificatToPDF(certificat));

                // Bouton Supprimer
                Button deleteButton = new Button("Supprimer");
                deleteButton.setOnAction(event -> deleteCertificat(certificat));

                // Bouton Modifier
                Button modifButton = new Button("Modifier");
                modifButton.setOnAction(event -> {
                    if (certificat.getNomc() == null || certificat.getNomc().isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom du certificat est vide.");
                        return;
                    }

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de modification");
                    alert.setHeaderText(null);
                    alert.setContentText("Voulez-vous vraiment modifier ce certificat ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCertificat.fxml"));
                            Scene scene = new Scene(loader.load());

                            ModifierCertificatController controller = loader.getController();
                            controller.initData(certificat);
                            controller.setOnUpdateSuccess(() -> loadCertificats());

                            Stage stage = new Stage();
                            stage.setTitle("Modifier Certificat");
                            stage.setScene(scene);
                            stage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire de modification.");
                        }
                    }
                });

                // Ajouter les éléments à la carte
                certificatCard.getChildren().addAll(
                        nomcLabel, typecLabel, scorecLabel,
                        etatcLabel, dateExpirationcLabel,
                        exportPDFButton, deleteButton, modifButton
                );

                // Ajouter la carte à la ligne actuelle
                currentRow.getChildren().add(certificatCard);
                cardCount++;

                // Si la ligne est pleine (4 cartes), ajouter la ligne au conteneur et créer une nouvelle ligne
                if (cardCount >= 4) {
                    certificatTilePane.getChildren().add(currentRow);
                    currentRow = new HBox(10);
                    currentRow.setAlignment(Pos.TOP_LEFT);
                    cardCount = 0;
                }
            }

            // Ajouter la dernière ligne si elle contient des cartes
            if (cardCount > 0) {
                certificatTilePane.getChildren().add(currentRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les certificats.");
        }
    }
    public void exportCertificatToPDF(Certificat certificat) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Certif.fxml"));
            AnchorPane root = loader.load();

            // Remplir les données dynamiques dans le FXML
            Label nomLabel = (Label) root.lookup("#nomLabel");
            if (nomLabel != null) {
                nomLabel.setText(certificat.getNomc());
            }

            Label scoreLabel = (Label) root.lookup("#scoreLabel");
            if (scoreLabel != null) {
                scoreLabel.setText("Score: " + certificat.getScorec());
            }

            Label dateLabel = (Label) root.lookup("#dateLabel");
            if (dateLabel != null) {
                dateLabel.setText("Date d'expiration: " + certificat.getDateExpirationc());
            }

            // Créer une scène temporaire pour capturer le rendu
            Scene scene = new Scene(root);
            Stage tempStage = new Stage();
            tempStage.setScene(scene);
            tempStage.show(); // Afficher la scène pour forcer le rendu

            // Attendre que la scène soit rendue
            Platform.runLater(() -> {
                try {
                    // Vérifier les dimensions de la scène
                    if (scene.getWidth() <= 0 || scene.getHeight() <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Les dimensions de la scène sont invalides.");
                        return;
                    }

                    // Prendre un snapshot de la scène
                    WritableImage writableImage = new WritableImage((int) scene.getWidth(), (int) scene.getHeight());
                    scene.snapshot(writableImage);

                    // Ouvrir une boîte de dialogue pour enregistrer le PDF
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Enregistrer le Certificat");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
                    fileChooser.setInitialFileName(certificat.getNomc() + "_Certificat.pdf");
                    File file = fileChooser.showSaveDialog(new Stage());

                    if (file != null) {
                        // Convertir l'image JavaFX en BufferedImage
                        java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

                        // Enregistrer l'image dans un fichier temporaire
                        File tempFile = File.createTempFile("certificat", ".png");
                        ImageIO.write(bufferedImage, "png", tempFile);

                        // Créer un document PDF
                        Document document = new Document(PageSize.A4);
                        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                        document.open();

                        // Ajouter l'image au PDF
                        Image pdfImage = Image.getInstance(tempFile.getAbsolutePath()); // Utilisation correcte de Image.getInstance()
                        pdfImage.scaleAbsolute(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                        pdfImage.setAbsolutePosition(0, 0);
                        document.add(pdfImage);

                        // Fermer le document
                        document.close();

                        // Supprimer le fichier temporaire
                        tempFile.delete();

                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Certificat exporté avec succès.");
                    }
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation du certificat.");
                } finally {
                    tempStage.close(); // Fermer la fenêtre temporaire
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation du certificat.");
        }
    }

    private void deleteCertificat(Certificat certificat) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce certificat ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    certificatService.supprimerCertificat(certificat, certificat.getNomc());
                    loadCertificats();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le certificat.");
                }
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}