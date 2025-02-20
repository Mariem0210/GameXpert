package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Certificat;
import tn.esprit.interfaces.CertificatService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AjouterCertificatController {

    @FXML
    private TextField nomcField;
    @FXML
    private ComboBox<String> typecComboBox;
    @FXML
    private TextField scorecField;
    @FXML
    private ComboBox<String> etatcComboBox;
    @FXML
    private DatePicker dateExpirationcField;
    @FXML
    private TextField idfField;
    @FXML
    private TextField iduField;
    @FXML
    private Button afficherBtn;

    private CertificatService certificatService = new CertificatService();

    @FXML
    public void initialize() {
        typecComboBox.getItems().addAll("Participation", "Excellence", "Complétion");
        etatcComboBox.getItems().addAll("Valide", "Expiré", "Révoqué"); // Remplir le ComboBox 'etatc
    }

    @FXML
    public void ajouterCertificat(ActionEvent event) {
        try {
            if (nomcField.getText().isEmpty() || typecComboBox.getValue() == null || scorecField.getText().isEmpty()
                    || etatcComboBox.getValue() == null || dateExpirationcField.getValue() == null
                    || idfField.getText().isEmpty() || iduField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs !");
                return;
            }

            int score;
            int idf;
            int idu;

            try {
                score = Integer.parseInt(scorecField.getText());
                if (score < 0 || score > 100) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de score", "Le score doit être entre 0 et 100.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer un nombre valide pour le score.");
                return;
            }

            try {
                idf = Integer.parseInt(idfField.getText());
                idu = Integer.parseInt(iduField.getText());
                if (idf <= 0 || idu <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Erreur d'ID", "Les ID doivent être des nombres positifs.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer des nombres valides pour les ID.");
                return;
            }

            LocalDate dateExpirationc = dateExpirationcField.getValue();
            if (dateExpirationc.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Erreur de date", "La date d'expiration ne peut pas être dans le passé.");
                return;
            }

            Certificat certificat = new Certificat();
            certificat.setNomc(nomcField.getText());
            certificat.setTypec(typecComboBox.getValue());
            certificat.setScorec(score);
            certificat.setEtatc(etatcComboBox.getValue()); // Utiliser la valeur sélectionnée dans le ComboBox
            certificat.setDateExpirationc(dateExpirationc);
            certificat.setIdf(idf);
            certificat.setIdu(idu);

            certificatService.add(certificat);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Certificat ajouté avec succès !");
            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomcField.clear();
        typecComboBox.setValue(null);
        scorecField.clear();
        etatcComboBox.setValue(null);
        dateExpirationcField.setValue(null);
        idfField.clear();
        iduField.clear();
    }

    @FXML
    public void affichebtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCertificats.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Certificats");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de l'interface AfficherCertificats.fxml : " + e.getMessage());
        }
    }
}
