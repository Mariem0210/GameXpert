package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Formation;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormation implements IService<Formation> {
    private Connection cnx;

    public ServiceFormation() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Formation formation) {
        String qry = "INSERT INTO formation (nomf, descriptionf, niveauf, date_debutf, date_finf, capacitef, prixf, idu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
    public List<Formation> getAll() {
        List<Formation> formations = new ArrayList<>();
        String qry = "SELECT * FROM formation";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Formation f = new Formation();
                f.setIdf(rs.getInt("idf"));
                f.setNomf(rs.getString("nomf"));
                f.setDescriptionf(rs.getString("descriptionf"));
                f.setNiveauf(rs.getString("niveauf"));
                f.setDateDebutf(rs.getDate("date_debutf").toLocalDate());
                f.setDateFinf(rs.getDate("date_finf").toLocalDate());
                f.setCapacitef(rs.getInt("capacitef"));
                f.setPrixf(rs.getDouble("prixf"));
                f.setIdu(rs.getInt("idu"));

                formations.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des formations : " + e.getMessage());
        }

        return formations;
    }


    @Override
    public void update(Formation formation) {
        String qryCheckExist = "SELECT COUNT(*) FROM formation WHERE idf = ?";
        String qryUpdate = "UPDATE formation SET nomf = ?, descriptionf = ?, niveauf = ?, date_debutf = ?, date_finf = ?, capacitef = ?, prixf = ?, idu = ? WHERE idf = ?";

        try {
            // Vérifie si la formation existe avant de faire l'update
            PreparedStatement pstmCheck = cnx.prepareStatement(qryCheckExist);
            pstmCheck.setInt(1, formation.getIdf());
            ResultSet rs = pstmCheck.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                // Si la formation existe, effectue la mise à jour
                PreparedStatement pstmUpdate = cnx.prepareStatement(qryUpdate);
                pstmUpdate.setString(1, formation.getNomf());
                pstmUpdate.setString(2, formation.getDescriptionf());
                pstmUpdate.setString(3, formation.getNiveauf());
                pstmUpdate.setDate(4, java.sql.Date.valueOf(formation.getDateDebutf()));
                pstmUpdate.setDate(5, java.sql.Date.valueOf(formation.getDateFinf()));
                pstmUpdate.setInt(6, formation.getCapacitef());
                pstmUpdate.setDouble(7, formation.getPrixf());
                pstmUpdate.setInt(8, formation.getIdu());
                pstmUpdate.setInt(9, formation.getIdf());

                int rowsAffected = pstmUpdate.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Formation mise à jour avec succès !");
                } else {
                    System.out.println("Aucune mise à jour effectuée (aucune ligne modifiée).");
                }
            } else {
                System.out.println("La formation avec l'ID " + formation.getIdf() + " n'existe pas.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la formation : " + e.getMessage());
        }
    }


    @Override
    public void delete(Formation formation) {
        String qry = "DELETE FROM formation WHERE idf = ?";

        try {
            // Préparation de la requête SQL pour supprimer la formation
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, formation.getIdf());  // Remplacement du "?" par l'ID de la formation à supprimer

            // Exécution de la requête
            int rowsAffected = pstm.executeUpdate();

            // Vérification si une ligne a été supprimée
            if (rowsAffected > 0) {
                System.out.println("Formation supprimée avec succès !");
            } else {
                System.out.println("Aucune formation trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la formation : " + e.getMessage());
        }
    }

}
