package TN.ESPRIT.services;

import TN.ESPRIT.interfaces.IService;
import TN.ESPRIT.models.Produit;
import TN.ESPRIT.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProduit implements IService<Produit> {
    private Connection cnx;

    public ServiceProduit() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Produit produit) {
        String qry = "INSERT INTO produits(`Nom`, `Description`, `Prix`, `Stock`, `Date_Creation`, `Categorie`, `Image_produit`) VALUES (?, ?, ?, ?, ?, ?, ?)";
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

    @Override
    public List<Produit> getAll() {
        List<Produit> produits = new ArrayList<>();
        String qry = "SELECT * FROM produits";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Produit p = new Produit();
                p.setId_produit(rs.getInt("Id_produit"));
                p.setNom(rs.getString("Nom"));
                p.setDescription(rs.getString("Description"));
                p.setPrix(rs.getFloat("Prix"));
                p.setStock(rs.getInt("Stock"));
                p.setDate_creation(rs.getDate("Date_Creation"));
                p.setCategorie(rs.getString("Categorie"));
                p.setImage_produit(rs.getString("Image_produit"));

                produits.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits : " + e.getMessage());
        }
        return produits;
    }

    @Override
    public void update(Produit produit) {
        String qry = "UPDATE produits SET `Nom`=?, `Description`=?, `Prix`=?, `Stock`=?, `Date_Creation`=?, `Categorie`=?, `Image_produit`=? WHERE `Id_produit`=?";
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

    @Override
    public void delete(Produit produit) {
        String qry = "DELETE FROM produits WHERE `Id_produit`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, produit.getId_produit());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du produit : " + e.getMessage());
        }
    }
}
