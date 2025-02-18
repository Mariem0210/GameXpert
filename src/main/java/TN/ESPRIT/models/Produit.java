package TN.ESPRIT.models;

import java.util.Date;

public class Produit {
    private int id_produit, stock;
    private String nom, description, categorie, image_produit;
    private float prix;
    private Date date_creation;

    // Constructeur par défaut
    public Produit() {
    }

    // Constructeur avec tous les attributs
    public Produit(int id_produit, String nom, String description, float prix, int stock, Date date_creation, String categorie, String image_produit) {
        this.id_produit = id_produit;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.stock = stock;
        this.date_creation = date_creation;
        this.categorie = categorie;
        this.image_produit = image_produit;
    }

    // Constructeur sans id_produit (utilisé pour les insertions)
    public Produit(String nom, String description, float prix, int stock, Date date_creation, String categorie, String image_produit) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.stock = stock;
        this.date_creation = date_creation;
        this.categorie = categorie;
        this.image_produit = image_produit;
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

    public Date getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Date date_creation) {
        this.date_creation = date_creation;
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

    // Méthode toString pour affichage
    @Override
    public String toString() {
        return "Produit{" +
                "id_produit=" + id_produit +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", stock=" + stock +
                ", date_creation=" + date_creation +
                ", categorie='" + categorie + '\'' +
                ", image_produit='" + image_produit + '\'' +
                '}';
    }
}
