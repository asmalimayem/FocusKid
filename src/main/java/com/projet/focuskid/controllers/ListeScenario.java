package controllers;

import entities.Scenario;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import services.ServiceScenario;

import java.sql.SQLException;
import java.util.List;

public class ListeScenario {

    @FXML
    private TextArea textarea;

    @FXML
    public void initialize() {

        ServiceScenario ss = new ServiceScenario();

        try {
            List<Scenario> scenarios = ss.recuperer();
            textarea.setText(scenarios.toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}