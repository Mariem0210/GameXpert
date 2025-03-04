package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.chart.*;

import tn.esprit.models.Offre_de_recrutement;
import tn.esprit.services.ServiceOffre_de_recrutement;

import java.util.List;
import java.util.Optional;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


public class GestionOffre_de_recrutement {




        @FXML private VBox offreContainer;
        @FXML private TextField tfPosteRecherche, tfNiveauRequis, tfSalairePropose, tfStatus, tfContrat;
        private final ServiceOffre_de_recrutement serviceOffre = new ServiceOffre_de_recrutement();
        private Offre_de_recrutement selectedOffre = null;

        @FXML
        public void initialize() {
            System.out.println("initialize() exécutée");
            refreshOffresList();

        }

    @FXML
    private void afficherStatistiques(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Statistiques des Salaires");

        PieChart pieChart = new PieChart();

        List<Offre_de_recrutement> offres = serviceOffre.getAll();
        int bas = 0, moyen = 0, eleve = 0;

        for (Offre_de_recrutement offre : offres) {
            float salaire = offre.getSalaire_propose();
            if (salaire < 1000) bas++;
            else if (salaire <= 3000) moyen++;
            else eleve++;
        }

        pieChart.getData().add(new PieChart.Data("Bas (< 1000€)", bas));
        pieChart.getData().add(new PieChart.Data("Moyen (1000€ - 3000€)", moyen));
        pieChart.getData().add(new PieChart.Data("Élevé (> 3000€)", eleve));

        VBox vbox = new VBox(pieChart);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 500, 400);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
        public void refreshOffresList() {
            Platform.runLater(() -> {
                offreContainer.getChildren().clear();
                List<Offre_de_recrutement> offres = serviceOffre.getAll();
                System.out.println("Nombre d'offres récupérées: " + offres.size());

                HBox currentRow = new HBox(10);
                currentRow.setAlignment(Pos.TOP_LEFT);
                int cardCount = 0;

                for (Offre_de_recrutement offre : offres) {
                    StackPane card = new StackPane();
                    card.setPrefSize(150, 150);
                    card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-radius: 20px; -fx-padding: 20px;");

                    VBox content = new VBox(10);
                    content.setAlignment(Pos.CENTER);


                    Label lblPoste = new Label("Poste: " + offre.getPoste_recherche());
                    lblPoste.setStyle("-fx-text-fill: white;");
                    Label lblNiveau = new Label("Niveau Requis: " + offre.getNiveu_requis());
                    lblNiveau.setStyle("-fx-text-fill: white;");
                    Label lblSalaire = new Label("Salaire: " + offre.getSalaire_propose() + " €");
                    lblSalaire.setStyle("-fx-text-fill: white;");
                    Label lblStatus = new Label("Status: " + offre.getStatus());
                    lblStatus.setStyle("-fx-text-fill: white;");
                    Label lblContrat = new Label("Contrat: " + offre.getContrat());
                    lblContrat.setStyle("-fx-text-fill: white;");

                    content.getChildren().addAll(lblPoste, lblNiveau, lblSalaire, lblStatus, lblContrat);
                    card.getChildren().add(content);
                    card.setOnMouseClicked(event -> remplirChamps(offre));

                    currentRow.getChildren().add(card);
                    cardCount++;

                    if (cardCount >= 4) {
                        offreContainer.getChildren().add(currentRow);
                        currentRow = new HBox(10);
                        cardCount = 0;
                    }
                }

                if (cardCount > 0) offreContainer.getChildren().add(currentRow);
            });
        }



        @FXML
        public void creerOffre(ActionEvent actionEvent) {
            // Vérification que les champs ne sont pas vides
            if (tfPosteRecherche.getText().isEmpty() || tfNiveauRequis.getText().isEmpty() || tfSalairePropose.getText().isEmpty() ||
                    tfStatus.getText().isEmpty() || tfContrat.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs avant d'ajouter une offre.", Alert.AlertType.ERROR);
                return;
            }

            // Vérification que le salaire est positif
            float salaire;
            try {
                salaire = Float.parseFloat(tfSalairePropose.getText());
                if (salaire <= 0) {
                    showAlert("Erreur", "Le salaire doit être un nombre positif.", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer un salaire valide.", Alert.AlertType.ERROR);
                return;
            }

            try {
                Offre_de_recrutement offre = new Offre_de_recrutement();
                offre.setPoste_recherche(tfPosteRecherche.getText());
                offre.setNiveu_requis(tfNiveauRequis.getText());
                offre.setSalaire_propose(salaire);
                offre.setStatus(tfStatus.getText());
                offre.setContrat(tfContrat.getText());

                serviceOffre.add(offre);
                refreshOffresList();
                showAlert("Succès", "Offre créée avec succès!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la création de l'offre.", Alert.AlertType.ERROR);
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
            contentStream.showText("Liste des Offres de recrutement");
            contentStream.newLineAtOffset(0, -20);

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            for (Offre_de_recrutement o : serviceOffre.getAll()) {
                String line = "l'offre de recrutement " + o.getIdo() + " , le contrat : " + o.getContrat() + " et salaire proposé " + o.getSalaire_propose() +
                        " , le niveau requis " + o.getNiveu_requis() + " , le statut " + o.getStatus() + "avec la poste de recherche :" + o.getPoste_recherche();
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15);
            }

            contentStream.endText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            document.save(new File("liste des offres de recrutements.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @FXML
        public void modifierOffre(ActionEvent actionEvent) {
            if (selectedOffre == null) {
                showAlert("Erreur", "Veuillez sélectionner une offre à modifier.", Alert.AlertType.ERROR);
                return;
            }
            try {
                selectedOffre.setPoste_recherche(tfPosteRecherche.getText());
                selectedOffre.setNiveu_requis(tfNiveauRequis.getText());
                selectedOffre.setSalaire_propose(Float.parseFloat(tfSalairePropose.getText()));
                selectedOffre.setStatus(tfStatus.getText());
                selectedOffre.setContrat(tfContrat.getText());

                serviceOffre.update(selectedOffre);
                refreshOffresList();
                showAlert("Succès", "Offre modifiée avec succès!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la modification de l'offre.", Alert.AlertType.ERROR);
            }
        }

        @FXML
        private void supprimerOffre(ActionEvent event) {
            if (selectedOffre == null) {
                showAlert("Erreur", "Veuillez sélectionner une offre à supprimer.", Alert.AlertType.ERROR);
                return;
            }
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer cette offre ?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                serviceOffre.delete(selectedOffre);
                refreshOffresList();
                showAlert("Succès", "Offre supprimée avec succès!", Alert.AlertType.INFORMATION);
            }
        }

        private void remplirChamps(Offre_de_recrutement offre) {
            selectedOffre = offre;

            tfPosteRecherche.setText(offre.getPoste_recherche());
            tfNiveauRequis.setText(offre.getNiveu_requis());
            tfSalairePropose.setText(String.valueOf(offre.getSalaire_propose()));
            tfStatus.setText(offre.getStatus());
            tfContrat.setText(offre.getContrat());
        }

        private void showAlert(String title, String message, Alert.AlertType type) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }




