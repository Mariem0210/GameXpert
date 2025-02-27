package tn.esprit.services;

import tn.esprit.models.Commande;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommande {
    private final Connection cnx;

    public ServiceCommande() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public void add(Commande commande) {
        String qry = "INSERT INTO commande(date_commande, montant_total, id_utilisateur) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDate(1, new java.sql.Date(commande.getDate_commande().getTime()));
            pstm.setFloat(2, commande.getMontant_total());
            pstm.setInt(3, commande.getId_utilisateur());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la commande : " + e.getMessage());
        }
    }

    public List<Commande> getAll() {
        List<Commande> commandes = new ArrayList<>();
        String qry = "SELECT * FROM commande";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Commande c = new Commande(
                        rs.getInt("id_commande"),
                        rs.getDate("date_commande"),
                        rs.getFloat("montant_total"),
                        rs.getInt("id_utilisateur")
                );
                commandes.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes : " + e.getMessage());
        }
        return commandes;
    }

    public void delete(Commande commande) {
        String qry = "DELETE FROM commande WHERE id_commande=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, commande.getId_commande());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la commande : " + e.getMessage());
        }
    }
}
