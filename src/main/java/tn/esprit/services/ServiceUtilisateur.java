package tn.esprit.services;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Utilisateur;
import tn.esprit.utils.MyDatabase;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.management.openmbean.InvalidKeyException;
import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceUtilisateur implements IService<Utilisateur> {
    private Connection cnx;
    private String imagePath;

    Encryptor encryptor = new Encryptor();

    byte[] encryptionKey = {65, 12, 12, 12, 12, 12, 12, 12, 12,
            12, 12, 12, 12, 12, 12, 12 };

    public ServiceUtilisateur() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Utilisateur utilisateur) {
        String qry = "INSERT INTO utilisateur (nomu, prenomu, typeu, mailu, mdpu, datenaissanceu, dateinscriu, numtelu, photo_profilu) VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, utilisateur.getNomu());
            pstm.setString(2, utilisateur.getPrenomu());
            pstm.setString(3, utilisateur.getTypeu());
            pstm.setString(4, utilisateur.getMailu());
            pstm.setString(5, encryptor.encrypt(utilisateur.getMdpu(), encryptionKey));
            pstm.setDate(6, java.sql.Date.valueOf(utilisateur.getDatenaissanceu()));
            pstm.setInt(7, utilisateur.getNumtelu());
            pstm.setString(8, utilisateur.getPhoto_de_profile());
            pstm.executeUpdate();
        } catch (SQLException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | java.security.InvalidKeyException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Utilisateur utilisateur) {
        String qry = "UPDATE `utilisateur` SET `nomu`=?, `prenomu`=?, `numtelu`=?, `mailu`=?, `mdpu`=? WHERE `idu`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, utilisateur.getNomu());
            pstm.setString(2, utilisateur.getPrenomu());
            pstm.setInt(3, utilisateur.getNumtelu());
            pstm.setString(4, utilisateur.getMailu());
            pstm.setString(5, encryptor.encrypt(utilisateur.getMdpu(), encryptionKey));
            pstm.setInt(6, utilisateur.getIdu());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (java.security.InvalidKeyException e) {
            throw new RuntimeException(e);
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
        String req = "SELECT * FROM utilisateur WHERE mailu=? AND mdpu=?;";
        int id = -1;
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, mailu);
            pst.setString(2, encryptor.encrypt(mdpu,encryptionKey));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (java.security.InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return id != -1;
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
                user.setIdu(rs.getInt("idu"));
                user.setNomu(rs.getString("nomu"));
                user.setPrenomu(rs.getString("prenomu"));
                user.setNumtelu(rs.getInt("numtelu"));
                user.setMailu(rs.getString("mailu"));
                user.setMdpu(encryptor.decrypt(rs.getString("mdpu"),encryptionKey));
                user.setTypeu(rs.getString("typeu"));
                user.setDateinscriu(rs.getDate("dateinscriu").toLocalDate());
                user.setDatenaissanceu(rs.getDate("datenaissanceu").toLocalDate());
                user.setPhoto_de_profile(rs.getString("photo_profilu"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (java.security.InvalidKeyException e) {
            throw new RuntimeException(e);
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

    public boolean isEmailAvailable(String mailu) throws SQLException {
        int id = -1;
        String req = "SELECT * FROM utilisateur WHERE mailu = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, mailu);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id == -1;
    }

    public boolean isValidPhoneNumber(String numtelu) {
        return numtelu.matches("[2459]\\d{7}");
    }

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(EMAIL_REGEX);

    public boolean validateEmail(String mailu) {
        Matcher matcher = EMAIL_PATTERN.matcher(mailu);
        return matcher.matches();
    }

    public boolean isAlpha(String chaine) {
        return chaine.matches("[a-zA-Z- -]+");
    }

    public boolean isMdp(String chaine) {
        return chaine.length() < 6 ? false : chaine.matches("[a-zA-Z-0-9]+");
    }

    public boolean isValidBirthDate(String datenaissanceu) {
        return datenaissanceu.matches("^(\\d{4})-(\\d{2})-(\\d{2})$");
    }

    public String getPassword(TextField Visiblepassword, PasswordField Hiddenpassword) {
        if (Visiblepassword.isVisible()) {
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

    public List<Utilisateur> afficher() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String qry = "SELECT * FROM `utilisateur` WHERE `typeu` = 'COACH' OR `typeu` = 'JOUEUR'";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setIdu(rs.getInt("idu"));
                utilisateur.setNomu(rs.getString("nomu"));
                utilisateur.setPrenomu(rs.getString("prenomu"));
                utilisateur.setNumtelu(rs.getInt("numtelu"));
                utilisateur.setMailu(rs.getString("mailu"));
                utilisateur.setMdpu(rs.getString("mdpu"));
                utilisateur.setTypeu(rs.getString("typeu"));
                utilisateur.setDateinscriu(rs.getDate("dateinscriu").toLocalDate());
                utilisateur.setDatenaissanceu(rs.getDate("datenaissanceu").toLocalDate());

                // Affichage dans la console pour vérifier les données récupérées
                System.out.println("Utilisateur : " + utilisateur.getNomu() + " | Type : " + utilisateur.getTypeu());

                utilisateurs.add(utilisateur);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return utilisateurs;
    }

    public Image loadImage(String filePath) {
        try {
            File file = new File(filePath);
            return new Image(file.toURI().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int generer(){
        Random rand = new Random();
        int code = rand.nextInt((9999 - 1000) + 1) + 1000;
        return code;
    }
}
