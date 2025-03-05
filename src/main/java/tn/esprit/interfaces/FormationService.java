package tn.esprit.interfaces;

import tn.esprit.models.Formation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



import tn.esprit.utils.MyDatabase;
import tn.esprit.interfaces.IServiceFormation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormationService implements IServiceFormation<Formation> {
    private Connection cnx;

    public FormationService() {
        cnx = MyDatabase.getInstance().getCnx();
    }


    @Override
    public void add(Formation formation) {
        String qry = "INSERT INTO formation (nomf, descriptionf, niveauf, dateDebutf, dateFinf, capacitef, prixf, idu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomf());
            pstm.setString(2, formation.getDescriptionf());
            pstm.setString(3, formation.getNiveauf());
            pstm.setDate(4, java.sql.Date.valueOf(formation.getDateDebutf()));
            pstm.setDate(5, java.sql.Date.valueOf(formation.getDateFinf()));
            pstm.setInt(6, formation.getCapacitef());
            pstm.setDouble(7, formation.getPrixf());
            pstm.setInt(8, formation.getIdu());

            pstm.executeUpdate();
            System.out.println("Formation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la formation : " + e.getMessage());
        }
    }


    @Override
    public void modifierFormation(Formation formation) {
        String qry = "UPDATE formation SET nomf=?, descriptionf=?, niveauf=?, dateDebutf=?, dateFinf=?, capacitef=?, prixf=?, idu=? WHERE idf=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomf());
            pstm.setString(2, formation.getDescriptionf());
            pstm.setString(3, formation.getNiveauf());
            pstm.setDate(4, Date.valueOf(formation.getDateDebutf()));
            pstm.setDate(5, Date.valueOf(formation.getDateFinf()));
            pstm.setInt(6, formation.getCapacitef());
            pstm.setFloat(7, formation.getPrixf());
            pstm.setInt(8, formation.getIdu());
            pstm.setInt(9, formation.getIdf());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Formation mise à jour avec succès !");
            } else {
                System.out.println("Aucune formation mise à jour. Vérifiez l'ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la formation : " + e.getMessage());
        }
    }







    @Override
    public void supprimerFormation(Formation formation, String nomFormation) throws SQLException {
        String sql = "DELETE FROM formation WHERE nomf=?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, nomFormation);

        int rowsDeleted = st.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Formation supprimée avec succès !");
        } else {
            System.out.println("Aucune formation supprimée. Vérifiez le nom de la formation.");
        }
    }


    @Override
    public List<Formation> recupererFormations() throws SQLException {
        String sql = "SELECT * FROM formation";  // Requête simple sans jointure

        List<Formation> formations = new ArrayList<>();
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)){
            while (rs.next()) {
                Formation f = new Formation();
                f.setIdf(rs.getInt("idf"));
                f.setNomf(rs.getString("nomf"));
                f.setDescriptionf(rs.getString("descriptionf"));
                f.setNiveauf(rs.getString("niveauf"));
                f.setDateDebutf(rs.getDate("dateDebutf").toLocalDate());
                f.setDateFinf(rs.getDate("dateFinf").toLocalDate());

                f.setCapacitef(rs.getInt("capacitef"));
                f.setPrixf(rs.getFloat("prixf"));
                f.setIdu(rs.getInt("idu"));



                formations.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des formations : " + e.getMessage());
        }

        return formations;
    }
}