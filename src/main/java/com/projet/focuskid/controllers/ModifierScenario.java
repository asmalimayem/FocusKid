package controllers;

import entities.Scenario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import services.ServiceScenario;

import java.sql.SQLException;

public class ModifierScenario {

    @FXML
    private TextField txtid;

    @FXML
    private TextField txttitre;

    @FXML
    private TextField txtdescription;

    @FXML
    private TextField txtanimationUrl;

    @FXML
    private TextField txtemotionId;

    @FXML
    void modifierScenario(ActionEvent event) {

        int id = Integer.parseInt(txtid.getText());
        String titre = txttitre.getText();
        String description = txtdescription.getText();
        String animationUrl = txtanimationUrl.getText();
        int emotionId = Integer.parseInt(txtemotionId.getText());

        Scenario scenario = new Scenario(id, titre, description, animationUrl, emotionId);
        ServiceScenario ss = new ServiceScenario();

        try {
            ss.modifier(scenario);
            System.out.println("Scenario modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}