package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Utilisateur;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtilisateur implements IService<Utilisateur> {
    private Connection cnx;

    public ServiceUtilisateur() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Utilisateur utilisateur) {
        String qry = "INSERT INTO `utilisateur`(`nomu`, `prenomu`, `numtelu`, `mailu`, `mdpu`, `typeu`, `datenaissanceu`, `dateinscriu`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, utilisateur.getNomu());
            pstm.setString(2, utilisateur.getPrenomu());
            pstm.setInt(3, utilisateur.getNumtelu());
            pstm.setString(4, utilisateur.getMailu());
            pstm.setString(5, utilisateur.getMdpu());
            pstm.setString(6, utilisateur.getTypeu());
            pstm.setDate(7, Date.valueOf(utilisateur.getDatenaissanceu())); // Conversion de LocalDate en java.sql.Date
            pstm.setDate(8, Date.valueOf(utilisateur.getDateinscriu()));

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String qry = "SELECT * FROM `utilisateur`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setIdu(rs.getInt("idu"));
                u.setNomu(rs.getString("nomu"));
                u.setPrenomu(rs.getString("prenomu"));
                u.setNumtelu(rs.getInt("numtelu"));
                u.setMailu(rs.getString("mailu"));
                u.setMdpu(rs.getString("mdpu"));
                u.setTypeu(rs.getString("typeu"));
                u.setDatenaissanceu(rs.getDate("datenaissanceu").toLocalDate()); // Conversion de java.sql.Date en LocalDate
                u.setDateinscriu(rs.getDate("dateinscriu").toLocalDate());

                utilisateurs.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return utilisateurs;
    }

    @Override
    public void update(Utilisateur utilisateur) {
        String qry = "UPDATE `utilisateur` SET `nomu`=?, `prenomu`=?, `numtelu`=?, `mailu`=?, `mdpu`=?, `typeu`=?, `datenaissanceu`=?, `dateinscriu`=? WHERE `idu`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, utilisateur.getNomu());
            pstm.setString(2, utilisateur.getPrenomu());
            pstm.setInt(3, utilisateur.getNumtelu());
            pstm.setString(4, utilisateur.getMailu());
            pstm.setString(5, utilisateur.getMdpu());
            pstm.setString(6, utilisateur.getTypeu());
            pstm.setDate(7, Date.valueOf(utilisateur.getDatenaissanceu()));
            pstm.setDate(8, Date.valueOf(utilisateur.getDateinscriu()));
            pstm.setInt(9, utilisateur.getIdu());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Utilisateur utilisateur) {
        String qry = "DELETE FROM `utilisateur` WHERE `idu`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, utilisateur.getIdu());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
