package com.projet.focuskid.services;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.projet.focuskid.entities.SessionDeCalme;
import com.projet.focuskid.entities.SessionDeCalme;
import com.projet.focuskid.tools.DataSource;

public class ServiceSessionDeCalme implements IService<SessionDeCalme> {
    Connection cnx;
    public ServiceSessionDeCalme() {
        this.cnx = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(SessionDeCalme t) {
        try {
            String req = "INSERT INTO `sessions_de_calme` (`enfant_id`, `type_activite`, `declencheur`, `duree_prevue`, `duree_reelle`, `feedback_enfant`, `note_parent`) "
                    + "VALUES (" + t.getEnfantId() + ", '" + t.getTypeActivite() + "', '" + t.getDeclencheur1() + "', "
                    + t.getDureePrevue() + ", " + t.getDureeReelle() + ", " + t.getFeedbackEnfant() + ", '" + t.getNoteParent() + "')";

            Statement stm = cnx.createStatement();
            stm.executeUpdate(req);
            System.out.println("Session de calme ajoutée avec succès");
        } catch (SQLException ex) {
            System.out.println("Erreur ajout: " + ex.getMessage());
        }
    }

    @Override
    public void modifier(SessionDeCalme t) {
        try {
            StringBuilder req = new StringBuilder("UPDATE `sessions_de_calme` SET ");

            // Construction dynamique de la requête
            if (t.getTypeActivite() != null) {
                req.append("`type_activite`='").append(t.getTypeActivite()).append("', ");
            }
            if (t.getDeclencheur1() != null) {
                req.append("`declencheur`='").append(t.getDeclencheur1()).append("', ");
            }
            if (t.getDureePrevue() != 0) {
                req.append("`duree_prevue`=").append(t.getDureePrevue()).append(", ");
            }
            if (t.getDureeReelle() != 0) {
                req.append("`duree_reelle`=").append(t.getDureeReelle()).append(", ");
            }
            if (t.getFeedbackEnfant() != 0) {
                req.append("`feedback_enfant`=").append(t.getFeedbackEnfant()).append(", ");
            }
            if (t.getNoteParent() != null) {
                req.append("`note_parent`='").append(t.getNoteParent()).append("', ");
            }

            // Supprimer la dernière virgule et espace
            String reqFinal = req.substring(0, req.length() - 2);

            // Ajouter la condition WHERE
            reqFinal += " WHERE `id`=" + t.getId();

            Statement stm = cnx.createStatement();
            int rowsAffected = stm.executeUpdate(reqFinal);

            if (rowsAffected > 0) {
                System.out.println("Session modifiée avec succès");
            } else {
                System.out.println("Aucune session trouvée avec l'ID: " + t.getId());
            }
        } catch (SQLException ex) {
            System.out.println("Erreur modification: " + ex.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        try {
            String req = "DELETE FROM `sessions_de_calme` WHERE `id` = " + id;
            Statement stm = cnx.createStatement();
            stm.executeUpdate(req);
            System.out.println("Session supprimée avec succès");
        } catch (SQLException ex) {
            System.out.println("Erreur suppression: " + ex.getMessage());
        }
    }

    @Override
    public SessionDeCalme getOne(SessionDeCalme t) {
        // Implémentation basique pour récupérer par ID
        return getAll(null).stream()
                .filter(s -> s.getId() == t.getId())
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<SessionDeCalme> getAll(SessionDeCalme t) {
        String req = "SELECT * FROM `sessions_de_calme`";
        ArrayList<SessionDeCalme> sessions = new ArrayList<>();
        try {
            Statement stm = this.cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                SessionDeCalme s = new SessionDeCalme();
                s.setId(rs.getInt("id"));
                s.setEnfantId(rs.getInt("enfant_id"));
                s.setTypeActivite(rs.getString("type_activite"));
                s.setDeclencheur(rs.getString("declencheur"));
                s.setDureePrevue(rs.getInt("duree_prevue"));
                s.setDureeReelle(rs.getInt("duree_reelle"));
                s.setHorodatage(rs.getTimestamp("horodatage"));
                s.setFeedbackEnfant(rs.getInt("feedback_enfant"));
                s.setNoteParent(rs.getString("note_parent"));

                sessions.add(s);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur getAll: " + ex.getMessage());
        }
        return sessions;
    }
}