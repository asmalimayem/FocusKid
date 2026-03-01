package com.projet.focuskid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("adminDashboard.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("FocusKid");
        stage.setScene(scene);
        stage.show();
    }

    // ✅ CETTE METHODE MANQUAIT
    public static void main(String[] args) {
        launch();
    }
}