package tn.esprit.models;

public class DetailsCommande {
    private int id_detail;
    private int id_commande;
    private int id_produit;
    private int quantite;
    private float prix_unitaire;

    // Constructeurs
    public DetailsCommande() {}

    public DetailsCommande(int id_detail, int id_commande, int id_produit, int quantite, float prix_unitaire) {
        this.id_detail = id_detail;
        this.id_commande = id_commande;
        this.id_produit = id_produit;
        this.quantite = quantite;
        this.prix_unitaire = prix_unitaire;
    }

    public DetailsCommande(int id_commande, int id_produit, int quantite, float prix_unitaire) {
        this.id_commande = id_commande;
        this.id_produit = id_produit;
        this.quantite = quantite;
        this.prix_unitaire = prix_unitaire;
    }

    // Getters et Setters
    public int getId_detail() { return id_detail; }
    public void setId_detail(int id_detail) { this.id_detail = id_detail; }
    public int getId_commande() { return id_commande; }
    public void setId_commande(int id_commande) { this.id_commande = id_commande; }
    public int getId_produit() { return id_produit; }
    public void setId_produit(int id_produit) { this.id_produit = id_produit; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public float getPrix_unitaire() { return prix_unitaire; }
    public void setPrix_unitaire(float prix_unitaire) { this.prix_unitaire = prix_unitaire; }

    @Override
    public String toString() {
        return "DetailsCommande{" +
                "id_detail=" + id_detail +
                ", id_commande=" + id_commande +
                ", id_produit=" + id_produit +
                ", quantite=" + quantite +
                ", prix_unitaire=" + prix_unitaire +
                '}';
    }
}