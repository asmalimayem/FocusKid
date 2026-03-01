package tn.edu.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ParentDashboardController {

    @FXML
    private void ouvrirCarnet() {
        ouvrirFenetre("/view/AjouterCarnet.fxml", "Gestion Carnet");
    }

    private void ouvrirFenetre(String path, String title) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setWidth(900);
            stage.setHeight(700);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
