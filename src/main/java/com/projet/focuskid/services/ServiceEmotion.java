package services;

import entities.Emotion;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmotion implements IService<Emotion> {

    private Connection connection;

    public ServiceEmotion() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Emotion emotion) throws SQLException {

        String req = "INSERT INTO emotion(nom, icone) " +
                "VALUES ('" + emotion.getNom() + "','" + emotion.getIcone() + "')";
        Statement st = connection.createStatement();
        st.executeUpdate(req);

    }

    @Override
    public void modifier(Emotion emotion) throws SQLException {

        String req = "UPDATE emotion SET nom=?, icone=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, emotion.getNom());
        ps.setString(2, emotion.getIcone());
        ps.setInt(3, emotion.getId());
        ps.executeUpdate();

        System.out.println("Emotion modifiée");
    }

    @Override
    public void supprimer(Emotion emotion) throws SQLException {

        String req = "DELETE FROM emotion WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, emotion.getId());
        ps.executeUpdate();

        System.out.println("Emotion supprimée");
    }

    @Override
    public List<Emotion> recuperer() throws SQLException {

        List<Emotion> emotions = new ArrayList<>();
        String req = "SELECT * FROM emotion";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            int idE = rs.getInt("id");
            String nomE = rs.getString("nom");
            String iconeE = rs.getString("icone");

            Emotion emotion = new Emotion(idE, nomE, iconeE);
            emotions.add(emotion);
        }

        return emotions;
    }
}
