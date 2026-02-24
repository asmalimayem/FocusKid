package com.projet.focuskid.controllers;

import com.projet.focuskid.entities.Scenario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.projet.focuskid.services.ServiceScenario;

import java.sql.SQLException;

public class SupprimerScenario {

    @FXML
    private TextField txtid;

    @FXML
    void supprimerScenario(ActionEvent event) {

        int id = Integer.parseInt(txtid.getText());

        Scenario scenario = new Scenario();
        scenario.setId(id);

        ServiceScenario ss = new ServiceScenario();

        try {
            ss.supprimer(scenario);
            System.out.println("Scenario supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}