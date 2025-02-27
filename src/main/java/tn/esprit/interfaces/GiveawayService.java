package tn.esprit.interfaces;




import tn.esprit.interfaces.IServiceGiveaway;
import tn.esprit.models.Giveaway;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GiveawayService  {
    private Connection cnx;

    public GiveawayService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Ajouter un Giveaway
    public void add(Giveaway giveaway) {
        String qry = "INSERT INTO giveaway (titreg, descg, datedg, datefg,  statusg) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, giveaway.getTitreg());
            pstm.setString(2, giveaway.getDescg());
            pstm.setDate(3, java.sql.Date.valueOf(giveaway.getDatedg()));
            pstm.setDate(4, java.sql.Date.valueOf(giveaway.getDatefg()));
            //pstm.setInt(5, giveaway.getIdu());
            pstm.setString(5, giveaway.getStatusg());

            pstm.executeUpdate();
            System.out.println("Giveaway ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du giveaway : " + e.getMessage());
        }
    }

    // Modifier un Giveaway
    public void modifierGiveaway(Giveaway giveaway) {
        String qry = "UPDATE giveaway SET titreg=?, descg=?, datedg=?, datefg=?, statusg=? WHERE idg=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, giveaway.getTitreg());
            pstm.setString(2, giveaway.getDescg());
            pstm.setDate(3, java.sql.Date.valueOf(giveaway.getDatedg()));
            pstm.setDate(4, java.sql.Date.valueOf(giveaway.getDatefg()));
            //pstm.setInt(5, giveaway.getIdu());
            pstm.setString(5, giveaway.getStatusg());
            pstm.setInt(6, giveaway.getIdg());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Giveaway mis à jour avec succès !");
            } else {
                System.out.println("Aucun giveaway mis à jour. Vérifiez l'ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du giveaway : " + e.getMessage());
        }
    }

    // Supprimer un Giveaway par titre
    public void supprimerGiveaway(Giveaway giveaway ,String titreGiveaway) throws SQLException {
        String sql = "DELETE FROM giveaway WHERE titreg=?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, titreGiveaway);

        int rowsDeleted = st.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Giveaway supprimé avec succès !");
        } else {
            System.out.println("Aucun giveaway supprimé. Vérifiez le titre.");
        }
    }

    // Récupérer tous les Giveaways
    public List<Giveaway> recupererGiveaways() throws SQLException {
        String sql = "SELECT * FROM giveaway";
        List<Giveaway> giveaways = new ArrayList<>();

        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Giveaway g = new Giveaway();
                g.setIdg(rs.getInt("idg"));
                g.setTitreg(rs.getString("titreg"));
                g.setDescg(rs.getString("descg"));
                g.setDatedg(rs.getDate("datedg").toLocalDate());
                g.setDatefg(rs.getDate("datefg").toLocalDate());
                //g.setIdu(rs.getInt("idu"));
                g.setStatusg(rs.getString("statusg"));

                giveaways.add(g);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des giveaways : " + e.getMessage());
        }

        return giveaways;
    }



}

