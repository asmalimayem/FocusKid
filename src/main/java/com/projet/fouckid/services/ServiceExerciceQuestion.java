package com.projet.fouckid.services;


import com.projet.fouckid.entities.ExerciceQuestion;
import com.projet.fouckid.tools.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceExerciceQuestion {

    private Connection cnx;

    public ServiceExerciceQuestion() {
        cnx = DataSource.getInstance().getConnection();
    }

    public void ajouter(ExerciceQuestion eq) {
        String req = "INSERT INTO exercice_question (jeu_id, consigne, reponse_attendue, niveau) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, eq.getJeuId());
            ps.setString(2, eq.getConsigne());
            ps.setString(3, eq.getReponseAttendue());
            ps.setString(4, eq.getNiveau());
            ps.executeUpdate();
            System.out.println("Exercice question ajouté !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifier(ExerciceQuestion eq) {
        String req = "UPDATE exercice_question SET jeu_id=?, consigne=?, reponse_attendue=?, niveau=? WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, eq.getJeuId());
            ps.setString(2, eq.getConsigne());
            ps.setString(3, eq.getReponseAttendue());
            ps.setString(4, eq.getNiveau());
            ps.setInt(5, eq.getId());
            ps.executeUpdate();
            System.out.println("Exercice question modifié avec succès !");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void supprimer(int id) {
        String req = "DELETE FROM exercice_question WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Exercice question supprimé avec succès !");
            } else {
                System.out.println("Aucun exercice question trouvé avec l'id " + id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<ExerciceQuestion> getAll() {
        List<ExerciceQuestion> list = new ArrayList<>();
        String req = "SELECT * FROM exercice_question";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                ExerciceQuestion eq = new ExerciceQuestion(
                        rs.getInt("id"),
                        rs.getInt("jeu_id"),
                        rs.getString("consigne"),
                        rs.getString("reponse_attendue"),
                        rs.getString("niveau")
                );
                list.add(eq);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<ExerciceQuestion> getByJeuId(int jeuId) {
        List<ExerciceQuestion> list = new ArrayList<>();
        String req = "SELECT * FROM exercice_question WHERE jeu_id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, jeuId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExerciceQuestion eq = new ExerciceQuestion(
                            rs.getInt("id"),
                            rs.getInt("jeu_id"),
                            rs.getString("consigne"),
                            rs.getString("reponse_attendue"),
                            rs.getString("niveau")
                    );
                    list.add(eq);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<ExerciceQuestion> getByNiveau(String niveau) {
        List<ExerciceQuestion> list = new ArrayList<>();
        String req = "SELECT * FROM exercice_question WHERE niveau = ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, niveau);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExerciceQuestion eq = new ExerciceQuestion(
                            rs.getInt("id"),
                            rs.getInt("jeu_id"),
                            rs.getString("consigne"),
                            rs.getString("reponse_attendue"),
                            rs.getString("niveau")
                    );
                    list.add(eq);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }
}