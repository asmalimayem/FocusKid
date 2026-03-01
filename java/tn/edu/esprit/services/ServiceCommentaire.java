package tn.edu.esprit.services;

import tn.edu.esprit.entities.Commentaire;
import tn.edu.esprit.tools.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommentaire {

    private Connection cnx = DataSource.getInstance().getConnection();

    public void ajouter(Commentaire c) {
        String sql = "INSERT INTO commentaire (carnet_id, date_commentaire, texte_commentaire, type_commentaire) VALUES (?,?,?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, c.getCarnetId());
            ps.setDate(2, c.getDateCommentaire());
            ps.setString(3, c.getTexteCommentaire());
            ps.setString(4, c.getTypeCommentaire());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifier(Commentaire c) {

        String sql = "UPDATE commentaire " +
                     "SET texte_commentaire=?, type_commentaire=?, date_commentaire=? " +
                     "WHERE id=?";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, c.getTexteCommentaire());
            ps.setString(2, c.getTypeCommentaire());
            ps.setDate(3, c.getDateCommentaire());
            ps.setInt(4, c.getId());

            ps.executeUpdate();
            System.out.println("✔ Commentaire modifié");

        } catch (SQLException e) {
            System.out.println("❌ Erreur modification : " + e.getMessage());
        }
    }

    public void supprimer(int id) {
        String sql = "DELETE FROM commentaire WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Commentaire> getAll() {
        List<Commentaire> list = new ArrayList<>();
        String sql = "SELECT * FROM commentaire";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Commentaire c = new Commentaire();
                c.setId(rs.getInt("id"));
                c.setCarnetId(rs.getInt("carnet_id"));
                c.setDateCommentaire(rs.getDate("date_commentaire"));
                c.setTexteCommentaire(rs.getString("texte_commentaire"));
                c.setTypeCommentaire(rs.getString("type_commentaire"));
                list.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    public List<Commentaire> getByCarnetId(int carnetId) {

        List<Commentaire> list = new ArrayList<>();

        String sql = "SELECT * FROM commentaire WHERE carnet_id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, carnetId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Commentaire c = new Commentaire();
                c.setId(rs.getInt("id"));
                c.setCarnetId(rs.getInt("carnet_id"));
                c.setDateCommentaire(rs.getDate("date_commentaire"));
                c.setTypeCommentaire(rs.getString("type_commentaire"));
                c.setTexteCommentaire(rs.getString("texte_commentaire"));

                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}