package controllers;

import entities.Scenario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import services.ServiceScenario;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterScenario {

    @FXML
    private TextField txttitre;

    @FXML
    private TextField txtdescription;

    @FXML
    private TextField txtanimationUrl;

    @FXML
    private TextField txtemotionId;

    @FXML
    void addScenario(ActionEvent event) {

        String titre = txttitre.getText();
        String description = txtdescription.getText();
        String animationUrl = txtanimationUrl.getText();
        int emotionId = Integer.parseInt(txtemotionId.getText());

        ServiceScenario ss = new ServiceScenario();
        Scenario scenario = new Scenario(titre, description, animationUrl, emotionId);

        try {
            ss.ajouter(scenario);

            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/AfficherScenario.fxml"));
            Parent root = loader.load();

            AfficherScenario as = loader.getController();
            as.setRtitre(titre);
            as.setRdescription(description);
            as.setRanimationUrl(animationUrl);
            as.setRemotionId(String.valueOf(emotionId));
            as.setrList(ss.recuperer().toString());

            txttitre.getScene().setRoot(root);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}