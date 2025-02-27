package tn.esprit.services;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;

import java.util.HashMap;
import java.util.Map;

public class PaymentService {

    public PaymentService() {
        Stripe.apiKey = "sk_test_51QwQeCJrisMbus4mh1Hg52UGJ8C7Zo9dvswVN4ac7mslDJ2YDYgZuR63v36c3Zwo03COxEIo7G95EjReDfuJ6t8500VofDvzyg";  // Votre clé secrète Stripe
    }

    public void processPayment(String tokenId, double amount) {
        try {
            // Créer un paiement avec le token reçu
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (amount * 100)); // Montant en centimes
            chargeParams.put("currency", "eur");
            chargeParams.put("source", tokenId);
            chargeParams.put("description", "Paiement Commande");

            Charge charge = Charge.create(chargeParams);

            // Vérification du statut du paiement
            if ("succeeded".equals(charge.getStatus())) {
                System.out.println("Paiement réussi");
            } else {
                System.out.println("Échec du paiement");
            }
        } catch (Exception e) {
            System.out.println("Erreur avec le paiement: " + e.getMessage());
        }
    }
}
