package tn.esprit.controllers;

import tn.esprit.models.Commande;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CardCommandeController {

    @FXML
    private VBox cardContainer;

    @FXML
    private Label lblIdCommande, lblDateCommande, lblMontantTotal;

    @FXML
    private Button btnVoirDetails;

    private Commande commande;

    public void setCommande(Commande commande) {
        this.commande = commande;
        lblIdCommande.setText("Commande ID: " + commande.getId_commande());
        lblDateCommande.setText("Date: " + commande.getDate_commande());
        lblMontantTotal.setText("Total: " + commande.getMontant_total() + " €");
    }

    @FXML
    private void voirDetailsCommande() {
        System.out.println("Voir détails de la commande ID: " + commande.getId_commande());
        // Ajoute ici la logique pour afficher les détails de la commande
    }
}
