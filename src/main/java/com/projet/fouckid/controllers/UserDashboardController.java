package com.projet.fouckid.controllers;

import com.projet.fouckid.entities.Jeu;
import com.projet.fouckid.services.ServiceJeu;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UserDashboardController implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private Label welcomeLabel;
    @FXML private Label totalPointsLabel;
    @FXML private FlowPane gamesContainer;
    @FXML private VBox emptyMessage;
    @FXML private Button btnLogout;
    @FXML private Button btnRefresh;

    private ServiceJeu serviceJeu = new ServiceJeu();
    private List<Jeu> allGames;
    private int userId = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ UserDashboard initialisé");

        // Animation de bienvenue
        animateWelcome();

        // Charger les jeux
        loadGames();

        // Charger les points totaux
        loadTotalPoints();

        // Configurer les animations des boutons
        setupAnimations();
    }

    private void animateWelcome() {
        if (welcomeLabel != null) {
            FadeTransition ft = new FadeTransition(Duration.millis(1000), welcomeLabel);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(800), welcomeLabel);
            tt.setFromY(-50);
            tt.setToY(0);
            tt.play();
        }
    }

    private void setupAnimations() {
        if (btnLogout != null) {
            btnLogout.setOnMouseEntered(e -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(150), btnLogout);
                st.setToX(1.1);
                st.setToY(1.1);
                st.play();
            });

            btnLogout.setOnMouseExited(e -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(150), btnLogout);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
            });
        }

        if (btnRefresh != null) {
            btnRefresh.setOnMouseEntered(e -> {
                RotateTransition rt = new RotateTransition(Duration.millis(400), btnRefresh);
                rt.setByAngle(360);
                rt.play();
            });
        }
    }

    private void loadGames() {
        try {
            allGames = serviceJeu.getAll();
            displayGames(allGames);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Erreur de chargement des jeux: " + e.getMessage());
        }
    }

    private void displayGames(List<Jeu> games) {
        if (gamesContainer == null) {
            System.err.println("gamesContainer est null!");
            return;
        }

        gamesContainer.getChildren().clear();

        if (games == null || games.isEmpty()) {
            if (emptyMessage != null) {
                emptyMessage.setVisible(true);
                emptyMessage.setManaged(true);
            }
            return;
        }

        if (emptyMessage != null) {
            emptyMessage.setVisible(false);
            emptyMessage.setManaged(false);
        }

        int delay = 0;
        for (Jeu game : games) {
            VBox gameCard = createGameCard(game);
            gameCard.setOpacity(0);
            gamesContainer.getChildren().add(gameCard);

            PauseTransition pause = new PauseTransition(Duration.millis(delay));
            pause.setOnFinished(e -> {
                FadeTransition ft = new FadeTransition(Duration.millis(400), gameCard);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();

                ScaleTransition st = new ScaleTransition(Duration.millis(400), gameCard);
                st.setFromX(0.8);
                st.setFromY(0.8);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
            });
            pause.play();
            delay += 80;
        }
    }

    private VBox createGameCard(Jeu game) {
        VBox card = new VBox(15);
        card.setPrefWidth(280);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("game-card");

        // Couleur selon le niveau
        String color = getGameColor(game.getNiveau());
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 25; " +
                "-fx-border-radius: 25; -fx-border-color: #ffb32b; -fx-border-width: 3;");

        // Icône selon le type
        Label iconLabel = new Label(game.getType().equals("QUIZ") ? "❓" : "📝");
        iconLabel.setStyle("-fx-font-size: 48px;");

        // Titre du jeu
        Label titleLabel = new Label(game.getTitre());
        titleLabel.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1a1a8b;");

        // Niveau avec étoiles
        Label levelLabel = new Label(getStars(game.getNiveau()));
        levelLabel.setStyle("-fx-font-size: 20px;");

        // Type de jeu
        Label typeLabel = new Label(game.getType());
        typeLabel.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 14px; -fx-text-fill: #ff8c00; -fx-font-weight: bold;");

        // Bouton Jouer
        Button playButton = new Button("🎮 JOUER 🎮");
        playButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-family: 'Comic Sans MS'; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 8 20;");

        playButton.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), playButton);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        playButton.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), playButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        playButton.setOnAction(e -> startGame(game));

        card.getChildren().addAll(iconLabel, titleLabel, levelLabel, typeLabel, playButton);

        // Animation au survol
        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.GOLD);
            glow.setRadius(20);
            card.setEffect(glow);
        });

        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            card.setEffect(null);
        });

        return card;
    }

    private String getGameColor(String niveau) {
        if (niveau == null) return "#FFF9C4";
        switch (niveau) {
            case "FACILE": return "#C8E6C9";
            case "MOYEN": return "#FFF9C4";
            case "DIFFICILE": return "#FFCDD2";
            default: return "#E1F5FE";
        }
    }

    private String getStars(String niveau) {
        if (niveau == null) return "⭐⭐";
        switch (niveau) {
            case "FACILE": return "⭐";
            case "MOYEN": return "⭐⭐";
            case "DIFFICILE": return "⭐⭐⭐";
            default: return "⭐⭐";
        }
    }
    private void startGame(Jeu game) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/projet/fouckid/gameSession.fxml"));
            Parent root = loader.load();

            GameSessionController controller = loader.getController();
            controller.setGame(game);
            controller.setUserId(userId);

            // Récupérer la scène depuis n'importe quel nœud
            Stage stage = null;
            if (rootPane != null && rootPane.getScene() != null) {
                stage = (Stage) rootPane.getScene().getWindow();
            } else if (gamesContainer != null && gamesContainer.getScene() != null) {
                stage = (Stage) gamesContainer.getScene().getWindow();
            }

            if (stage != null) {
                // Stocker la racine de la scène dans une variable finale
                final Scene oldScene = stage.getScene();
                final Stage currentStage = stage;

                FadeTransition ft = new FadeTransition(Duration.millis(300), oldScene.getRoot());
                ft.setFromValue(1);
                ft.setToValue(0);
                ft.setOnFinished(e -> {
                    // Utiliser les variables finales
                    currentStage.setScene(new Scene(root));
                    currentStage.setFullScreen(true);
                    FadeTransition ft2 = new FadeTransition(Duration.millis(300), currentStage.getScene().getRoot());
                    ft2.setFromValue(0);
                    ft2.setToValue(1);
                    ft2.play();
                });
                ft.play();
            } else {
                // Fallback si le stage n'est pas trouvé
                Scene scene = new Scene(root);
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setFullScreen(true);
                newStage.show();

                // Fermer l'ancienne fenêtre
                if (rootPane != null && rootPane.getScene() != null) {
                    Stage oldStage = (Stage) rootPane.getScene().getWindow();
                    if (oldStage != null) {
                        oldStage.close();
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Erreur de chargement du jeu: " + ex.getMessage());
        }
    }

    private void loadTotalPoints() {
        if (totalPointsLabel != null) {
            int totalPoints = (int)(Math.random() * 1000);
            totalPointsLabel.setText(String.valueOf(totalPoints));
            animateCounter(totalPointsLabel, 0, totalPoints);
        }
    }

    private void animateCounter(Label label, int from, int to) {
        Timeline timeline = new Timeline();
        for (int i = from; i <= to; i++) {
            final int value = i;
            KeyFrame kf = new KeyFrame(Duration.millis(i * 5), e -> label.setText(String.valueOf(value)));
            timeline.getKeyFrames().add(kf);
        }
        timeline.play();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("FocusKid");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleRefresh() {
        loadGames();
        if (btnRefresh != null) {
            RotateTransition rt = new RotateTransition(Duration.millis(500), btnRefresh);
            rt.setByAngle(360);
            rt.play();
        }
        showMessage("Liste des jeux actualisée !");
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/projet/fouckid/login.fxml"));
            Stage stage = (Stage) btnLogout.getScene().getWindow();

            FadeTransition ft = new FadeTransition(Duration.millis(500), stage.getScene().getRoot());
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(e -> {
                stage.setScene(new Scene(root));
                FadeTransition ft2 = new FadeTransition(Duration.millis(500), stage.getScene().getRoot());
                ft2.setFromValue(0);
                ft2.setToValue(1);
                ft2.play();
                stage.setFullScreen(false);
            });
            ft.play();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}