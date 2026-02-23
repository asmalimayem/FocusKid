package dao;

import model.QuestionExercice;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionExerciceDAO {

    public void ajouter(QuestionExercice question) {
        String query = "INSERT INTO question_exercice (id_exercice, enonce, ordre) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, question.getIdExercice());
            stmt.setString(2, question.getEnonce());
            stmt.setInt(3, question.getOrdre());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                question.setIdQuestionExercice(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<QuestionExercice> getByExercice(int idExercice) {
        List<QuestionExercice> questions = new ArrayList<>();
        String query = "SELECT * FROM question_exercice WHERE id_exercice = ? ORDER BY ordre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            System.out.println("📊 Chargement des questions pour l'exercice ID: " + idExercice);
            stmt.setInt(1, idExercice);
            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                QuestionExercice question = mapResultSetToQuestion(rs);
                questions.add(question);
                count++;
                System.out.println("  → Question " + count + ": " + question.getEnonce() + " (ID: " + question.getIdQuestionExercice() + ")");
            }

            System.out.println("✅ Total questions chargées: " + count);

        } catch (SQLException e) {
            System.err.println("❌ Erreur chargement questions: " + e.getMessage());
            e.printStackTrace();
        }

        return questions;
    }

    public QuestionExercice getById(int id) {
        String query = "SELECT * FROM question_exercice WHERE id_question_exercice = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToQuestion(rs);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur getById question: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public void modifier(QuestionExercice question) {
        String query = "UPDATE question_exercice SET enonce=?, ordre=? WHERE id_question_exercice=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            System.out.println("📝 Modification question ID: " + question.getIdQuestionExercice());

            stmt.setString(1, question.getEnonce());
            stmt.setInt(2, question.getOrdre());
            stmt.setInt(3, question.getIdQuestionExercice());

            int affectedRows = stmt.executeUpdate();
            System.out.println("📊 Lignes modifiées: " + affectedRows);

            if (affectedRows > 0) {
                System.out.println("✅ Question modifiée avec succès!");
            } else {
                System.err.println("❌ Aucune modification - question non trouvée?");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur modification question: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void supprimer(int id) {
        String query = "DELETE FROM question_exercice WHERE id_question_exercice=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            System.out.println("🗑️ Suppression question ID: " + id);
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            System.out.println("📊 Lignes supprimées: " + affectedRows);

            if (affectedRows > 0) {
                System.out.println("✅ Question supprimée avec succès!");
            } else {
                System.err.println("❌ Aucune suppression - question non trouvée?");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression question: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void supprimerParExercice(int idExercice) {
        String query = "DELETE FROM question_exercice WHERE id_exercice=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            System.out.println("🗑️ Suppression des questions pour l'exercice ID: " + idExercice);
            stmt.setInt(1, idExercice);

            int affectedRows = stmt.executeUpdate();
            System.out.println("📊 Questions supprimées: " + affectedRows);

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression questions par exercice: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private QuestionExercice mapResultSetToQuestion(ResultSet rs) throws SQLException {
        QuestionExercice question = new QuestionExercice();
        question.setIdQuestionExercice(rs.getInt("id_question_exercice"));
        question.setIdExercice(rs.getInt("id_exercice"));
        question.setEnonce(rs.getString("enonce"));
        question.setOrdre(rs.getInt("ordre"));
        return question;
    }
}