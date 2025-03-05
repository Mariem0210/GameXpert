package tn.esprit.controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Certificat;
import tn.esprit.interfaces.CertificatService;
import java.sql.SQLException;
import java.io.IOException;
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
        certificatTilePane.getChildren().clear();
        certificatTilePane.setHgap(50);
        certificatTilePane.setVgap(120);

        try {
            List<Certificat> certificats = certificatService.recupererCertificats();

            for (Certificat certificat : certificats) {
                VBox certificatCard = new VBox(10);

                // Chargement de l'image depuis resources
                javafx.scene.image.Image backgroundImage = new Image(getClass().getResource("/1.png").toExternalForm());
                BackgroundImage background = new BackgroundImage(
                        backgroundImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, false, true)
                );

                certificatCard.setBackground(new Background(background)); // Appliquer l'image de fond

                certificatCard.setStyle("-fx-border-radius: 10; -fx-border-color: #8a2be2; -fx-padding: 15px;");
                certificatCard.setPrefSize(280, 320);


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

                Button deleteButton = new Button("Supprimer");
                deleteButton.setOnAction(event -> deleteCertificat(certificat));

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

                certificatCard.getChildren().addAll(
                        nomcLabel, typecLabel, scorecLabel,
                        etatcLabel, dateExpirationcLabel,
                        exportPDFButton, deleteButton, modifButton
                );

                certificatTilePane.getChildren().add(certificatCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les certificats.");
        }
    }

    public void exportCertificatToPDF(Certificat certificat) {
        Document document = new Document();
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le Certificat");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

            Stage stage = new Stage();
            fileChooser.setInitialFileName(certificat.getNomc() + "_Certificat.pdf");
            java.io.File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Charger l'image de fond
                PdfContentByte canvas = writer.getDirectContentUnder();
                InputStream bgStream = getClass().getResourceAsStream("/FinalCertificate.jpg");
                if (bgStream != null) {
                    byte[] bgBytes = bgStream.readAllBytes();
                    com.itextpdf.text.Image bgImage = com.itextpdf.text.Image.getInstance(bgBytes);
                    bgImage.setAbsolutePosition(0, 0);
                    bgImage.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                    canvas.addImage(bgImage);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Image d'arrière-plan non trouvée !");
                    return;
                }

                // Police pour les informations
                Font nameFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.BLUE);
                Font detailsFont = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLUE);

                // Nom précis du destinataire
                Paragraph name = new Paragraph(certificat.getNomc(), nameFont);
                name.setAlignment(Element.ALIGN_CENTER);

                // Créer un PdfPTable pour positionner précisément les informations
                PdfPTable infoTable = new PdfPTable(1);
                infoTable.setTotalWidth(500);
                infoTable.setLockedWidth(true);
                infoTable.setHorizontalAlignment(Element.ALIGN_CENTER);

                // Cellule pour le nom
                PdfPCell nameCell = new PdfPCell(name);
                nameCell.setBorder(Rectangle.NO_BORDER);
                nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                infoTable.addCell(nameCell);

                // Cellule pour le score et la date d'expiration sous forme de petite phrase
                String detailsText = String.format("Score: %.2f | Date d'Expiration: %s",
                        certificat.getScorec(), certificat.getDateExpirationc());
                Paragraph details = new Paragraph(detailsText, detailsFont);


                details.setAlignment(Element.ALIGN_CENTER);

                PdfPCell detailsCell = new PdfPCell(details);
                detailsCell.setBorder(Rectangle.NO_BORDER);
                detailsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                infoTable.addCell(detailsCell);

                // Positionner le tableau précisément
                infoTable.writeSelectedRows(0, -1, 50, 350, canvas);

                document.close();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Certificat exporté avec succès.");
            }
        } catch (DocumentException | IOException e) {
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