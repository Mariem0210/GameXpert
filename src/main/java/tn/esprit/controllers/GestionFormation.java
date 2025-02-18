package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import tn.esprit.models.Formation;
import tn.esprit.services.ServiceFormation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GestionFormation {

    @FXML
    private TextField tfNomf;
    @FXML
    private TextField tfDescriptionf;
    @FXML
    private TextField tfNiveauf;
    @FXML
    private DatePicker tfDateDebutf;
    @FXML
    private DatePicker tfDateFinf;
    @FXML
    private TextField tfCapacitef;
    @FXML
    private TextField tfPrixf;
    @FXML
    private TextField tfIdu;

    @FXML
    private FlowPane cardContainer;

    private ServiceFormation serviceFormation = new ServiceFormation();

    @FXML
    public void ajouterFormation(ActionEvent actionEvent) {
        // Récupérer les données du formulaire
        Formation formation = new Formation();
        formation.setNomf(tfNomf.getText());
        formation.setDescriptionf(tfDescriptionf.getText());
        formation.setNiveauf(tfNiveauf.getText());

        // Récupérer les dates du DatePicker
        LocalDate dateDebut = tfDateDebutf.getValue();
        LocalDate dateFin = tfDateFinf.getValue();

        if (dateDebut != null && dateFin != null) {
            formation.setDateDebutf(dateDebut);
            formation.setDateFinf(dateFin);
        }

        formation.setCapacitef(Integer.parseInt(tfCapacitef.getText()));
        formation.setPrixf(Double.parseDouble(tfPrixf.getText()));
        formation.setIdu(Integer.parseInt(tfIdu.getText()));

        // Ajouter la formation à la base de données (ServiceFormation)
        serviceFormation.add(formation);
    }

    @FXML
    public void afficherFormations(ActionEvent actionEvent) {
        // Obtenir toutes les formations
        List<Formation> formations = serviceFormation.getAll();

        // Vider le conteneur avant d'ajouter de nouvelles cartes
        cardContainer.getChildren().clear();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Formation formation : formations) {
            // Créer une carte pour chaque formation
            VBox card = new VBox(10);
            card.setStyle("-fx-border-color: black; -fx-background-color: #050505; -fx-padding: 10;");

            Label nomLabel = new Label("Nom : " + formation.getNomf());
            Label descriptionLabel = new Label("Description : " + formation.getDescriptionf());
            Label niveauLabel = new Label("Niveau : " + formation.getNiveauf());
            Label capaciteLabel = new Label("Capacité : " + formation.getCapacitef());
            Label prixLabel = new Label("Prix : " + formation.getPrixf() + "€");
            Label dateDebutLabel = new Label("Début : " + formation.getDateDebutf().format(formatter));
            Label dateFinLabel = new Label("Fin : " + formation.getDateFinf().format(formatter));

            card.getChildren().addAll(nomLabel, descriptionLabel, niveauLabel, capaciteLabel, prixLabel, dateDebutLabel, dateFinLabel);

            // Ajouter la carte dans le FlowPane
            cardContainer.getChildren().add(card);
        }
    }

}
