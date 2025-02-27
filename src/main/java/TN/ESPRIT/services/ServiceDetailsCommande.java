package tn.esprit.services;

import tn.esprit.models.DetailsCommande;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDetailsCommande {
    private final Connection cnx;

    public ServiceDetailsCommande() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public void add(DetailsCommande detail) {
        String qry = "INSERT INTO details_commande(id_commande, id_produit, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, detail.getId_commande());
            pstm.setInt(2, detail.getId_produit());
            pstm.setInt(3, detail.getQuantite());
            pstm.setFloat(4, detail.getPrix_unitaire());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du détail de commande : " + e.getMessage());
        }
    }
}
