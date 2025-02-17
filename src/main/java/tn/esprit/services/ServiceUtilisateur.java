package tn.esprit.services;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Utilisateur;
import tn.esprit.utils.MyDatabase;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.management.openmbean.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
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
    public boolean verifierLogin(String mailu, String mdpu) throws SQLException {

        String req = "SELECT * FROM utilisateur WHERE mailu=? AND mdpu=? ;";
        int id = -1;
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, mailu);
            pst.setString(2,mdpu);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return id != -1;
    }

    public String getMdpu(TextField Visiblepassword, PasswordField Hiddenpassword) {
        if (Visiblepassword.isVisible()) {
            return Visiblepassword.getText();
        } else {
            return Hiddenpassword.getText();
        }
    }
    public  boolean areFieldsNotEmpty(List<String> fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    public Utilisateur getUser(int i) throws SQLException {
        Utilisateur connectedUser = null;
        String req = "SELECT * FROM utilisateur WHERE idu = ?";

        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, i);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                connectedUser = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"), // Mot de passe sans cryptage
                        rs.getString("UserType"),
                        rs.getInt("num_telephone"),
                        rs.getDate("date_de_naissance").toLocalDate(),
                        rs.getDate("date_inscription").toLocalDate()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connectedUser;
    }
    public int getUserId(String mailu) throws SQLException {
        String req = "SELECT idu FROM utilisateur WHERE mailu = ?";
        int i = 0;

        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, mailu);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                i = rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return i;
    }




}

