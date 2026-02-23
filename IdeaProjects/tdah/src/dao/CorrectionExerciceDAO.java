package dao;

import model.CorrectionExercice;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CorrectionExerciceDAO {

    public void ajouter(CorrectionExercice correction) {
        String query = "INSERT INTO correction_exercice (id_exercice, id_question_exercice, solution) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, correction.getIdExercice());
            stmt.setInt(2, correction.getIdQuestionExercice());
            stmt.setString(3, correction.getSolution());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                correction.setIdCorrection(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CorrectionExercice getByQuestion(int idQuestion) {
        String query = "SELECT * FROM correction_exercice WHERE id_question_exercice = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            System.out.println("🔍 Recherche correction pour la question ID: " + idQuestion);
            stmt.setInt(1, idQuestion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CorrectionExercice correction = mapResultSetToCorrection(rs);
                System.out.println("✅ Correction trouvée: " + correction.getSolution());
                return correction;
            } else {
                System.out.println("ℹ️ Aucune correction trouvée pour cette question");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur getByQuestion: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public List<CorrectionExercice> getByExercice(int idExercice) {
        List<CorrectionExercice> corrections = new ArrayList<>();
        String query = "SELECT * FROM correction_exercice WHERE id_exercice = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            System.out.println("📊 Chargement des corrections pour l'exercice ID: " + idExercice);
            stmt.setInt(1, idExercice);
            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                CorrectionExercice correction = mapResultSetToCorrection(rs);
                corrections.add(correction);
                count++;
                System.out.println("  → Correction " + count + " pour question ID: " + correction.getIdQuestionExercice());
            }

            System.out.println("✅ Total corrections chargées: " + count);

        } catch (SQLException e) {
            System.err.println("❌ Erreur chargement corrections: " + e.getMessage());
            e.printStackTrace();
        }

        return corrections;
    }

    private CorrectionExercice mapResultSetToCorrection(ResultSet rs) throws SQLException {
        CorrectionExercice correction = new CorrectionExercice();
        correction.setIdCorrection(rs.getInt("id_correction"));
        correction.setIdExercice(rs.getInt("id_exercice"));
        correction.setIdQuestionExercice(rs.getInt("id_question_exercice"));
        correction.setSolution(rs.getString("solution"));
        return correction;
    }
    public void modifier(CorrectionExercice correction) {
        String query = "UPDATE correction_exercice SET solution=? WHERE id_correction=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, correction.getSolution());
            stmt.setInt(2, correction.getIdCorrection());

            stmt.executeUpdate();
            System.out.println("✅ Correction modifiée ID: " + correction.getIdCorrection());

        } catch (SQLException e) {
            System.err.println("❌ Erreur modification correction: " + e.getMessage());
            e.printStackTrace();
        }
    }
}