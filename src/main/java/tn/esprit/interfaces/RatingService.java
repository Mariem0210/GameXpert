package tn.esprit.interfaces;

import tn.esprit.utils.MyDatabase;
import tn.esprit.models.Rating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingService {
    private static final Connection connection = MyDatabase.getInstance().getCnx();

    // Enregistrer ou mettre à jour une note
    public static void enregistrerNote(int idf, double note) {
        String sql = "INSERT INTO rating (idf, note) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE note = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idf);
            preparedStatement.setDouble(2, note);  // Utilisez setDouble ici pour accepter un double
            preparedStatement.setDouble(3, note);  // Utilisez setDouble ici aussi

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Note enregistrée avec succès.");
            } else {
                System.out.println("Erreur lors de l'enregistrement de la note.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    // Récupérer toutes les notes
    public List<Rating> getAllRatings() {
        List<Rating> ratings = new ArrayList<>();
        String query = "SELECT r.id, r.idf, r.note, f.nomf " +
                "FROM rating r " +
                "JOIN formation f ON r.idf = f.idf";  // Ne récupère que 'nomf'

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Rating rating = new Rating();
                rating.setNote(resultSet.getFloat("note"));
                rating.setNomf(resultSet.getString("nomf"));  // Récupère le nom de la formation
                ratings.add(rating);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des notations : " + e.getMessage());
        }

        return ratings;
    }

}
