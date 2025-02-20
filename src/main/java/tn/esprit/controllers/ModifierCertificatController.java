package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Certificat;
import tn.esprit.interfaces.CertificatService;

import java.time.LocalDate;

public class ModifierCertificatController {

    @FXML
    private TextField nomCertificatField;

    @FXML
    private ComboBox<String> typeCertificatComboBox; // ComboBox pour le type

    @FXML
    private TextField scoreCertificatField;

    @FXML
    private DatePicker dateExpirationPicker;

    @FXML
    private ComboBox<String> etatComboBox; // ComboBox pour l'état

    @FXML
    private ComboBox<String> formationComboBox; // ComboBox pour la sélection de formation

    @FXML
    private ComboBox<String> utilisateurComboBox; // ComboBox pour la sélection d'utilisateur

    @FXML
    private Button modifierButton, annulerButton;

    private CertificatService certificatService = new CertificatService();
    private Certificat certificat;  // Certificat sélectionné
    private Runnable onUpdateSuccess; // Callback

    // Méthode d'initialisation pour charger les données du Certificat
    @FXML
    public void initData(Certificat certificat) {
        this.certificat = certificat;
        nomCertificatField.setText(certificat.getNomc());

        // Initialisation du ComboBox pour le type de certificat
        typeCertificatComboBox.getItems().setAll("Type 1", "Type 2", "Type 3"); // Modifier selon les types possibles
        typeCertificatComboBox.setValue(certificat.getTypec());

        // Initialisation du score
        scoreCertificatField.setText(String.valueOf(certificat.getScorec())); // Utilisation de setText() pour TextField


        // Initialisation du DatePicker pour la date d'expiration
        dateExpirationPicker.setValue(certificat.getDateExpirationc());

        // Initialisation du ComboBox pour l'état
        etatComboBox.getItems().setAll("Actif", "Terminé", "Annulé");
        etatComboBox.setValue(certificat.getEtatc());

        // Initialisation du ComboBox pour la formation
        // Assumes you have a method to get the list of formations
        formationComboBox.getItems().setAll("Formation 1", "Formation 2", "Formation 3"); // À adapter selon les formations
        formationComboBox.setValue(String.valueOf(certificat.getIdf())); // Met l'id de la formation

        // Initialisation du ComboBox pour l'utilisateur
        // Assumes you have a method to get the list of users
        utilisateurComboBox.getItems().setAll("Utilisateur 1", "Utilisateur 2", "Utilisateur 3"); // À adapter selon les utilisateurs
        utilisateurComboBox.setValue(String.valueOf(certificat.getIdu())); // Met l'id de l'utilisateur
    }

    public void setOnUpdateSuccess(Runnable runnable) {
        this.onUpdateSuccess = runnable;
    }

    @FXML
    private void initialize() {
        modifierButton.setOnAction(event -> modifieCertificat());
        annulerButton.setOnAction(event -> fermerFenetre());
    }

    @FXML
    private void modifieCertificat() {
        try {
            // Vérification du nom
            if (nomCertificatField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom du certificat ne peut pas être vide !");
                return;
            }

            // Vérification du type
            if (typeCertificatComboBox.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le type de certificat doit être sélectionné !");
                return;
            }

            // Vérification du score (modifié pour float)
            float score;
            try {
                score = Float.parseFloat(scoreCertificatField.getText()); // Parsing en float
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le score doit être un nombre valide !");
                return;
            }

            // Vérification de la date d'expiration
            LocalDate today = LocalDate.now();
            LocalDate dateExpiration = dateExpirationPicker.getValue();

            if (dateExpiration == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La date d'expiration doit être sélectionnée !");
                return;
            }

            if (dateExpiration.isBefore(today)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La date d'expiration ne peut pas être antérieure à aujourd'hui !");
                return;
            }

            // Vérification de l'état
            if (etatComboBox.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'état du certificat doit être sélectionné !");
                return;
            }

            // Vérification de la formation
            if (formationComboBox.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La formation doit être sélectionnée !");
                return;
            }

            // Vérification de l'utilisateur
            if (utilisateurComboBox.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'utilisateur doit être sélectionné !");
                return;
            }

            // Mise à jour des valeurs du certificat
            certificat.setNomc(nomCertificatField.getText());
            certificat.setTypec(typeCertificatComboBox.getValue());
            certificat.setScorec(score); // Utilisation de score en tant que float
            certificat.setDateExpirationc(dateExpiration);
            certificat.setEtatc(etatComboBox.getValue());
            certificat.setIdf(Integer.parseInt(formationComboBox.getValue())); // Assumer que la formation est un ID
            certificat.setIdu(Integer.parseInt(utilisateurComboBox.getValue())); // Assumer que l'utilisateur est un ID

            // Appel du service pour modifier le certificat
            certificatService.modifierCertificat(certificat);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le certificat a été modifié avec succès !");
            fermerFenetre();

            // Déclenchement du callback après modification
            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la modification.");
            e.printStackTrace();
        }
    }


    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) modifierButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
