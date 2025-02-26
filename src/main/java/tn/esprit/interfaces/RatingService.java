
package tn.esprit.interfaces;




import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RatingService {

    private static final Connection connection = MyDatabase.getInstance().getCnx();

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
}
