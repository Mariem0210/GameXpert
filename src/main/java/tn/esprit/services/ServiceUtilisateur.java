package tn.esprit.services;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Admin;
import tn.esprit.models.Coach;
import tn.esprit.models.Joueur;
import tn.esprit.models.Utilisateur;
import tn.esprit.utils.MyDatabase;
import java.time.LocalDate;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ServiceUtilisateur implements IService<Utilisateur> {
    private Connection cnx;


    public ServiceUtilisateur() {
        cnx = MyDatabase.getInstance().getCnx();

    }


    @Override
    public void add(Utilisateur utilisateur) {
        //create Qry SQL
        //execute Qry
        String qry = "INSERT INTO utilisateur (nomu, prenomu, typeu,  mailu, mdpu, datenaissanceu, dateinscriu,numtelu) VALUES (?, ?, ?, ?, ?, ?,NOW(), ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, utilisateur.getNomu());
            pstm.setString(2, utilisateur.getPrenomu());
            pstm.setString(3, utilisateur.getTypeu());
            pstm.setString(4, utilisateur.getMailu());
            pstm.setString(5, utilisateur.getMdpu());
            pstm.setDate(6, java.sql.Date.valueOf(utilisateur.getDatenaissanceu()));
            pstm.setInt(7, utilisateur.getNumtelu());





      // Problème possible ici !



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
    public boolean areFieldsNotEmpty(List<String> fields) {
        for (String field : fields) {
            if (field == null || field.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    public Utilisateur getUser(int userId) {
        String query = "SELECT * FROM utilisateur WHERE idu = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Utilisateur user = new Utilisateur();
                user.setIdu(rs.getInt("idu")); // Ensure you are using the correct column name
                user.setMailu(rs.getString("mailu"));
                user.setTypeu(rs.getString("typeu"));
            // Assuming you have this method
                // Set other attributes as necessary
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user is not found
    }
    public int getUserId(String mailu) {
        String query = "SELECT idu FROM utilisateur WHERE mailu = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setString(1, mailu);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idu"); // Ensure correct column name
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if email is not found
    }





    public  boolean isEmailAvailable(String mailu) throws SQLException {
        int id=-1;
        String req = "SELECT * FROM utilisateur WHERE mailu = ?";
        try{
            PreparedStatement pst = cnx.prepareStatement(req) ;
            pst.setString(1, mailu);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);

            } } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id== -1;


    }
    public  boolean isValidPhoneNumber(String numtelu) {
        return numtelu.matches("[2459]\\d{7}");
    }
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(EMAIL_REGEX);
    public  boolean validateEmail(String mailu) {
        Matcher matcher = EMAIL_PATTERN.matcher(mailu);
        return matcher.matches();
    }
    public boolean isAlpha(String chaine) {
        return chaine.matches("[a-zA-Z- -]+");
    }
    public boolean isMdp(String chaine) {
        return chaine.length() < 6 ? false : chaine.matches("[a-zA-Z-0-9]+");
    }
    public  boolean isValidBirthDate(String datenaissanceu) {
        return datenaissanceu.matches("^(\\d{4})-(\\d{2})-(\\d{2})$");
    }
    public String getPassword(TextField Visiblepassword, PasswordField Hiddenpassword ){
        if(Visiblepassword.isVisible()){
            return Visiblepassword.getText();
        } else {
            return Hiddenpassword.getText();
        }
    }
    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE mailu = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Retourne true si l'email existe
            }
        }
        return false;
    }

}

