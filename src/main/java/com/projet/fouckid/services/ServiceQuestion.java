package com.projet.fouckid.services;

import com.projet.fouckid.entities.Question;
import com.projet.fouckid.tools.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceQuestion {

    private Connection cnx;

    public ServiceQuestion() {
        cnx = DataSource.getInstance().getConnection();
    }

    // AJOUTER une question
    public void ajouter(Question q) {
        String req = "INSERT INTO question (jeu_id, question_text, option_a, option_b, option_c, bonne_reponse) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, q.getJeuId());
            ps.setString(2, q.getQuestionText());
            ps.setString(3, q.getOptionA());
            ps.setString(4, q.getOptionB());
            ps.setString(5, q.getOptionC());
            ps.setString(6, q.getBonneReponse());

            ps.executeUpdate();
            System.out.println("✅ Question ajoutée avec succès !");

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    // MODIFIER une question
    public void modifier(Question q) {
        String req = "UPDATE question SET jeu_id=?, question_text=?, option_a=?, option_b=?, option_c=?, bonne_reponse=? WHERE id=?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, q.getJeuId());
            ps.setString(2, q.getQuestionText());
            ps.setString(3, q.getOptionA());
            ps.setString(4, q.getOptionB());
            ps.setString(5, q.getOptionC());
            ps.setString(6, q.getBonneReponse());
            ps.setInt(7, q.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Question modifiée avec succès (ID: " + q.getId() + ")");
            } else {
                System.out.println("⚠️ Aucune question trouvée avec l'ID: " + q.getId());
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    // SUPPRIMER une question par son ID
    public void supprimer(int id) {
        String req = "DELETE FROM question WHERE id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Question supprimée avec succès (ID: " + id + ")");
            } else {
                System.out.println("⚠️ Aucune question trouvée avec l'ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // RÉCUPÉRER toutes les questions
    public List<Question> getAll() {
        List<Question> questions = new ArrayList<>();
        String req = "SELECT * FROM question";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("id"),
                        rs.getInt("jeu_id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("bonne_reponse")
                );
                questions.add(q);
            }

            System.out.println("📋 " + questions.size() + " question(s) trouvée(s)");

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération : " + e.getMessage());
        }

        return questions;
    }

    // RÉCUPÉRER une question par son ID
    public Question getById(int id) {
        String req = "SELECT * FROM question WHERE id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Question(
                            rs.getInt("id"),
                            rs.getInt("jeu_id"),
                            rs.getString("question_text"),
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("bonne_reponse")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la recherche par ID : " + e.getMessage());
        }

        return null; // Aucune question trouvée
    }

    // RÉCUPÉRER les questions d'un jeu spécifique
    public List<Question> getByJeuId(int jeuId) {
        List<Question> questions = new ArrayList<>();
        String req = "SELECT * FROM question WHERE jeu_id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, jeuId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Question q = new Question(
                            rs.getInt("id"),
                            rs.getInt("jeu_id"),
                            rs.getString("question_text"),
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("bonne_reponse")
                    );
                    questions.add(q);
                }
            }

            System.out.println("📋 " + questions.size() + " question(s) trouvée(s) pour le jeu ID " + jeuId);

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération par jeu : " + e.getMessage());
        }

        return questions;
    }

    // RECHERCHER des questions par texte (mots-clés)
    public List<Question> rechercherParTexte(String motCle) {
        List<Question> questions = new ArrayList<>();
        String req = "SELECT * FROM question WHERE question_text LIKE ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, "%" + motCle + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Question q = new Question(
                            rs.getInt("id"),
                            rs.getInt("jeu_id"),
                            rs.getString("question_text"),
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("bonne_reponse")
                    );
                    questions.add(q);
                }
            }

            System.out.println("🔍 " + questions.size() + " question(s) trouvée(s) pour la recherche: '" + motCle + "'");

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la recherche : " + e.getMessage());
        }

        return questions;
    }

    // COMPTER le nombre de questions
    public int compterQuestions() {
        String req = "SELECT COUNT(*) AS total FROM question";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du comptage : " + e.getMessage());
        }

        return 0;
    }
}