package tn.esprit.interfaces;

import tn.esprit.models.User;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private Connection cnx;

    public UserService() {
        this.cnx = MyDatabase.getInstance().getCnx();
        if (this.cnx == null) {
            System.out.println("⚠️ Connexion non initialisée !");
        }
    }

    // ✅ Récupérer tous les utilisateurs (emails uniquement)
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/gamexpert";
        String username = "root";
        String password = "";

        String query = "SELECT mailu FROM utilisateur";  // Vérifie que 'mailu' est bien le bon champ pour l'email

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                User user = new User();
                user.setMailu(resultSet.getString("mailu"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // ✅ Récupérer les utilisateurs par type (ex: 'coach')
    public List<User> getUsersByType(String type) {
        cnx = MyDatabase.getInstance().getCnx();  // 🔗 Connexion unique
        List<User> users = new ArrayList<>();
        String query = "SELECT idu, nomu FROM utilisateur WHERE typeu = ?";  // ✅ Vérifie que 'idu' et 'nomu' existent dans ta table

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, type);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setIdu(rs.getInt("idu"));  // ✅ 'idu' est bien sélectionné
                user.setNomu(rs.getString("nomu"));  // ✅ 'nomu' est bien sélectionné
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return users;
    }
    public List<String> getCoaches() {
        List<String> coaches = new ArrayList<>();
        String sql = "SELECT nomu FROM users WHERE typeu = 'coach'";  // Assurez-vous que votre table et colonne correspondent.

        try (Connection connection = MyDatabase.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                coaches.add(resultSet.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coaches;
    }
    private boolean isConnectionClosed(Connection cnx) {
        try {
            return cnx == null || cnx.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }
}
