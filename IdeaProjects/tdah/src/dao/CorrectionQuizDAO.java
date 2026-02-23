package dao;

import model.CorrectionQuiz;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CorrectionQuizDAO {

    public void ajouter(CorrectionQuiz correction) {
        String query = "INSERT INTO correction_quiz (id_question, contenu, est_correcte) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, correction.getIdQuestion());
            stmt.setString(2, correction.getContenu());
            stmt.setBoolean(3, correction.isEstCorrecte());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    correction.setIdCorrection(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout correction quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<CorrectionQuiz> getByQuestion(int idQuestion) {
        List<CorrectionQuiz> corrections = new ArrayList<>();
        String query = "SELECT * FROM correction_quiz WHERE id_question = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idQuestion);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CorrectionQuiz c = new CorrectionQuiz();
                c.setIdCorrection(rs.getInt("id_correction"));
                c.setIdQuestion(rs.getInt("id_question"));
                c.setContenu(rs.getString("contenu"));
                c.setEstCorrecte(rs.getBoolean("est_correcte"));
                corrections.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return corrections;
    }

    public void supprimerParQuestion(int idQuestion) {
        String query = "DELETE FROM correction_quiz WHERE id_question = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idQuestion);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression corrections: " + e.getMessage());
            e.printStackTrace();
        }
    }
}