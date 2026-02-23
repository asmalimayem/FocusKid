package dao;

import model.Quiz;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    public void ajouter(Quiz quiz) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO quiz (id_cours, titre, description, difficulte, points_total, duree_totale, est_actif) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, quiz.getIdCours());
            stmt.setString(2, quiz.getTitre());
            stmt.setString(3, quiz.getDescription());
            stmt.setString(4, quiz.getDifficulte());
            stmt.setInt(5, quiz.getPointsTotal());
            stmt.setInt(6, quiz.getDureeTotale());
            stmt.setBoolean(7, quiz.isEstActif());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    quiz.setIdQuiz(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout quiz: " + e.getMessage());
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

    public List<Quiz> getAll() {
        List<Quiz> quizs = new ArrayList<>();
        String query = "SELECT * FROM quiz WHERE est_actif = true ORDER BY id_quiz DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setIdQuiz(rs.getInt("id_quiz"));
                quiz.setIdCours(rs.getInt("id_cours"));
                quiz.setTitre(rs.getString("titre"));
                quiz.setDescription(rs.getString("description"));
                quiz.setDifficulte(rs.getString("difficulte"));
                quiz.setPointsTotal(rs.getInt("points_total"));
                quiz.setDureeTotale(rs.getInt("duree_totale"));
                quiz.setEstActif(rs.getBoolean("est_actif"));
                quizs.add(quiz);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur getAll quiz: " + e.getMessage());
            e.printStackTrace();
        }
        return quizs;
    }

    public void modifier(Quiz quiz) {
        String query = "UPDATE quiz SET titre=?, description=?, difficulte=?, points_total=?, duree_totale=? WHERE id_quiz=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, quiz.getTitre());
            stmt.setString(2, quiz.getDescription());
            stmt.setString(3, quiz.getDifficulte());
            stmt.setInt(4, quiz.getPointsTotal());
            stmt.setInt(5, quiz.getDureeTotale());
            stmt.setInt(6, quiz.getIdQuiz());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Erreur modification quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void supprimer(int id) {
        String query = "DELETE FROM quiz WHERE id_quiz=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean supprimerAvecDependances(int idQuiz) {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Supprimer les réponses
            String sqlReponses = "DELETE FROM reponse_user_quiz WHERE id_quiz = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlReponses)) {
                stmt.setInt(1, idQuiz);
                stmt.executeUpdate();
            }

            // Supprimer les corrections
            String sqlCorrections = "DELETE FROM correction_quiz WHERE id_question IN (SELECT id_question FROM question_quiz WHERE id_quiz = ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCorrections)) {
                stmt.setInt(1, idQuiz);
                stmt.executeUpdate();
            }

            // Supprimer les questions
            String sqlQuestions = "DELETE FROM question_quiz WHERE id_quiz = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlQuestions)) {
                stmt.setInt(1, idQuiz);
                stmt.executeUpdate();
            }

            // Supprimer le quiz
            String sqlQuiz = "DELETE FROM quiz WHERE id_quiz = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlQuiz)) {
                stmt.setInt(1, idQuiz);
                if (stmt.executeUpdate() > 0) {
                    success = true;
                }
            }

            if (success) {
                conn.commit();
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }
}