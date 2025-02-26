package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Certificat;
import tn.esprit.models.Formation;
import tn.esprit.interfaces.CertificatService;
import tn.esprit.utils.MyDatabase;

import java.io.IOException;
import java.sql.*;
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
    private Formation formation; // Stocke la formation sélectionnée

    @FXML
    public void initialize() {
        typecComboBox.getItems().addAll("Participation", "Excellence", "Complétion");
        etatcComboBox.getItems().addAll("Valide", "Expiré", "Révoqué"); // Remplir le ComboBox 'etatc
    }

    @FXML
    public void ajouterCertificat(ActionEvent event) {
        try {
            // Vérification des champs
            if (nomcField.getText().isEmpty() || typecComboBox.getValue() == null || scorecField.getText().isEmpty()
                    || etatcComboBox.getValue() == null || dateExpirationcField.getValue() == null
                    || idfField.getText().isEmpty() || iduField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs !");
                return;
            }

            int score;
            int idu;

            // Validation du score
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

            // Conversion de l'ID de formation en int
            int idf;
            try {
                idf = Integer.parseInt(idfField.getText().trim());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur d'ID", "L'ID de la formation doit être un nombre valide.");
                return;
            }


            // Validation de l'ID utilisateur
            try {
                idu = Integer.parseInt(iduField.getText());
                if (idu <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Erreur d'ID", "L'ID Utilisateur doit être un nombre positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer un nombre valide pour l'ID User.");
                return;
            }

            // Validation de la date d'expiration
            LocalDate dateExpirationc = dateExpirationcField.getValue();
            if (dateExpirationc.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Erreur de date", "La date d'expiration ne peut pas être dans le passé.");
                return;
            }

            // Récupération du nom de la formation
            String nomFormation = getNomFormationById(idf);
            if (nomFormation == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune formation trouvée avec cet ID.");
                return;
            }

            // Création du certificat
            Certificat certificat = new Certificat();
            certificat.setNomc(nomcField.getText());
            certificat.setTypec(typecComboBox.getValue());
            certificat.setScorec(score);
            certificat.setEtatc(etatcComboBox.getValue());
            certificat.setDateExpirationc(dateExpirationc);
            certificat.setIdf(idf); // ID de formation en int
            certificat.setIdu(idu);

            // Ajouter le certificat
            certificatService.add(certificat);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Certificat ajouté avec succès !");
            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }



    private String getNomFormationById(int idf) {
        String nomFormation = null;
        try {
            // Connexion à la base de données
            Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/gamexpert", "root", "");
            Statement stmt = cnx.createStatement();
            // Requête SQL pour récupérer le nom de la formation
            ResultSet rs = stmt.executeQuery("SELECT nomf FROM formation WHERE idf = " + idf);

            // Vérifier si une formation avec l'ID donné existe
            if (rs.next()) {
                nomFormation = rs.getString("nomf"); // Récupérer le nom de la formation
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            cnx.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Impossible de charger le nom de la formation.");
        }
        return nomFormation;
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

    // Méthode pour recevoir la formation
  /* public void setFormation(Formation formation) {
        this.formation = formation;
        idfField.setText(" " + formation.getNomf());
    }*/
    public void setFormation(Formation formation) {
        this.formation = formation;
        idfField.setText(String.valueOf(formation.getIdf())); // Passer l'ID de la formation
    }


}