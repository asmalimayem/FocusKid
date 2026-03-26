package com.projet.fouckid.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {

    @FXML private Button adminButton;
    @FXML private Button userButton;
    @FXML private Label infoLabel;

    @FXML
    public void initialize() {
        setupButtonAnimation(adminButton);
        setupButtonAnimation(userButton);
    }

    private void setupButtonAnimation(Button button) {
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), button);
            tt.setByY(-10);
            tt.play();
        });

        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), button);
            tt.setByY(10);
            tt.play();
        });
    }

    @FXML
    private void handleAdminLogin() {
        try {
            // Afficher le chemin exact pour déboguer
            String fxmlPath = "/com/projet/fouckid/adminDashboardGamification.fxml";
            System.out.println("Tentative de chargement: " + fxmlPath);

            // Vérifier si le fichier existe
            java.net.URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                System.err.println("Fichier FXML non trouvé: " + fxmlPath);
                showError("Fichier FXML non trouvé: " + fxmlPath);
                return;
            }
            System.out.println("Fichier trouvé: " + resource.getPath());

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            Stage stage = (Stage) adminButton.getScene().getWindow();

            FadeTransition ft = new FadeTransition(Duration.millis(500), stage.getScene().getRoot());
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(e -> {
                stage.setScene(new Scene(root));
                FadeTransition ft2 = new FadeTransition(Duration.millis(500), stage.getScene().getRoot());
                ft2.setFromValue(0);
                ft2.setToValue(1);
                ft2.play();
                stage.setFullScreen(true);
            });
            ft.play();

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Erreur de chargement de l'interface administrateur:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleUserLogin() {
        try {
            String fxmlPath = "/com/projet/fouckid/userDashboard.fxml";
            System.out.println("Tentative de chargement: " + fxmlPath);

            java.net.URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                System.err.println("Fichier FXML non trouvé: " + fxmlPath);
                showError("Fichier FXML non trouvé: " + fxmlPath);
                return;
            }

            Parent root = FXMLLoader.load(resource);
            Stage stage = (Stage) userButton.getScene().getWindow();

            FadeTransition ft = new FadeTransition(Duration.millis(500), stage.getScene().getRoot());
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(e -> {
                stage.setScene(new Scene(root));
                FadeTransition ft2 = new FadeTransition(Duration.millis(500), stage.getScene().getRoot());
                ft2.setFromValue(0);
                ft2.setToValue(1);
                ft2.play();
                stage.setFullScreen(true);
            });
            ft.play();

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Erreur de chargement de l'interface utilisateur:\n" + ex.getMessage());
        }
    }

    private void showError(String message) {
        infoLabel.setText("❌ " + message);
        infoLabel.setStyle("-fx-text-fill: #f44336; -fx-background-color: rgba(244,67,54,0.1); -fx-padding: 10; -fx-background-radius: 10;");

        FadeTransition ft = new FadeTransition(Duration.millis(300), infoLabel);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();

        // Afficher aussi une alerte
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur de chargement");
        alert.setContentText(message);
        alert.showAndWait();
    }
}