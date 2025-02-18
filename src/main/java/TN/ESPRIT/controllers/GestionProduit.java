package TN.ESPRIT.controllers;

import TN.ESPRIT.models.Produit;
import TN.ESPRIT.services.ServiceProduit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import TN.ESPRIT.models.Produit;
import TN.ESPRIT.services.ServiceProduit;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;


import java.sql.SQLException;
import java.util.Date;

public class GestionProduit {

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfDescription;

    @FXML
    private TextField tfPrix;

    @FXML
    private TextField tfStock;

    @FXML
    private TextField tfCategorie;

    @FXML
    private TextField tfImageProduit;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnModifier;

    @FXML
    private ListView<Produit> listViewProduits;

    private ServiceProduit serviceProduits;

    public GestionProduit() {
        serviceProduits = new ServiceProduit();
    }

    @FXML
    public void initialize() {
        // Personnaliser l'affichage des éléments de la liste
        listViewProduits.setCellFactory(new Callback<ListView<Produit>, ListCell<Produit>>() {
            @Override
            public ListCell<Produit> call(ListView<Produit> listView) {
                return new ListCell<Produit>() {
                    private ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(Produit produit, boolean empty) {
                        super.updateItem(produit, empty);
                        if (empty || produit == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(
                                    "🛒 " + produit.getNom() + "\n" +
                                            "📜 " + produit.getDescription() + "\n" +
                                            "💰 " + produit.getPrix() + " € | 📦 Stock: " + produit.getStock() + "\n" +
                                            "🏷️ Catégorie: " + produit.getCategorie()
                            );

                            if (produit.getImage_produit() != null && !produit.getImage_produit().isEmpty()) {
                                try {
                                    Image img = new Image(produit.getImage_produit(), 50, 50, true, true);
                                    imageView.setImage(img);
                                } catch (Exception e) {
                                    imageView.setImage(null);
                                }
                            } else {
                                imageView.setImage(null);
                            }

                            setGraphic(imageView);
                        }
                    }
                };
            }
        });

        refreshProduitsList();
    }

    @FXML
    private void ajouterProduit(ActionEvent event) throws SQLException {
        String nom = tfNom.getText();
        String description = tfDescription.getText();
        float prix;
        int stock;

        try {
            prix = Float.parseFloat(tfPrix.getText());
            stock = Integer.parseInt(tfStock.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Prix et stock doivent être des nombres valides.", Alert.AlertType.ERROR);
            return;
        }

        String categorie = tfCategorie.getText();
        String imageProduit = tfImageProduit.getText();

        if (nom.isEmpty() || description.isEmpty() || tfPrix.getText().isEmpty() || tfStock.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }

        Produit produit = new Produit(nom, description, prix, stock, new Date(), categorie, imageProduit);
        serviceProduits.add(produit);
        refreshProduitsList();
        showAlert("Succès", "Produit ajouté avec succès!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void supprimerProduit(ActionEvent event) throws SQLException {
        Produit selectedProduit = listViewProduits.getSelectionModel().getSelectedItem();

        if (selectedProduit == null) {
            showAlert("Erreur", "Veuillez sélectionner un produit à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        serviceProduits.delete(selectedProduit);
        refreshProduitsList();
        showAlert("Succès", "Produit supprimé avec succès!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void modifierProduit(ActionEvent event) throws SQLException {
        Produit selectedProduit = listViewProduits.getSelectionModel().getSelectedItem();

        if (selectedProduit == null) {
            showAlert("Erreur", "Veuillez sélectionner un produit à modifier.", Alert.AlertType.ERROR);
            return;
        }

        // Création de la boîte de dialogue
        Dialog<Produit> dialog = new Dialog<>();
        dialog.setTitle("Modifier Produit");
        dialog.setHeaderText("Modifiez les informations du produit");

        // Boutons OK / Annuler
        ButtonType modifierButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifierButtonType, ButtonType.CANCEL);

        // Champs de texte pour la modification
        TextField nomField = new TextField(selectedProduit.getNom());
        TextField descriptionField = new TextField(selectedProduit.getDescription());
        TextField prixField = new TextField(String.valueOf(selectedProduit.getPrix()));
        TextField stockField = new TextField(String.valueOf(selectedProduit.getStock()));
        TextField categorieField = new TextField(selectedProduit.getCategorie());
        TextField imageField = new TextField(selectedProduit.getImage_produit());

        // Mise en page des champs
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Prix:"), 0, 2);
        grid.add(prixField, 1, 2);
        grid.add(new Label("Stock:"), 0, 3);
        grid.add(stockField, 1, 3);
        grid.add(new Label("Catégorie:"), 0, 4);
        grid.add(categorieField, 1, 4);
        grid.add(new Label("Image URL:"), 0, 5);
        grid.add(imageField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Conversion du résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifierButtonType) {
                try {
                    selectedProduit.setNom(nomField.getText());
                    selectedProduit.setDescription(descriptionField.getText());
                    selectedProduit.setPrix(Float.parseFloat(prixField.getText()));
                    selectedProduit.setStock(Integer.parseInt(stockField.getText()));
                    selectedProduit.setCategorie(categorieField.getText());
                    selectedProduit.setImage_produit(imageField.getText());
                    return selectedProduit;
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Prix et stock doivent être des nombres valides.", Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        // Affichage de la boîte de dialogue et gestion du résultat
        dialog.showAndWait().ifPresent(produit -> {
            // Traiter la mise à jour du produit dans la base de données
            try {
                // Mise à jour dans la base de données
                serviceProduits.update(produit);
                refreshProduitsList(); // Rafraîchir la liste des produits
                showAlert("Succès", "Produit modifié avec succès!", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                // Gérer les erreurs SQL et afficher un message d'erreur
                showAlert("Erreur", "Erreur lors de la modification du produit.", Alert.AlertType.ERROR);
            }
        });
    }

    public void refreshProduitsList() {
        ObservableList<Produit> produitsList = FXCollections.observableArrayList(serviceProduits.getAll());
        listViewProduits.setItems(produitsList);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
