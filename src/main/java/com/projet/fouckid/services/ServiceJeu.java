package com.projet.fouckid.services;


import com.projet.fouckid.entities.Jeu;
import com.projet.fouckid.tools.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceJeu {

    private Connection cnx;

    public ServiceJeu() {
        cnx = DataSource.getInstance().getConnection();
    }

    public void ajouter(Jeu j) {
        String req = "INSERT INTO jeu (titre, type, niveau) VALUES (?, ?, ?)";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, j.getTitre());
            ps.setString(2, j.getType());
            ps.setString(3, j.getNiveau());
            ps.executeUpdate();
            System.out.println("Jeu ajouté !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifier(Jeu j) {
        String req = "UPDATE jeu SET titre=?, type=?, niveau=? WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, j.getTitre());
            ps.setString(2, j.getType());
            ps.setString(3, j.getNiveau());
            ps.setInt(4, j.getId());
            ps.executeUpdate();
            System.out.println("Jeu modifié avec succès !");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void supprimer(int id) {
        String req = "DELETE FROM jeu WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Jeu supprimé avec succès !");
            } else {
                System.out.println("Aucun jeu trouvé avec l'id " + id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Jeu> getAll() {
        List<Jeu> list = new ArrayList<>();
        String req = "SELECT * FROM jeu";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                Jeu j = new Jeu(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("type"),
                        rs.getString("niveau")
                );
                list.add(j);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public Jeu getById(int id) {
        String req = "SELECT * FROM jeu WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Jeu(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("type"),
                            rs.getString("niveau")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}