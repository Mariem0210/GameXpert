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
    public void modifierFormation(Formation formation, int idf) throws SQLException {
        String sql = "UPDATE formation SET nomf=?, dateDebutf=?, dateFinf=?, capacitef=?, prixf=?, niveauf=?, descriptionf=?, idu=? WHERE idf=?";
        PreparedStatement statement = cnx.prepareStatement(sql);

        // Correction des indices des paramètres SQL
        statement.setString(1, formation.getNomf());
        statement.setDate(2, Date.valueOf(formation.getDateDebutf())); // Conversion LocalDate -> Date SQL
        statement.setDate(2, Date.valueOf(formation.getDateFinf()));  // Assure-toi que 'formation.getDateFinf()' renvoie un java.sql.Date
        statement.setInt(4, formation.getCapacitef());
        statement.setFloat(5, formation.getPrixf());
        statement.setString(6, formation.getNiveauf());
        statement.setString(7, formation.getDescriptionf());
        statement.setInt(8, formation.getIdu());
        statement.setInt(9, idf);  // Utilisation correcte du paramètre 'idf' pour WHERE

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Compagne mise à jour avec succès !");
        } else {
            System.out.println("Aucune compagne mise à jour. Vérifiez l'ID.");
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
                f.setIdf(rs.getInt("idu"));



                formations.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des formations : " + e.getMessage());
        }

        return formations;
    }
}
