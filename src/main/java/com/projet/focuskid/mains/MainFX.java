package mains;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/AjouterEmotion.fxml")
            );

            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Ajouter Emotion");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            System.out.println("Erreur chargement FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}