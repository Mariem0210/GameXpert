package tn.esprit.interfaces;



import tn.esprit.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/gamexpert";
        String username = "root";
        String password = "";

        String query = "SELECT mailu FROM utilisateur";  // Assurez-vous que 'mailu' est le bon champ pour l'email

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
}

