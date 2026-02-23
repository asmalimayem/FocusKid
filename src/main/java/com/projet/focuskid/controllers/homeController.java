package com.projet.focuskid.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class homeController extends BaseController implements Initializable {

    // ===== BOUTONS DE LA SIDEBAR GAUCHE =====
    @FXML private Button btnAdd;        // Bouton plus (créer)
    @FXML private Button btnList;       // Bouton liste (lire)
    @FXML private Button btnRefresh;    // Bouton refresh (modifier)
    @FXML private Button btnDelete;     // Bouton trash (supprimer)

    // ===== BOUTONS DE NAVIGATION DU BAS =====
    @FXML private VBox navCours;
    @FXML private VBox navExercices;
    @FXML private VBox navParents;
    @FXML private VBox navGamification;
    @FXML private VBox navCalme;
    @FXML private VBox navEmotions;

    // ===== BOUTONS D'HUMEUR =====
    @FXML private Button moodHappy;
    @FXML private Button moodCalm;
    @FXML private Button moodExcited;
    @FXML private Button moodTired;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("=== homeController.initialize() ===");

        // Vérification des boutons injectés
        System.out.println("btnAdd = " + (btnAdd != null ? "✅" : "❌"));
        System.out.println("btnList = " + (btnList != null ? "✅" : "❌"));
        System.out.println("btnRefresh = " + (btnRefresh != null ? "✅" : "❌"));
        System.out.println("btnDelete = " + (btnDelete != null ? "✅" : "❌"));

        // Appeler initialize() de BaseController
        super.initialize();

        // Configurer les autres boutons
        setupBottomNavigation();
        setupMoodButtons();
    }

    /**
     * Configure la navigation du bas
     */
    private void setupBottomNavigation() {
        if (navCours != null) {
            navCours.setOnMouseClicked(event -> {
                System.out.println("📚 Navigation vers Cours");
                showInfoAlert("Information", "Module Cours - À venir !");
            });
        }

        if (navExercices != null) {
            navExercices.setOnMouseClicked(event -> {
                System.out.println("✏️ Navigation vers Exercices");
                showInfoAlert("Information", "Module Exercices - À venir !");
            });
        }

        if (navParents != null) {
            navParents.setOnMouseClicked(event -> {
                System.out.println("👪 Navigation vers Espace Parents");
                showInfoAlert("Information", "Espace Parents - À venir !");
            });
        }

        if (navGamification != null) {
            navGamification.setOnMouseClicked(event -> {
                System.out.println("🎮 Navigation vers Gamification");
                showInfoAlert("Information", "Module Gamification - À venir !");
            });
        }

        if (navCalme != null) {
            navCalme.setOnMouseClicked(event -> {
                System.out.println("🧘 Navigation vers Session Calme");
                showInfoAlert("Information", "Module Session Calme - À venir !");
            });
        }

        if (navEmotions != null) {
            navEmotions.setOnMouseClicked(event -> {
                System.out.println("😊 Navigation vers Émotions");
                showInfoAlert("Information", "Module Émotions - À venir !");
            });
        }
    }

    /**
     * Configure les boutons d'humeur
     */
    private void setupMoodButtons() {
        if (moodHappy != null) {
            moodHappy.setOnAction(event -> {
                System.out.println("😄 Humeur : Heureux");
                showMoodMessage("Tu es heureux aujourd'hui ! 🌞");
            });
        }

        if (moodCalm != null) {
            moodCalm.setOnAction(event -> {
                System.out.println("😌 Humeur : Calme");
                showMoodMessage("Tu es calme, c'est parfait ! 🕊️");
            });
        }

        if (moodExcited != null) {
            moodExcited.setOnAction(event -> {
                System.out.println("🤩 Humeur : Excité");
                showMoodMessage("Tu es plein d'énergie ! ⚡");
            });
        }

        if (moodTired != null) {
            moodTired.setOnAction(event -> {
                System.out.println("😴 Humeur : Fatigué");
                showMoodMessage("Une petite pause s'impose ? 🌙");
            });
        }
    }

    private void showMoodMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Humeur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    protected void showMessage(String message, boolean success) {
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(success ? "Succès" : "Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}