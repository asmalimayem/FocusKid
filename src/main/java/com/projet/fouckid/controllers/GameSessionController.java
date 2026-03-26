package com.projet.fouckid.controllers;

import com.projet.fouckid.entities.Jeu;
import com.projet.fouckid.entities.Question;
import com.projet.fouckid.entities.Score;
import com.projet.fouckid.services.ServiceJeu;
import com.projet.fouckid.services.ServiceQuestion;
import com.projet.fouckid.services.ServiceScore;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameSessionController implements Initializable {

    @FXML private Label gameTitleLabel;
    @FXML private Label scoreLabel;
    @FXML private Label questionNumberLabel;
    @FXML private Label questionLabel;
    @FXML private Button optionAButton;
    @FXML private Button optionBButton;
    @FXML private Button optionCButton;
    @FXML private Label feedbackLabel;
    @FXML private Label timerLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Button backButton;

    private ServiceJeu serviceJeu = new ServiceJeu();
    private ServiceQuestion serviceQuestion = new ServiceQuestion();
    private ServiceScore serviceScore = new ServiceScore();

    private Jeu currentGame;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int userId;
    private int timeRemaining = 30;
    private Timeline timer;
    private Timeline feedbackTimer;
    private int answeredQuestions = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Animation des boutons
        setupButtonAnimation(optionAButton);
        setupButtonAnimation(optionBButton);
        setupButtonAnimation(optionCButton);
        setupButtonAnimation(backButton);

        // Désactiver les boutons au début
        disableButtons(true);
    }

    public void setGame(Jeu game) {
        this.currentGame = game;
        loadQuestions();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private void loadQuestions() {
        try {
            questions = serviceQuestion.getByJeuId(currentGame.getId());
            if (questions != null && !questions.isEmpty()) {
                gameTitleLabel.setText("🎮 " + currentGame.getTitre() + " - " + currentGame.getNiveau());
                showNextQuestion();
            } else {
                showNoQuestions();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de chargement des questions");
        }
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText(q.getQuestionText());
            optionAButton.setText("A. " + q.getOptionA());
            optionBButton.setText("B. " + q.getOptionB());
            optionCButton.setText("C. " + q.getOptionC());

            // Mettre à jour le numéro de question
            questionNumberLabel.setText((currentQuestionIndex + 1) + "/" + questions.size());

            // Mettre à jour la barre de progression
            progressBar.setProgress((double) currentQuestionIndex / questions.size());

            // Réactiver les boutons
            disableButtons(false);

            // Démarrer le timer
            startTimer();

            // Animation d'entrée
            animateQuestionEntry();

        } else {
            endGame();
        }
    }

    private void animateQuestionEntry() {
        questionLabel.setScaleX(0.8);
        questionLabel.setScaleY(0.8);
        questionLabel.setOpacity(0);

        ScaleTransition st = new ScaleTransition(Duration.millis(400), questionLabel);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();

        FadeTransition ft = new FadeTransition(Duration.millis(400), questionLabel);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }
        timeRemaining = 30;
        updateTimerDisplay();

        timer = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    if (timeRemaining > 0) {
                        timeRemaining--;
                        updateTimerDisplay();

                        // Animation quand le temps est bas
                        if (timeRemaining <= 5) {
                            timerLabel.setStyle("-fx-text-fill: #f44336;");
                            ScaleTransition st = new ScaleTransition(Duration.millis(200), timerLabel);
                            st.setToX(1.2);
                            st.setToY(1.2);
                            st.setAutoReverse(true);
                            st.setCycleCount(2);
                            st.play();
                        }
                    }

                    if (timeRemaining <= 0) {
                        timer.stop();
                        handleTimeout();
                    }
                })
        );
        timer.setCycleCount(30);
        timer.play();
    }

    private void updateTimerDisplay() {
        timerLabel.setText("⏱️ " + timeRemaining + "s");
    }

    private void handleTimeout() {
        disableButtons(true);
        showFeedback("⏰ Temps écoulé ! La bonne réponse était : " +
                getCorrectAnswerLetter(), false);

        // Passer à la question suivante après 2 secondes
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            currentQuestionIndex++;
            showNextQuestion();
        });
        pause.play();
    }

    private String getCorrectAnswerLetter() {
        Question q = questions.get(currentQuestionIndex);
        return q.getBonneReponse();
    }

    private void checkAnswer(String selectedLetter) {
        if (timer != null) {
            timer.stop();
        }

        Question q = questions.get(currentQuestionIndex);
        boolean isCorrect = selectedLetter.equals(q.getBonneReponse());

        if (isCorrect) {
            // Calculer les points selon la difficulté
            int points = calculatePoints();
            score += points;
            updateScore();

            showFeedback("✅ BRAVO ! +" + points + " points !", true);

            // Animation de succès
            animateSuccess();
        } else {
            showFeedback("❌ Dommage ! La bonne réponse était " +
                    q.getBonneReponse() + ".", false);
            animateError();
        }

        disableButtons(true);

        // Passer à la question suivante
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> {
            currentQuestionIndex++;
            showNextQuestion();
        });
        pause.play();
    }

    private int calculatePoints() {
        int basePoints = 10;
        switch (currentGame.getNiveau()) {
            case "FACILE": return basePoints + (30 - timeRemaining) / 3;
            case "MOYEN": return basePoints * 2 + (30 - timeRemaining) / 2;
            case "DIFFICILE": return basePoints * 3 + (30 - timeRemaining);
            default: return basePoints;
        }
    }

    private void updateScore() {
        scoreLabel.setText(String.valueOf(score));

        // Animation du score
        ScaleTransition st = new ScaleTransition(Duration.millis(200), scoreLabel);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    private void animateSuccess() {
        // Animation de succès sur le feedback
        feedbackLabel.setStyle("-fx-text-fill: #4CAF50;");

        // Faire briller la bonne réponse
        DropShadow glow = new DropShadow();
        glow.setColor(Color.GREEN);
        glow.setRadius(15);

        switch (questions.get(currentQuestionIndex).getBonneReponse()) {
            case "A": optionAButton.setEffect(glow); break;
            case "B": optionBButton.setEffect(glow); break;
            case "C": optionCButton.setEffect(glow); break;
        }

        // Enlever l'effet après 1 seconde
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            optionAButton.setEffect(null);
            optionBButton.setEffect(null);
            optionCButton.setEffect(null);
        });
        pause.play();
    }

    private void animateError() {
        feedbackLabel.setStyle("-fx-text-fill: #f44336;");

        // Secouer la mauvaise réponse
        Timeline shake = new Timeline(
                new KeyFrame(Duration.ZERO, e -> getCurrentButton().setTranslateX(0)),
                new KeyFrame(Duration.millis(50), e -> getCurrentButton().setTranslateX(-5)),
                new KeyFrame(Duration.millis(100), e -> getCurrentButton().setTranslateX(5)),
                new KeyFrame(Duration.millis(150), e -> getCurrentButton().setTranslateX(-5)),
                new KeyFrame(Duration.millis(200), e -> getCurrentButton().setTranslateX(5)),
                new KeyFrame(Duration.millis(250), e -> getCurrentButton().setTranslateX(0))
        );
        shake.play();
    }

    private Button getCurrentButton() {
        String selected = getSelectedButtonLetter();
        if (selected.equals("A")) return optionAButton;
        if (selected.equals("B")) return optionBButton;
        return optionCButton;
    }

    private String getSelectedButtonLetter() {
        // À implémenter selon le bouton cliqué
        return "";
    }

    private void showFeedback(String message, boolean isSuccess) {
        feedbackLabel.setText(message);
        feedbackLabel.setVisible(true);

        FadeTransition ft = new FadeTransition(Duration.millis(300), feedbackLabel);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        if (feedbackTimer != null) {
            feedbackTimer.stop();
        }

        feedbackTimer = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> {
                    FadeTransition ft2 = new FadeTransition(Duration.millis(500), feedbackLabel);
                    ft2.setFromValue(1);
                    ft2.setToValue(0);
                    ft2.play();
                })
        );
        feedbackTimer.play();
    }

    private void disableButtons(boolean disable) {
        optionAButton.setDisable(disable);
        optionBButton.setDisable(disable);
        optionCButton.setDisable(disable);
    }

    private void setupButtonAnimation(Button button) {
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    private void endGame() {
        if (timer != null) {
            timer.stop();
        }

        // Sauvegarder le score
        try {
            Score scoreObj = new Score(userId, currentGame.getId(), score);
            serviceScore.ajouter(scoreObj);

            showResult();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de sauvegarde du score");
        }
    }

    private void showResult() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/projet/fouckid/result.fxml"));
            Parent root = loader.load();

            ResultController controller = loader.getController();
            controller.setResult(score, questions.size(), currentGame);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showNoQuestions() {
        showError("Aucune question disponible pour ce jeu !");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        handleBackToMenu();
    }

    @FXML
    private void handleOptionA() {
        checkAnswer("A");
    }

    @FXML
    private void handleOptionB() {
        checkAnswer("B");
    }

    @FXML
    private void handleOptionC() {
        checkAnswer("C");
    }

    @FXML
    private void handleBackToMenu() {
        if (timer != null) {
            timer.stop();
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/projet/fouckid/userDashboard.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();

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
}