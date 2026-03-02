package com.projet.fouckid.services;


import com.projet.fouckid.entities.Score;
import com.projet.fouckid.tools.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceScore {

    private Connection cnx;

    public ServiceScore() {
        cnx = DataSource.getInstance().getConnection();
    }

    public void ajouter(Score s) {
        String req = "INSERT INTO score (utilisateur_id, jeu_id, points) VALUES (?, ?, ?)";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, s.getUtilisateurId());
            ps.setInt(2, s.getJeuId());
            ps.setInt(3, s.getPoints());
            ps.executeUpdate();
            System.out.println("Score ajouté !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifier(Score s) {
        String req = "UPDATE score SET utilisateur_id=?, jeu_id=?, points=? WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, s.getUtilisateurId());
            ps.setInt(2, s.getJeuId());
            ps.setInt(3, s.getPoints());
            ps.setInt(4, s.getId());
            ps.executeUpdate();
            System.out.println("Score modifié avec succès !");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void supprimer(int id) {
        String req = "DELETE FROM score WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Score supprimé avec succès !");
            } else {
                System.out.println("Aucun score trouvé avec l'id " + id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Score> getAll() {
        List<Score> list = new ArrayList<>();
        String req = "SELECT * FROM score";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                Score s = new Score(
                        rs.getInt("id"),
                        rs.getInt("utilisateur_id"),
                        rs.getInt("jeu_id"),
                        rs.getInt("points"),
                        rs.getTimestamp("date_partie")
                );
                list.add(s);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<Score> getByUtilisateurId(int utilisateurId) {
        List<Score> list = new ArrayList<>();
        String req = "SELECT * FROM score WHERE utilisateur_id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Score s = new Score(
                            rs.getInt("id"),
                            rs.getInt("utilisateur_id"),
                            rs.getInt("jeu_id"),
                            rs.getInt("points"),
                            rs.getTimestamp("date_partie")
                    );
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<Score> getByJeuId(int jeuId) {
        List<Score> list = new ArrayList<>();
        String req = "SELECT * FROM score WHERE jeu_id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, jeuId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Score s = new Score(
                            rs.getInt("id"),
                            rs.getInt("utilisateur_id"),
                            rs.getInt("jeu_id"),
                            rs.getInt("points"),
                            rs.getTimestamp("date_partie")
                    );
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }
}
