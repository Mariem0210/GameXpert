package tn.esprit.services;

import tn.esprit.models.Produit;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProduit {
    private Connection cnx;

    public ServiceProduit() {
        cnx = MyDatabase.getInstance().getCnx();
    }
    public List<Produit> searchByName(String name) {
        List<Produit> produits = new ArrayList<>();
        String qry = "SELECT * FROM produit WHERE Nom LIKE ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, "%" + name + "%");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Produit p = new Produit(
                        rs.getInt("Id_produit"),
                        rs.getString("Nom"),
                        rs.getString("Description"),
                        rs.getFloat("Prix"),
                        rs.getInt("Stock"),
                        rs.getString("Categorie"),
                        rs.getString("Image_produit"),
                        rs.getDate("Date_Creation")
                );
                produits.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du produit : " + e.getMessage());
        }
        return produits;
    }
    public void add(Produit produit) {
        String qry = "INSERT INTO produit(Nom, Description, Prix, Stock, Date_Creation, Categorie, Image_produit) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, produit.getNom());
            pstm.setString(2, produit.getDescription());
            pstm.setFloat(3, produit.getPrix());
            pstm.setInt(4, produit.getStock());
            pstm.setDate(5, new java.sql.Date(produit.getDate_creation().getTime()));
            pstm.setString(6, produit.getCategorie());
            pstm.setString(7, produit.getImage_produit());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du produit : " + e.getMessage());
        }
    }

    public List<Produit> getAll() {
        List<Produit> produits = new ArrayList<>();
        String qry = "SELECT * FROM produit";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Produit p = new Produit(
                        rs.getInt("Id_produit"),
                        rs.getString("Nom"),
                        rs.getString("Description"),
                        rs.getFloat("Prix"),
                        rs.getInt("Stock"),
                        rs.getString("Categorie"),
                        rs.getString("Image_produit"),
                        rs.getDate("Date_Creation")
                );
                produits.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits : " + e.getMessage());
        }
        return produits;
    }

    public void update(Produit produit) {
        String qry = "UPDATE produit SET Nom=?, Description=?, Prix=?, Stock=?, Date_Creation=?, Categorie=?, Image_produit=? WHERE Id_produit=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, produit.getNom());
            pstm.setString(2, produit.getDescription());
            pstm.setFloat(3, produit.getPrix());
            pstm.setInt(4, produit.getStock());
            pstm.setDate(5, new java.sql.Date(produit.getDate_creation().getTime()));
            pstm.setString(6, produit.getCategorie());
            pstm.setString(7, produit.getImage_produit());
            pstm.setInt(8, produit.getId_produit());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du produit : " + e.getMessage());
        }
    }

    public void delete(Produit produit) {
        String qry = "DELETE FROM produit WHERE Id_produit=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, produit.getId_produit());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du produit : " + e.getMessage());
        }
    }
}
