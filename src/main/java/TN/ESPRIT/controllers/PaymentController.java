package tn.esprit.controllers;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import org.springframework.web.bind.annotation.*;
import tn.esprit.models.PaymentRequest;
@RestController
@RequestMapping("/payment")
public class PaymentController {

    static {
        // Clé secrète Stripe
        Stripe.apiKey = "sk_test_51QwQeCJrisMbus4mh1Hg52UGJ8C7Zo9dvswVN4ac7mslDJ2YDYgZuR63v36c3Zwo03COxEIo7G95EjReDfuJ6t8500VofDvzyg";
    }

    @PostMapping
    public String handlePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            // Créez un paiement Stripe
            ChargeCreateParams params = ChargeCreateParams.builder()
                    .setAmount((long) (paymentRequest.getAmount() * 100)) // Montant en centimes
                    .setCurrency("eur")
                    .setSource(paymentRequest.getToken())
                    .setDescription("Paiement Commande")
                    .build();

            Charge charge = Charge.create(params);

            // Vérification du statut du paiement
            if ("succeeded".equals(charge.getStatus())) {
                return "Paiement réussi";
            } else {
                return "Échec du paiement";
            }

        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}
