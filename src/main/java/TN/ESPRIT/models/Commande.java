package tn.esprit.models;

import java.util.Date;

public class Commande {
    private int id_commande;
    private Date date_commande;
    private float montant_total;
    private int id_utilisateur;

    // Constructeurs
    public Commande() {}

    public Commande(int id_commande, Date date_commande, float montant_total, int id_utilisateur) {
        this.id_commande = id_commande;
        this.date_commande = date_commande;
        this.montant_total = montant_total;
        this.id_utilisateur = id_utilisateur;
    }

    public Commande(Date date_commande, float montant_total, int id_utilisateur) {
        this.date_commande = date_commande;
        this.montant_total = montant_total;
        this.id_utilisateur = id_utilisateur;
    }

    // Getters et Setters
    public int getId_commande() { return id_commande; }
    public void setId_commande(int id_commande) { this.id_commande = id_commande; }
    public Date getDate_commande() { return date_commande; }
    public void setDate_commande(Date date_commande) { this.date_commande = date_commande; }
    public float getMontant_total() { return montant_total; }
    public void setMontant_total(float montant_total) { this.montant_total = montant_total; }
    public int getId_utilisateur() { return id_utilisateur; }
    public void setId_utilisateur(int id_utilisateur) { this.id_utilisateur = id_utilisateur; }

    @Override
    public String toString() {
        return "Commande{" +
                "id_commande=" + id_commande +
                ", date_commande=" + date_commande +
                ", montant_total=" + montant_total +
                ", id_utilisateur=" + id_utilisateur +
                '}';
    }
}