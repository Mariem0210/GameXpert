package tn.esprit.controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
                VBox certificatCard = new VBox(5);
                certificatCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #1e1e2e;");
                certificatCard.setPrefSize(200, 300);

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
        Document document = new Document(PageSize.A4);
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le Certificat");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

            Stage stage = new Stage();
            fileChooser.setInitialFileName(certificat.getNomc() + ".pdf");
            java.io.File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Encadrement décoratif
                PdfContentByte canvas = writer.getDirectContent();
                Rectangle rect = new Rectangle(36, 36, 559, 806);
                rect.setBorder(Rectangle.BOX);
                rect.setBorderWidth(8);
                rect.setBorderColor(BaseColor.DARK_GRAY);
                canvas.rectangle(rect);

                // Titre principal
                Font titleFont = new Font(Font.FontFamily.HELVETICA, 28, Font.BOLD, BaseColor.BLACK);
                Paragraph title = new Paragraph("Certificat de Réussite", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingBefore(50);
                document.add(title);

                // Sous-titre
                Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.ITALIC, BaseColor.GRAY);
                Paragraph subTitle = new Paragraph("Ce certificat est décerné à", subTitleFont);
                subTitle.setAlignment(Element.ALIGN_CENTER);
                subTitle.setSpacingBefore(30);
                document.add(subTitle);

                // Nom du destinataire
                Font nameFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.BLUE);
                Paragraph name = new Paragraph(certificat.getNomc(), nameFont);
                name.setAlignment(Element.ALIGN_CENTER);
                name.setSpacingBefore(20);
                document.add(name);

                // Détails du certificat
                Font bodyFont = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
                Paragraph details = new Paragraph(
                        "Type : " + certificat.getTypec() + "\n" +
                                "Score : " + certificat.getScorec() + "\n" +
                                "État : " + certificat.getEtatc() + "\n" +
                                "Date d'expiration : " + certificat.getDateExpirationc(),
                        bodyFont
                );
                details.setAlignment(Element.ALIGN_CENTER);
                details.setSpacingBefore(30);
                document.add(details);

                // Signature et Date
                Paragraph space = new Paragraph("\n\n\n");
                document.add(space);

                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(80);
                table.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell dateCell = new PdfPCell(new Phrase("Date : ....................."));
                dateCell.setBorder(Rectangle.NO_BORDER);
                dateCell.setHorizontalAlignment(Element.ALIGN_LEFT);

                PdfPCell signCell = new PdfPCell(new Phrase("Signature : ....................."));
                signCell.setBorder(Rectangle.NO_BORDER);
                signCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

                table.addCell(dateCell);
                table.addCell(signCell);

                document.add(table);

                document.close();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Certificat exporté avec succès en PDF.");
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