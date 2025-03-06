package tn.esprit.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.esprit.config.Config;

import java.util.HashMap;
import java.util.Map;

public class StripeController {

    @FXML private TextField tfCardNumber;
    @FXML private TextField tfExpMonth;
    @FXML private TextField tfExpYear;
    @FXML private TextField tfCVC;
    @FXML private TextField tfAmount;

    private static final String STRIPE_SECRET_KEY = "sk_test_51QwQeCJrisMbus4mh1Hg52UGJ8C7Zo9dvswVN4ac7mslDJ2YDYgZuR63v36c3Zwo03COxEIo7G95EjReDfuJ6t8500VofDvzyg";

    public StripeController() {
        Stripe.apiKey = STRIPE_SECRET_KEY;
    }

    // Méthode pour définir le montant du paiement
    public void setMontant(float montant) {
        tfAmount.setText(String.valueOf(montant));
    }

    @FXML
    public void processPayment() {
        try {
            // 1. Créer un token pour la carte bancaire avec les informations saisies par l'utilisateur
            Map<String, Object> cardParams = new HashMap<>();
            cardParams.put("number", tfCardNumber.getText().replaceAll("\\s+", "")); // Supprimer les espaces
            cardParams.put("exp_month", Integer.parseInt(tfExpMonth.getText())); // Mois d'expiration
            cardParams.put("exp_year", Integer.parseInt(tfExpYear.getText())); // Année d'expiration
            cardParams.put("cvc", tfCVC.getText()); // CVC

            Map<String, Object> tokenParams = new HashMap<>();
            tokenParams.put("card", cardParams);

            Token token = Token.create(tokenParams);

            // 2. Créer un paiement (Charge) avec le token
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (Double.parseDouble(tfAmount.getText()) * 100)); // Montant en centimes
            chargeParams.put("currency", "eur");
            chargeParams.put("source", token.getId()); // Utiliser le token comme source
            chargeParams.put("description", "Paiement Produit");

            Charge charge = Charge.create(chargeParams);

            // 3. Vérifier si le paiement est réussi
            if ("succeeded".equals(charge.getStatus())) {
                showAlert("Paiement réussi", "Le paiement a été effectué avec succès.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Échec du paiement", "Le paiement a échoué, veuillez réessayer.", Alert.AlertType.ERROR);
            }

        } catch (StripeException e) {
            showAlert("Erreur", "Problème avec le paiement : " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des informations valides.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}