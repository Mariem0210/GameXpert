package tn.esprit.models;

import java.util.Date;

public class Produit {
    private int id_produit;
    private String nom;
    private String description;
    private float prix;
    private int stock;
    private String categorie;
    private String image_produit;
    private Date date_creation;

    // Constructeur avec l'ID du produit
    public Produit(int id_produit) {
        this.id_produit = id_produit;
        // Vous pouvez récupérer ici les autres informations à partir de la base de données si nécessaire
        // Exemple : Requête pour récupérer nom, description, etc., à partir de l'ID
        // Ici, on suppose que le produit est récupéré à partir de l'ID dans une base de données.
        this.nom = "Nom du produit " + id_produit;  // Exemple, vous devez le remplacer par une vraie récupération
        this.description = "Description du produit " + id_produit;  // Même remarque ici
        this.prix = 100;  // Exemple
        this.stock = 50;  // Exemple
    }
    public Produit(String nom) {
        this.id_produit = id_produit;
        // Vous pouvez récupérer ici les autres informations à partir de la base de données si nécessaire
        // Exemple : Requête pour récupérer nom, description, etc., à partir de l'ID
        // Ici, on suppose que le produit est récupéré à partir de l'ID dans une base de données.
        this.nom = "nom";  // Exemple, vous devez le remplacer par une vraie récupération
        this.description = "Description du produit " + id_produit;  // Même remarque ici
        this.prix = 100;  // Exemple
        this.stock = 50;  // Exemple
    }
    public Produit(String nom, String description, float prix, int stock, Date date_creation, String categorie, String image_produit) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.stock = stock;
        this.date_creation = date_creation;
        this.categorie = categorie;
        this.image_produit = image_produit;
        // L'ID sera généré par la base de données lorsqu'un produit est inséré
    }


    // Constructeur avec tous les champs
    public Produit(int id_produit, String nom, String description, float prix, int stock, String categorie, String image_produit, Date date_creation) {
        this.id_produit = id_produit;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.stock = stock;
        this.categorie = categorie;
        this.image_produit = image_produit;
        this.date_creation = date_creation;
    }

    // Getters et Setters
    public int getId_produit() {
        return id_produit;
    }

    public void setId_produit(int id_produit) {
        this.id_produit = id_produit;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getImage_produit() {
        return image_produit;
    }

    public void setImage_produit(String image_produit) {
        this.image_produit = image_produit;
    }

    public Date getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Date date_creation) {
        this.date_creation = date_creation;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id_produit=" + id_produit +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", stock=" + stock +
                ", categorie='" + categorie + '\'' +
                ", image_produit='" + image_produit + '\'' +
                ", date_creation=" + date_creation +
                '}';
    }
}
