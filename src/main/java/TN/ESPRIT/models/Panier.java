package tn.esprit.models;

import java.util.Date;

public class Panier {
    private int id_panier;
    private int id_utilisateur;
    private Produit produit;  // Remplacer id_produit par un objet Produit
    private int quantite;
    private Date date_ajout;

    // Constructeur modifié
    public Panier(int id_panier, int id_utilisateur, Produit produit, int quantite, Date date_ajout) {
        this.id_panier = id_panier;
        this.id_utilisateur = id_utilisateur;
        this.produit = produit;
        this.quantite = quantite;
        this.date_ajout = date_ajout;
    }

    // Constructeur sans Produit
    public Panier(int id_utilisateur, int id_produit, int quantite, Date date_ajout) {
        this.id_utilisateur = id_utilisateur;
        this.produit = new Produit(id_produit); // Créez un produit à partir de l'ID
        this.quantite = quantite;
        this.date_ajout = date_ajout;
    }

    // Getters et Setters
    public int getId_panier() { return id_panier; }
    public void setId_panier(int id_panier) { this.id_panier = id_panier; }

    public int getId_utilisateur() { return id_utilisateur; }
    public void setId_utilisateur(int id_utilisateur) { this.id_utilisateur = id_utilisateur; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public Date getDate_ajout() { return date_ajout; }
    public void setDate_ajout(Date date_ajout) { this.date_ajout = date_ajout; }

    @Override
    public String toString() {
        return "Panier{" +
                "id_panier=" + id_panier +
                ", id_utilisateur=" + id_utilisateur +
                ", produit=" + produit.getNom() +  // Affichage du nom du produit
                ", quantite=" + quantite +
                ", date_ajout=" + date_ajout +
                '}';
    }
}
