package tn.esprit.interfaces;

import tn.esprit.models.Certificat;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CertificatService {
    private Connection cnx;

    public CertificatService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Ajouter un Certificat
    public void add(Certificat certificat) {
        String qry = "INSERT INTO certificat (idf, idu, nomc, typec, scorec, etatc, dateExpirationc) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, certificat.getIdf());  // idf
            pstm.setInt(2, certificat.getIdu());  // idu
            pstm.setString(3, certificat.getNomc());
            pstm.setString(4, certificat.getTypec());
            pstm.setFloat(5, certificat.getScorec());
            pstm.setString(6, certificat.getEtatc());
            pstm.setDate(7, java.sql.Date.valueOf(certificat.getDateExpirationc()));

            pstm.executeUpdate();
            System.out.println("Certificat ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du certificat : " + e.getMessage());
        }
    }

    // Modifier un Certificat
    public void modifierCertificat(Certificat certificat) {
        String qry = "UPDATE certificat SET idf=?, idu=?, nomc=?, typec=?, scorec=?, etatc=?, dateExpirationc=? WHERE idc=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, certificat.getIdf());  // idf
            pstm.setInt(2, certificat.getIdu());  // idu
            pstm.setString(3, certificat.getNomc());
            pstm.setString(4, certificat.getTypec());
            pstm.setFloat(5, certificat.getScorec());
            pstm.setString(6, certificat.getEtatc());
            pstm.setDate(7, java.sql.Date.valueOf(certificat.getDateExpirationc()));
            pstm.setInt(8, certificat.getIdc());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Certificat mis à jour avec succès !");
            } else {
                System.out.println("Aucun certificat mis à jour. Vérifiez l'ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du certificat : " + e.getMessage());
        }
    }

    // Supprimer un Certificat par titre
    public void supprimerCertificat(Certificat certificat, String titreCertificat) throws SQLException {
        String sql = "DELETE FROM certificat WHERE nomc=?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, titreCertificat);

        int rowsDeleted = st.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Certificat supprimé avec succès !");
        } else {
            System.out.println("Aucun certificat supprimé. Vérifiez le titre.");
        }
    }

    // Récupérer tous les Certificats
    public List<Certificat> recupererCertificats() throws SQLException {
        String sql = "SELECT * FROM certificat";
        List<Certificat> certificats = new ArrayList<>();

        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Certificat c = new Certificat();
                c.setIdc(rs.getInt("idc"));
                c.setIdf(rs.getInt("idf"));  // idf
                c.setIdu(rs.getInt("idu"));  // idu
                c.setNomc(rs.getString("nomc"));
                c.setTypec(rs.getString("typec"));
                c.setScorec(rs.getInt("scorec"));
                c.setEtatc(rs.getString("etatc"));
                c.setDateExpirationc(rs.getDate("dateExpirationc").toLocalDate());

                certificats.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des certificats : " + e.getMessage());
        }

        return certificats;
    }
}
