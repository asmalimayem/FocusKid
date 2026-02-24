package services;

import entities.Scenario;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceScenario implements IService<Scenario> {

    private Connection connection;

    public ServiceScenario() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Scenario scenario) throws SQLException {

        String req = "INSERT INTO scenario(titre, description, animationUrl, emotionId) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, scenario.getTitre());
        ps.setString(2, scenario.getDescription());
        ps.setString(3, scenario.getAnimationUrl());
        ps.setInt(4, scenario.getEmotionId());

        ps.executeUpdate();
        System.out.println("Scenario ajouté");
    }

    @Override
    public void modifier(Scenario scenario) throws SQLException {

        String req = "UPDATE scenario SET titre=?, description=?, animationUrl=?, emotionId=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, scenario.getTitre());
        ps.setString(2, scenario.getDescription());
        ps.setString(3, scenario.getAnimationUrl());
        ps.setInt(4, scenario.getEmotionId());
        ps.setInt(5, scenario.getId());

        ps.executeUpdate();
        System.out.println("Scenario modifié");
    }

    @Override
    public void supprimer(Scenario scenario) throws SQLException {

        String req = "DELETE FROM scenario WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, scenario.getId());

        ps.executeUpdate();
        System.out.println("Scenario supprimé");
    }

    @Override
    public List<Scenario> recuperer() throws SQLException {

        List<Scenario> scenarios = new ArrayList<>();
        String req = "SELECT * FROM scenario";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            int idS = rs.getInt("id");
            String titreS = rs.getString("titre");
            String descS = rs.getString("description");
            String animS = rs.getString("animationUrl");
            int emotionIdS = rs.getInt("emotionId");

            Scenario scenario = new Scenario(idS, titreS, descS, animS, emotionIdS);
            scenarios.add(scenario);
        }

        return scenarios;
    }
}
