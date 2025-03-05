package tn.esprit.config;

public class Config {
    private static String stripeSecretKey;

    static {
        // Charger la clé API depuis un fichier de configuration externe
        stripeSecretKey = System.getenv("STRIPE_SECRET_KEY");
    }

    public static String getStripeSecretKey() {
        return stripeSecretKey;
    }
}