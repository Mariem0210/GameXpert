package tn.esprit.services;



import tn.esprit.interfaces.IService;
import tn.esprit.models.Giveaway;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceGiveaway implements IService<Giveaway> {
    private Connection cnx;

    public ServiceGiveaway() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Giveaway giveaway) {
        String qry = "INSERT INTO giveaway (idu, titreg, descg, datedg, datefg, statusg) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, giveaway.getIdu());
            pstm.setString(2, giveaway.getTitreg());
            pstm.setString(3, giveaway.getDescg());
            pstm.setDate(4, java.sql.Date.valueOf(giveaway.getDatedg()));
            pstm.setDate(5, java.sql.Date.valueOf(giveaway.getDatefg()));
            pstm.setString(6, giveaway.getStatusg());

            pstm.executeUpdate();
            System.out.println("Giveaway ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du giveaway : " + e.getMessage());
        }
    }

    @Override
    public List<Giveaway> getAll() {
        List<Giveaway> giveaways = new ArrayList<>();
        String qry = "SELECT * FROM giveaway";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Giveaway g = new Giveaway();
                g.setIdg(rs.getInt("idg"));
                g.setIdu(rs.getInt("idu"));
                g.setTitreg(rs.getString("titreg"));
                g.setDescg(rs.getString("descg"));
                g.setDatedg(rs.getDate("datedg").toLocalDate());
                g.setDatefg(rs.getDate("datefg").toLocalDate());
                g.setStatusg(rs.getString("statusg"));

                giveaways.add(g);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des giveaways : " + e.getMessage());
        }

        return giveaways;
    }

    @Override
    public void update(Giveaway giveaway) {
        String qryCheckExist = "SELECT COUNT(*) FROM giveaway WHERE idg = ?";
        String qryUpdate = "UPDATE giveaway SET idu = ?, titreg = ?, descg = ?, datedg = ?, datefg = ?, statusg = ? WHERE idg = ?";

        try {
            // Vérification si le giveaway existe avant la mise à jour
            PreparedStatement pstmCheck = cnx.prepareStatement(qryCheckExist);
            pstmCheck.setInt(1, giveaway.getIdg());
            ResultSet rs = pstmCheck.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                // Mise à jour des données
                PreparedStatement pstmUpdate = cnx.prepareStatement(qryUpdate);
                pstmUpdate.setInt(1, giveaway.getIdu());
                pstmUpdate.setString(2, giveaway.getTitreg());
                pstmUpdate.setString(3, giveaway.getDescg());
                pstmUpdate.setDate(4, java.sql.Date.valueOf(giveaway.getDatedg()));
                pstmUpdate.setDate(5, java.sql.Date.valueOf(giveaway.getDatefg()));
                pstmUpdate.setString(6, giveaway.getStatusg());
                pstmUpdate.setInt(7, giveaway.getIdg());

                int rowsAffected = pstmUpdate.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Giveaway mis à jour avec succès !");
                } else {
                    System.out.println("Aucune mise à jour effectuée.");
                }
            } else {
                System.out.println("Le giveaway avec l'ID " + giveaway.getIdg() + " n'existe pas.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du giveaway : " + e.getMessage());
        }
    }

    @Override
    public void delete(Giveaway giveaway) {
        String qry = "DELETE FROM giveaway WHERE idg = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, giveaway.getIdg());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Giveaway supprimé avec succès !");
            } else {
                System.out.println("Aucun giveaway trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du giveaway : " + e.getMessage());
        }
    }
}
