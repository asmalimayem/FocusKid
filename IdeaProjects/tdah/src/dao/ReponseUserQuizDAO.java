package dao;

import model.ReponseUserQuiz;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseUserQuizDAO {

    public void ajouter(ReponseUserQuiz reponse) {
        String query = "INSERT INTO reponse_user_quiz (id_user, id_quiz, id_question, id_correction, est_correcte, temps_reponse) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reponse.getIdUser());
            stmt.setInt(2, reponse.getIdQuiz());
            stmt.setInt(3, reponse.getIdQuestion());
            stmt.setInt(4, reponse.getIdCorrection());
            stmt.setBoolean(5, reponse.isEstCorrecte());
            stmt.setInt(6, reponse.getTempsReponse());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                reponse.setIdRepUser(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ReponseUserQuiz> getByUserAndQuiz(int idUser, int idQuiz) {
        List<ReponseUserQuiz> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponse_user_quiz WHERE id_user=? AND id_quiz=? ORDER BY date_reponse";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUser);
            stmt.setInt(2, idQuiz);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ReponseUserQuiz reponse = new ReponseUserQuiz();
                reponse.setIdRepUser(rs.getInt("id_rep_user"));
                reponse.setIdUser(rs.getInt("id_user"));
                reponse.setIdQuiz(rs.getInt("id_quiz"));
                reponse.setIdQuestion(rs.getInt("id_question"));
                reponse.setIdCorrection(rs.getInt("id_correction"));
                reponse.setEstCorrecte(rs.getBoolean("est_correcte"));
                reponse.setTempsReponse(rs.getInt("temps_reponse"));
                reponse.setDateReponse(rs.getTimestamp("date_reponse").toLocalDateTime());
                reponses.add(reponse);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reponses;
    }
}