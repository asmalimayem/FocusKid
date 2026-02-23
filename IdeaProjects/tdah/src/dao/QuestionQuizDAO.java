package dao;

import model.QuestionQuiz;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionQuizDAO {

    public void ajouter(QuestionQuiz question) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO question_quiz (id_quiz, enonce, type_question, points, temps_limite, ordre) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, question.getIdQuiz());
            stmt.setString(2, question.getEnonce());
            stmt.setString(3, question.getTypeQuestion());
            stmt.setInt(4, question.getPoints());
            stmt.setInt(5, question.getTempsLimite());
            stmt.setInt(6, question.getOrdre());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    question.setIdQuestion(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout question quiz: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<QuestionQuiz> getByQuiz(int idQuiz) {
        List<QuestionQuiz> questions = new ArrayList<>();
        String query = "SELECT * FROM question_quiz WHERE id_quiz = ? ORDER BY ordre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idQuiz);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                QuestionQuiz q = new QuestionQuiz();
                q.setIdQuestion(rs.getInt("id_question"));
                q.setIdQuiz(rs.getInt("id_quiz"));
                q.setEnonce(rs.getString("enonce"));
                q.setTypeQuestion(rs.getString("type_question"));
                q.setPoints(rs.getInt("points"));
                q.setTempsLimite(rs.getInt("temps_limite"));
                q.setOrdre(rs.getInt("ordre"));
                questions.add(q);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur getByQuiz: " + e.getMessage());
            e.printStackTrace();
        }
        return questions;
    }

    public QuestionQuiz getById(int id) {
        String query = "SELECT * FROM question_quiz WHERE id_question = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                QuestionQuiz q = new QuestionQuiz();
                q.setIdQuestion(rs.getInt("id_question"));
                q.setIdQuiz(rs.getInt("id_quiz"));
                q.setEnonce(rs.getString("enonce"));
                q.setTypeQuestion(rs.getString("type_question"));
                q.setPoints(rs.getInt("points"));
                q.setTempsLimite(rs.getInt("temps_limite"));
                q.setOrdre(rs.getInt("ordre"));
                return q;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void supprimerParQuiz(int idQuiz) {
        String query = "DELETE FROM question_quiz WHERE id_quiz = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idQuiz);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression questions: " + e.getMessage());
            e.printStackTrace();
        }
    }
}