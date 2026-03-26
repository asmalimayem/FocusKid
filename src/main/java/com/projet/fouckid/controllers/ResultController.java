package com.projet.fouckid.controllers;

import com.projet.fouckid.entities.Jeu;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ResultController {

    @FXML private Label congratsLabel;
    @FXML private Label gameNameLabel;
    @FXML private Label scoreLabel;
    @FXML private Label percentageLabel;

    private int score;
    private int totalQuestions;
    private Jeu game;

    public void setResult(int score, int totalQuestions, Jeu game) {
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.game = game;

        displayResult();
    }

    private void displayResult() {
        gameNameLabel.setText(game.getTitre() + " - " + game.getNiveau());

        int maxScore = totalQuestions * 30;
        int percentage = (score * 100) / maxScore;

        scoreLabel.setText(String.valueOf(score));
        percentageLabel.setText(percentage + "% de réussite");

        // Message personnalisé selon le score
        if (percentage >= 80) {
            congratsLabel.setText("🌟 EXCELLENT ! 🌟");
            congratsLabel.setStyle("-fx-text-fill: #FFD700;");
        } else if (percentage >= 60) {
            congratsLabel.setText("👍 BRAVO ! 👍");
            congratsLabel.setStyle("-fx-text-fill: #4CAF50;");
        } else if (percentage >= 40) {
            congratsLabel.setText("😊 PAS MAL ! 😊");
            congratsLabel.setStyle("-fx-text-fill: #FF9800;");
        } else {
            congratsLabel.setText("💪 CONTINUE À T'ENTRAÎNER ! 💪");
            congratsLabel.setStyle("-fx-text-fill: #2196F3;");
        }

        // Animations
        animateResult();
    }

    private void animateResult() {
        // Animation du score
        ScaleTransition st = new ScaleTransition(Duration.millis(800), scoreLabel);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);
        st.setInterpolator(Interpolator.EASE_OUT);
        st.play();

        // Animation de la barre de progression
        FadeTransition ft = new FadeTransition(Duration.millis(1000), percentageLabel);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        // Animation des confettis (simulée)
        RotateTransition rt = new RotateTransition(Duration.millis(200), congratsLabel);
        rt.setByAngle(5);
        rt.setAutoReverse(true);
        rt.setCycleCount(6);
        rt.play();
    }

    @FXML
    private void handlePlayAgain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/projet/fouckid/gameSession.fxml"));
            Parent root = loader.load();

            GameSessionController controller = loader.getController();
            controller.setGame(game);
            controller.setUserId(1); // À remplacer par l'ID utilisateur réel

            Stage stage = (Stage) scoreLabel.getScene().getWindow();

            FadeTransition ft = new FadeTransition(Duration.millis(300), stage.getScene().getRoot());
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(e -> {
                stage.setScene(new Scene(root));
                FadeTransition ft2 = new FadeTransition(Duration.millis(300), stage.getScene().getRoot());
                ft2.setFromValue(0);
                ft2.setToValue(1);
                ft2.play();
            });
            ft.play();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleBackToMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/projet/fouckid/userDashboard.fxml"));
            Stage stage = (Stage) scoreLabel.getScene().getWindow();

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
        }
    }

    @FXML
    private void handleViewScores() {
        // Afficher les scores de l'utilisateur
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mes Scores");
        alert.setHeaderText("🏆 Mes meilleurs scores 🏆");
        alert.setContentText("Fonctionnalité à venir dans la prochaine version !\n\n" +
                "Ton dernier score: " + score + " points\n" +
                "Jeu: " + game.getTitre());
        alert.showAndWait();
    }
}