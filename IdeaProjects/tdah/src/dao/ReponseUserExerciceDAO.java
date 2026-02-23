package dao;

import model.ReponseUserExercice;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseUserExerciceDAO {

    public void ajouter(ReponseUserExercice reponse) {
        String query = "INSERT INTO reponse_user_exercice (id_user, id_exercice, id_question_exercice, reponse, est_correcte, temps_reponse) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reponse.getIdUser());
            stmt.setInt(2, reponse.getIdExercice());
            if (reponse.getIdQuestionExercice() != null) {
                stmt.setInt(3, reponse.getIdQuestionExercice());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, reponse.getReponse());
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

    public List<ReponseUserExercice> getByUserAndExercice(int idUser, int idExercice) {
        List<ReponseUserExercice> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponse_user_exercice WHERE id_user=? AND id_exercice=? ORDER BY date_reponse";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUser);
            stmt.setInt(2, idExercice);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ReponseUserExercice reponse = new ReponseUserExercice();
                reponse.setIdRepUser(rs.getInt("id_rep_user"));
                reponse.setIdUser(rs.getInt("id_user"));
                reponse.setIdExercice(rs.getInt("id_exercice"));
                reponse.setIdQuestionExercice(rs.getInt("id_question_exercice"));
                reponse.setReponse(rs.getString("reponse"));
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