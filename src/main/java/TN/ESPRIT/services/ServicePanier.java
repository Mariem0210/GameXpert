package tn.esprit.services;

import tn.esprit.models.Panier;
import tn.esprit.models.Produit;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePanier {
    private final Connection cnx;

    public ServicePanier() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public void add(Panier panier) {
        String qry = "INSERT INTO panier(idu, id_produit, quantite, date_ajout) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, panier.getId_utilisateur());
            pstm.setInt(2, panier.getProduit().getId_produit());
            pstm.setInt(3, panier.getQuantite());
            pstm.setTimestamp(4, new Timestamp(panier.getDate_ajout().getTime()));
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout au panier : " + e.getMessage());
        }
    }

    public List<Panier> getAll() {
        List<Panier> panierList = new ArrayList<>();
        String qry = "SELECT * FROM panier";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Produit produit = new Produit(rs.getInt("id_produit"));
                Panier panier = new Panier(rs.getInt("id_panier"), rs.getInt("idu"), produit, rs.getInt("quantite"), rs.getTimestamp("date_ajout"));
                panierList.add(panier);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du panier : " + e.getMessage());
        }
        return panierList;
    }

    public void update(Panier panier) {
        String qry = "UPDATE panier SET quantite = ? WHERE id_panier = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, panier.getQuantite());
            pstm.setInt(2, panier.getId_panier());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du panier : " + e.getMessage());
        }
    }

    public void delete(Panier panier) {
        String qry = "DELETE FROM panier WHERE id_panier = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, panier.getId_panier());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du produit dans le panier : " + e.getMessage());
        }
    }
}
