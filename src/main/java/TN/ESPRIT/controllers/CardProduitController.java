package tn.esprit.controllers;

import tn.esprit.models.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.InputStream;

public class CardProduitController {

    @FXML
    private VBox cardContainer;

    @FXML
    private Label lblNom, lblDescription, lblPrix, lblStock, lblCategorie;

    @FXML
    private ImageView imageProduit;

    @FXML
    private Button btnAjouterPanier;

    private PanierController panierController;
    private Produit produit;

    public void setProduit(Produit produit, GestionProduitController gestionProduitController, PanierController panierController) {
        this.produit = produit;
        this.panierController = panierController;

        lblNom.setText(produit.getNom());
        lblDescription.setText(produit.getDescription());
        lblPrix.setText(produit.getPrix() + " €");
        lblStock.setText("Stock: " + produit.getStock());
        lblCategorie.setText("Catégorie: " + produit.getCategorie());

        // Chargement de l'image du produit
        if (produit.getImage_produit() != null && !produit.getImage_produit().isEmpty()) {
            try {
                InputStream is = getClass().getResourceAsStream(produit.getImage_produit());
                if (is != null) {
                    Image image = new Image(is);
                    imageProduit.setImage(image);
                } else {
                    System.out.println("⚠️ Image introuvable : " + produit.getImage_produit());
                }
            } catch (Exception e) {
                System.out.println("❌ Erreur lors du chargement de l'image : " + produit.getImage_produit());
                e.printStackTrace();
            }
        }

        // Vérifier si le bouton "Ajouter au Panier" est bien ajouté
        btnAjouterPanier.setOnAction(event -> ajouterAuPanier());
    }

    @FXML
    private void ajouterAuPanier() {
        if (panierController != null && produit != null) {
            System.out.println("✅ Produit ajouté au panier : " + produit.getNom());
            panierController.ajouterAuPanier(produit);
        }
    }
}
