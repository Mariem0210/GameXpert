package tn.esprit.interfaces;

import tn.esprit.utils.MyDatabase;
import tn.esprit.models.Rating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingService {
    private static final Connection connection = MyDatabase.getInstance().getCnx();

    // Enregistrer ou mettre à jour une note
    public static void enregistrerNote(int idf, int note) {
        String sql = "INSERT INTO rating (idf, note) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE note = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idf);
            preparedStatement.setInt(2, note);
            preparedStatement.setInt(3, note);

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
        String query = "SELECT * FROM rating";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                ratings.add(new Rating(
                        rs.getInt("id"),
                        rs.getInt("idf"),
                        rs.getInt("note")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }
}
