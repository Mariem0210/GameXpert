/*
package tn.esprit.services;

import tn.esprit.interfaces.IServiceFormation;
import tn.esprit.models.Certificat;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCertificat implements IServiceFormation<Certificat> {
    private Connection cnx;

    public ServiceCertificat() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Certificat certificat) {
        String qry = "INSERT INTO certificat (idu, idf, validitec, datec, description) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, certificat.getIdu());
            pstm.setInt(2, certificat.getIdf());
            pstm.setString(3, certificat.getValiditec());
            pstm.setDate(4, java.sql.Date.valueOf(certificat.getDatec()));
            pstm.setString(5, certificat.getDescription());


            pstm.executeUpdate();
            System.out.println("Certificat ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du certificat : " + e.getMessage());
        }
    }

    @Override
    public List<Certificat> getAll() {
        List<Certificat> certificats = new ArrayList<>();
        String qry = "SELECT * FROM certificat";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Certificat c = new Certificat();
                c.setIdc(rs.getInt("idc"));
                c.setIdu(rs.getInt("idu"));
                c.setIdf(rs.getInt("idf"));
                c.setValiditec(rs.getString("validitec"));
                c.setDatec(rs.getDate("datec").toLocalDate());
                c.setDescription(rs.getString("description"));

                certificats.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des certificats : " + e.getMessage());
        }

        return certificats;
    }

    @Override
    public void update(Certificat certificat) {
        String qryCheckExist = "SELECT COUNT(*) FROM certificat WHERE idc = ?";
        String qryUpdate = "UPDATE certificat SET idu = ?, idf = ?, validitec = ?, datec = ?, description = ? WHERE idc = ?";

        try {
            // Vérifie si le certificat existe avant de faire l'update
            PreparedStatement pstmCheck = cnx.prepareStatement(qryCheckExist);
            pstmCheck.setInt(1, certificat.getIdc());
            ResultSet rs = pstmCheck.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                // Si le certificat existe, effectue la mise à jour
                PreparedStatement pstmUpdate = cnx.prepareStatement(qryUpdate);
                pstmUpdate.setInt(1, certificat.getIdu());
                pstmUpdate.setInt(2, certificat.getIdf());
                pstmUpdate.setString(3, certificat.getValiditec());
                pstmUpdate.setDate(4, java.sql.Date.valueOf(certificat.getDatec()));
                pstmUpdate.setString(5, certificat.getDescription());
                pstmUpdate.setInt(6, certificat.getIdc());

                int rowsAffected = pstmUpdate.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Certificat mis à jour avec succès !");
                } else {
                    System.out.println("Aucune mise à jour effectuée (aucune ligne modifiée).");
                }
            } else {
                System.out.println("Le certificat avec l'ID " + certificat.getIdc() + " n'existe pas.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du certificat : " + e.getMessage());
        }
    }

    @Override
    public void delete(Certificat certificat) {
        String qry = "DELETE FROM certificat WHERE idc = ?";

        try {
            // Préparation de la requête SQL pour supprimer le certificat
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, certificat.getIdc());  // Remplacement du "?" par l'ID du certificat à supprimer

            // Exécution de la requête
            int rowsAffected = pstm.executeUpdate();

            // Vérification si une ligne a été supprimée
            if (rowsAffected > 0) {
                System.out.println("Certificat supprimé avec succès !");
            } else {
                System.out.println("Aucun certificat trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du certificat : " + e.getMessage());
        }
    }
}*/
