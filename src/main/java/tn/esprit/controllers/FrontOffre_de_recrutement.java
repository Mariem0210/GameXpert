package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.net.URI;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.ResourceBundle;

import tn.esprit.models.Offre_de_recrutement;
import tn.esprit.services.ServiceOffre_de_recrutement;

public class FrontOffre_de_recrutement implements Initializable {

    @FXML private VBox offreContainer;
    private final ServiceOffre_de_recrutement serviceOffre = new ServiceOffre_de_recrutement();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshOffresList();
    }

    private void refreshOffresList() {
        Platform.runLater(() -> {
            offreContainer.getChildren().clear();
            List<Offre_de_recrutement> offres = serviceOffre.getAll();
            System.out.println("Nombre d'offres récupérées: " + offres.size());

            for (Offre_de_recrutement offre : offres) {
                StackPane card = new StackPane();
                card.setPrefSize(900, 150); // Ajuster la largeur et la hauteur de la carte
                card.setStyle("-fx-background-color: #2a2a3d; -fx-border-color: #ffcc00; -fx-border-radius: 20px; -fx-padding: 20px;");

                VBox content = new VBox(10);
                content.setAlignment(Pos.CENTER);

                Label lblPoste = new Label("Poste: " + offre.getPoste_recherche());
                lblPoste.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
                Label lblNiveau = new Label("Niveau Requis: " + offre.getNiveu_requis());
                lblNiveau.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
                Label lblSalaire = new Label("Salaire: " + offre.getSalaire_propose() + " €");
                lblSalaire.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
                Label lblStatus = new Label("Status: " + offre.getStatus());
                lblStatus.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
                Label lblContrat = new Label("Contrat: " + offre.getContrat());
                lblContrat.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

                content.getChildren().addAll(lblPoste, lblNiveau, lblSalaire, lblStatus, lblContrat);
                card.getChildren().add(content);

                // Ajouter la carte à la VBox principale
                offreContainer.getChildren().add(card);
            }
        });
    }

    @FXML
    private void afficherActualitesEsport() {
        try {
            // Liste des flux RSS pour différents jeux esports
            String[] rssFeeds = {
                    "https://www.hltv.org/rss/news", // CS:GO
                    "https://dotesports.com/valorant/feed", // Valorant
                    "https://dotesports.com/fortnite/feed", // Fortnite
                    "https://dotesports.com/fifa/feed", // FIFA
                    "https://dotesports.com/league-of-legends/feed", // League of Legends
                    "https://dotesports.com/rocket-league/feed" // Rocket League
            };

            // Créer une nouvelle fenêtre pour afficher les actualités
            Stage stage = new Stage();
            stage.setTitle("Actualités Esport (Multi-jeux)");

            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(10));
            vbox.setStyle("-fx-background-color: #f4f4f4;");

            // Parcourir chaque flux RSS
            for (String rssUrl : rssFeeds) {
                try {
                    // Parser le flux RSS
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(new URL(rssUrl).openStream());

                    // Récupérer les éléments "item" du flux RSS
                    NodeList itemList = document.getElementsByTagName("item");

                    for (int i = 0; i < itemList.getLength(); i++) {
                        Element item = (Element) itemList.item(i);
                        String title = item.getElementsByTagName("title").item(0).getTextContent();
                        String link = item.getElementsByTagName("link").item(0).getTextContent();
                        String description = item.getElementsByTagName("description").item(0).getTextContent();

                        // Extraire l'URL de l'image automatiquement
                        String imageUrl = extractImageUrlFromItem(item);

                        // Créer un conteneur pour l'article
                        HBox articleBox = new HBox(10);
                        articleBox.setAlignment(Pos.TOP_LEFT);
                        articleBox.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-padding: 15px;");
                        articleBox.getStyleClass().add("article-box");

                        // Ajouter l'image (si disponible)
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            try {
                                ImageView imageView = new ImageView(new Image(imageUrl));
                                imageView.setFitWidth(120); // Largeur fixe pour l'image
                                imageView.setFitHeight(120); // Hauteur fixe pour l'image
                                imageView.setPreserveRatio(true);
                                imageView.getStyleClass().add("article-image");
                                articleBox.getChildren().add(imageView);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Si aucune image n'est disponible, ajouter un espace vide pour conserver la mise en page
                            Region placeholder = new Region();
                            placeholder.setPrefWidth(120);
                            placeholder.setPrefHeight(120);
                            articleBox.getChildren().add(placeholder);
                        }

                        // Créer un conteneur pour le texte
                        VBox textBox = new VBox(5);
                        textBox.setAlignment(Pos.TOP_LEFT);

                        // Créer un hyperlien pour le titre
                        Hyperlink hyperlink = new Hyperlink(title);
                        hyperlink.getStyleClass().add("article-hyperlink");
                        hyperlink.setOnAction(e -> {
                            try {
                                java.awt.Desktop.getDesktop().browse(new URI(link));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });

                        // Ajouter la description
                        Label descLabel = new Label(cleanDescription(description));
                        descLabel.getStyleClass().add("article-description");
                        descLabel.setWrapText(true);

                        // Ajouter les éléments au conteneur de texte
                        textBox.getChildren().addAll(hyperlink, descLabel);

                        // Ajouter le conteneur de texte à l'article
                        articleBox.getChildren().add(textBox);

                        // Ajouter l'article à la VBox
                        vbox.getChildren().addAll(articleBox, new Separator());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Erreur", "Impossible de récupérer les actualités pour : " + rssUrl, Alert.AlertType.ERROR);
                }
            }

            // Ajouter une ScrollPane pour permettre le défilement
            ScrollPane scrollPane = new ScrollPane(vbox);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: #f4f4f4;");

            Scene scene = new Scene(scrollPane, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de récupérer les actualités de l'esport.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Extrait l'URL de l'image à partir d'un élément <item> du flux RSS.
     */
    private String extractImageUrlFromItem(Element item) {
        // Essayer de récupérer l'image depuis <media:content>
        NodeList mediaContent = item.getElementsByTagName("media:content");
        if (mediaContent.getLength() > 0) {
            return ((Element) mediaContent.item(0)).getAttribute("url");
        }

        // Essayer de récupérer l'image depuis <enclosure>
        NodeList enclosure = item.getElementsByTagName("enclosure");
        if (enclosure.getLength() > 0) {
            return ((Element) enclosure.item(0)).getAttribute("url");
        }

        // Essayer de récupérer l'image depuis la description HTML
        String description = item.getElementsByTagName("description").item(0).getTextContent();
        if (description.contains("<img")) {
            int imgStart = description.indexOf("src=\"") + 5;
            int imgEnd = description.indexOf("\"", imgStart);
            return description.substring(imgStart, imgEnd);
        }

        return null; // Aucune image trouvée
    }

    /**
     * Nettoie la description pour enlever les balises HTML.
     */
    private String cleanDescription(String description) {
        return description.replaceAll("<[^>]*>", ""); // Supprime toutes les balises HTML
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}