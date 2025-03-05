package tn.esprit.controllers;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.models.Produit;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ChatIAController {

    @FXML
    private TextArea taChat;

    @FXML
    private TextField tfMessage;

    private final OpenAiService openAiService = new OpenAiService("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");
    private List<Produit> produits;

    // Méthode pour injecter la liste des produits
    public void setProduits(List<Produit> produits) {
        this.produits = produits;
        System.out.println("Produits disponibles : " + produits); // Log pour vérifier les produits
    }

    @FXML
    private void envoyerMessage() {
        String messageUtilisateur = tfMessage.getText().trim();
        if (!messageUtilisateur.isEmpty()) {
            // Afficher le message de l'utilisateur dans le chat
            taChat.appendText("Vous: " + messageUtilisateur + "\n");

            // Envoyer le message à l'IA et obtenir une réponse
            String reponseIA = obtenirReponseIA(messageUtilisateur);

            // Afficher la réponse de l'IA dans le chat
            taChat.appendText("IA: " + reponseIA + "\n");

            // Effacer le champ de texte après l'envoi
            tfMessage.clear();
        }
    }

    private String obtenirReponseIA(String message) {
        try {
            // Si l'utilisateur spécifie un budget ou une catégorie
            if (message.toLowerCase().contains("mon budget est") || message.toLowerCase().contains("je veux catégorie")) {
                return filtrerProduitsParBudgetEtCategorie(message);
            }

            // Si l'utilisateur tape un mot-clé partiel (comme "gaming" ou "rtx")
            List<Produit> produitsCorrespondants = produits.stream()
                    .filter(p -> p.getNom().toLowerCase().contains(message.toLowerCase()) ||
                            p.getCategorie().toLowerCase().contains(message.toLowerCase()) ||
                            p.getDescription().toLowerCase().contains(message.toLowerCase()))
                    .collect(Collectors.toList());

            if (!produitsCorrespondants.isEmpty()) {
                // Si des produits correspondent, les afficher
                StringBuilder reponse = new StringBuilder("Voici les produits correspondants :\n");
                for (Produit produit : produitsCorrespondants) {
                    reponse.append("- ").append(produit.getNom()).append(" (").append(produit.getPrix()).append(" €)\n");
                }
                return reponse.toString();
            }

            // Si l'utilisateur demande de l'aide pour choisir un produit gaming
            if (message.toLowerCase().contains("gaming")) {
                return recommanderProduitsGaming();
            }

            // Si aucun produit ne correspond, utiliser l'API OpenAI pour générer une réponse
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo") // Utiliser le modèle gpt-3.5-turbo
                    .messages(List.of(
                            new ChatMessage("system", "Vous êtes un assistant utile qui aide les utilisateurs à choisir des produits."),
                            new ChatMessage("user", message)
                    ))
                    .maxTokens(100) // Limite la réponse à 100 tokens
                    .build();

            return openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return "Désolé, une erreur s'est produite. Veuillez réessayer plus tard.";
        }
    }

    private String filtrerProduitsParBudgetEtCategorie(String message) {
        // Déclarer les variables comme finales
        final double budget;
        final String categorie;

        // Extraire le budget de la requête
        if (message.toLowerCase().contains("mon budget est")) {
            try {
                budget = Double.parseDouble(message.toLowerCase().replace("mon budget est", "").trim().replace("€", "").trim());
            } catch (NumberFormatException e) {
                return "Veuillez entrer un budget valide (exemple : 'mon budget est 1000 €').";
            }
        } else {
            budget = -1; // Valeur par défaut si le budget n'est pas spécifié
        }

        // Extraire la catégorie de la requête
        if (message.toLowerCase().contains("je veux catégorie")) {
            categorie = message.toLowerCase().replace("je veux catégorie", "").trim();
        } else {
            categorie = ""; // Valeur par défaut si la catégorie n'est pas spécifiée
        }

        // Filtrer les produits en fonction du budget et de la catégorie
        List<Produit> produitsFiltres = produits.stream()
                .filter(p -> (budget == -1 || p.getPrix() <= budget) &&
                        (categorie.isEmpty() || p.getCategorie().toLowerCase().contains(categorie)))
                .collect(Collectors.toList());

        if (produitsFiltres.isEmpty()) {
            return "Aucun produit trouvé correspondant à votre demande.";
        }

        // Afficher les produits correspondants
        StringBuilder reponse = new StringBuilder("Voici les produits correspondants :\n");
        for (Produit produit : produitsFiltres) {
            reponse.append("- ").append(produit.getNom()).append(" (").append(produit.getPrix()).append(" €)\n");
        }
        return reponse.toString();
    }
    private String recommanderProduitsGaming() {
        if (produits == null || produits.isEmpty()) {
            return "Aucun produit disponible pour le moment.";
        }

        // Filtrer les produits pour ne garder que ceux qui correspondent à des mots-clés liés au gaming
        List<Produit> produitsGaming = produits.stream()
                .filter(p -> p.getNom().toLowerCase().contains("gaming") ||
                        p.getCategorie().toLowerCase().contains("gaming") ||
                        p.getDescription().toLowerCase().contains("gaming"))
                .collect(Collectors.toList());

        if (produitsGaming.isEmpty()) {
            return "Aucun produit gaming disponible dans le magasin.";
        }

        // Afficher tous les produits correspondants
        StringBuilder reponse = new StringBuilder("Voici les produits gaming disponibles :\n");
        for (Produit produit : produitsGaming) {
            reponse.append("- ").append(produit.getNom()).append(" (").append(produit.getPrix()).append(" €)\n");
        }
        return reponse.toString();
    }
}