package dao;

import model.Exercice;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciceDAO {

    public void ajouter(Exercice exercice) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            System.out.println("✅ Connexion obtenue pour ExerciceDAO");

            String query = "INSERT INTO exercice (id_cours, titre, description, difficulte, points_total, duree, est_actif) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, exercice.getIdCours());
            stmt.setString(2, exercice.getTitre());
            stmt.setString(3, exercice.getDescription());
            stmt.setString(4, exercice.getDifficulte());
            stmt.setInt(5, exercice.getPointsTotal());
            stmt.setInt(6, exercice.getDuree());
            stmt.setBoolean(7, exercice.isEstActif());

            int affectedRows = stmt.executeUpdate();
            System.out.println("📊 Lignes affectées: " + affectedRows);

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    exercice.setIdExercice(rs.getInt(1));
                    System.out.println("🆔 ID généré: " + exercice.getIdExercice());
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout exercice: " + e.getMessage());
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

    public List<Exercice> getAll() {
        List<Exercice> exercices = new ArrayList<>();
        String query = "SELECT * FROM exercice WHERE est_actif = true ORDER BY id_exercice DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Exercice exercice = new Exercice();
                exercice.setIdExercice(rs.getInt("id_exercice"));
                exercice.setIdCours(rs.getInt("id_cours"));
                exercice.setTitre(rs.getString("titre"));
                exercice.setDescription(rs.getString("description"));
                exercice.setDifficulte(rs.getString("difficulte"));
                exercice.setPointsTotal(rs.getInt("points_total"));
                exercice.setDuree(rs.getInt("duree"));
                exercice.setEstActif(rs.getBoolean("est_actif"));
                exercices.add(exercice);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur getAll: " + e.getMessage());
            e.printStackTrace();
        }
        return exercices;
    }

    public void modifier(Exercice exercice) {
        String query = "UPDATE exercice SET titre=?, description=?, difficulte=?, points_total=?, duree=? WHERE id_exercice=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, exercice.getTitre());
            stmt.setString(2, exercice.getDescription());
            stmt.setString(3, exercice.getDifficulte());
            stmt.setInt(4, exercice.getPointsTotal());
            stmt.setInt(5, exercice.getDuree());
            stmt.setInt(6, exercice.getIdExercice());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Erreur modification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void supprimer(int id) {
        String query = "DELETE FROM exercice WHERE id_exercice=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean supprimerAvecDependances(int idExercice) {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Supprimer les réponses
            String sqlReponses = "DELETE FROM reponse_user_exercice WHERE id_exercice = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlReponses)) {
                stmt.setInt(1, idExercice);
                stmt.executeUpdate();
            }

            // Supprimer les corrections
            String sqlCorrections = "DELETE FROM correction_exercice WHERE id_exercice = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCorrections)) {
                stmt.setInt(1, idExercice);
                stmt.executeUpdate();
            }

            // Supprimer les questions
            String sqlQuestions = "DELETE FROM question_exercice WHERE id_exercice = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlQuestions)) {
                stmt.setInt(1, idExercice);
                stmt.executeUpdate();
            }

            // Supprimer l'exercice
            String sqlExercice = "DELETE FROM exercice WHERE id_exercice = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlExercice)) {
                stmt.setInt(1, idExercice);
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