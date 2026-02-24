package tn.edu.esprit.services;

import tn.edu.esprit.entities.CarnetEducatif;
import tn.edu.esprit.tools.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCarnetEducatif {

    private Connection cnx = DataSource.getInstance().getConnection();

    /* ================= AJOUT ================= */
    public void ajouter(CarnetEducatif c) {

        String sql = "INSERT INTO carnet_educatif (" +
                "date_etude, heure_debut, heure_fin, duree_totale, lieu, matiere, " +
                "type_activite, niveau_difficulte, niveau_concentration, niveau_agitation, " +
                "nombre_interruptions, temps_avant_perte_concentration, travaille_seul, " +
                "demande_aide, niveau_autonomie, travail_termine, difficultes, points_positifs" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setDate(1, c.getDateEtude());
            ps.setTime(2, c.getHeureDebut());
            ps.setTime(3, c.getHeureFin());
            ps.setInt(4, c.getDureeTotale());
            ps.setString(5, c.getLieu());
            ps.setString(6, c.getMatiere());
            ps.setString(7, c.getTypeActivite());
            ps.setString(8, c.getNiveauDifficulte());
            ps.setInt(9, c.getNiveauConcentration());
            ps.setInt(10, c.getNiveauAgitation());
            ps.setInt(11, c.getNombreInterruptions());
            ps.setInt(12, c.getTempsAvantPerteConcentration());
            ps.setBoolean(13, c.isTravailleSeul());
            ps.setBoolean(14, c.isDemandeAide());
            ps.setInt(15, c.getNiveauAutonomie());
            ps.setBoolean(16, c.isTravailTermine());
            ps.setString(17, c.getDifficultes());
            ps.setString(18, c.getPointsPositifs());

            ps.executeUpdate();
            System.out.println("✔ Carnet ajouté");

        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout : " + e.getMessage());
        }
    }

    /* ================= MODIFIER ================= */
    public void modifier(CarnetEducatif c) {

        String sql = "UPDATE carnet_educatif SET " +
                "matiere=?, type_activite=?, niveau_difficulte=?, difficultes=?, points_positifs=? " +
                "WHERE id=?";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, c.getMatiere());
            ps.setString(2, c.getTypeActivite());
            ps.setString(3, c.getNiveauDifficulte());
            ps.setString(4, c.getDifficultes());
            ps.setString(5, c.getPointsPositifs());
            ps.setInt(6, c.getId());

            ps.executeUpdate();
            System.out.println("✔ Carnet modifié");

        } catch (SQLException e) {
            System.out.println("❌ Erreur modification : " + e.getMessage());
        }
    }

    /* ================= SUPPRIMER ================= */
    public void supprimer(int id) {
        try (PreparedStatement ps =
                     cnx.prepareStatement("DELETE FROM carnet_educatif WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /* ================= GET ALL ================= */
    public List<CarnetEducatif> getAll() {

        List<CarnetEducatif> list = new ArrayList<>();

        String sql =  "SELECT * FROM carnet_educatif";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

        	while (rs.next()) {
        	    CarnetEducatif c = new CarnetEducatif();
        	    c.setId(rs.getInt("id"));
        	    c.setDateEtude(rs.getDate("date_etude"));
        	    c.setHeureDebut(rs.getTime("heure_debut"));
        	    c.setHeureFin(rs.getTime("heure_fin"));
        	    c.setDureeTotale(rs.getInt("duree_totale"));
        	    c.setLieu(rs.getString("lieu"));
        	    c.setMatiere(rs.getString("matiere"));
        	    c.setTypeActivite(rs.getString("type_activite"));
        	    c.setNiveauDifficulte(rs.getString("niveau_difficulte"));
        	    c.setNiveauConcentration(rs.getInt("niveau_concentration"));
        	    c.setNiveauAgitation(rs.getInt("niveau_agitation"));
        	    c.setNombreInterruptions(rs.getInt("nombre_interruptions"));
        	    c.setTempsAvantPerteConcentration(rs.getInt("temps_avant_perte_concentration"));
        	    c.setTravailleSeul(rs.getBoolean("travaille_seul"));
        	    c.setDemandeAide(rs.getBoolean("demande_aide"));
        	    c.setNiveauAutonomie(rs.getInt("niveau_autonomie"));
        	    c.setTravailTermine(rs.getBoolean("travail_termine"));
        	    c.setDifficultes(rs.getString("difficultes"));
        	    c.setPointsPositifs(rs.getString("points_positifs"));
        	    list.add(c);
        	}

        } catch (SQLException e) {
            System.out.println("Erreur getAll : " + e.getMessage());
        }

        return list;
    }
}