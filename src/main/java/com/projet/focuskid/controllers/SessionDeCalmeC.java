package com.projet.focuskid.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer;
import javafx.animation.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class SessionDeCalmeC implements Initializable {

    // FXML Injections pour les boutons de la sidebar
    @FXML private Button btnRespiration;
    @FXML private Button btnColoriage;
    @FXML private Button btnMusique;
    @FXML private Button btnHistoire;

    // FXML Injections pour les vues
    @FXML private VBox respirationView;
    @FXML private VBox musiqueView;
    @FXML private VBox histoireView;
    @FXML private VBox coloriageView;
    @FXML private StackPane contentArea;

    // Navigation bottom
    @FXML private VBox navCours, navExercices, navParents, navGamification, navCalme, navEmotions;

    // ========== COMPOSANTS VUE RESPIRATION ==========
    @FXML private Circle breathingCircle;
    @FXML private Label breathingInstruction;
    @FXML private Label timerLabel;
    @FXML private Button btnStartBreathing;
    @FXML private Button btnPauseBreathing;
    @FXML private Slider breathingSpeedSlider;

    // ========== COMPOSANTS VUE MUSIQUE ==========
    @FXML private ComboBox<String> musicGenreCombo;
    @FXML private ListView<String> musicPlaylist;
    @FXML private Button btnPlayMusic;
    @FXML private Button btnPauseMusic;
    @FXML private Button btnStopMusic;
    @FXML private Slider musicVolumeSlider;
    @FXML private Label currentMusicLabel;
    @FXML private MediaView mediaView;

    // ========== COMPOSANTS VUE HISTOIRE ==========
    @FXML private Label storyTitle;
    @FXML private TextArea storyContent;
    @FXML private ImageView storyImage;
    @FXML private Button btnPrevStory;
    @FXML private Button btnNextStory;
    @FXML private Button btnReadStory;
    @FXML private Slider storyProgressSlider;
    @FXML private ComboBox<String> storyCategoryCombo;

    // ========== COMPOSANTS VUE COLORIAGE ==========
    @FXML private Canvas coloringCanvas;
    @FXML private ColorPicker colorPicker;
    @FXML private Slider brushSizeSlider;
    @FXML private Button btnClearCanvas;
    @FXML private Button btnSaveDrawing;
    @FXML private ComboBox<String> coloringTemplateCombo;
    @FXML private Label drawingTooltip;

    // Variables pour les fonctionnalités
    private Timeline breathingTimeline;
    private int breathCount = 0;
    private final int MAX_BREATHS = 10;
    private GraphicsContext graphicsContext;
    private double lastX, lastY;
    private MediaPlayer mediaPlayer;
    private int currentStoryIndex = 0;
    private Story[] stories;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupRespirationView();
        setupMusiqueView();
        setupHistoireView();
        setupColoriageView();
        setupNavigation();
        Platform.runLater(() -> {
            Stage stage = (Stage) breathingCircle.getScene().getWindow();
            stage.setMaximized(true);
        });
        // Afficher la vue respiration par défaut
        showRespirationView();
    }

    // ========== HANDLERS POUR LES BOUTONS DE LA SIDEBAR ==========

    @FXML
    private void handleRespiration(ActionEvent event) {
        showRespirationView();
        // Animation pour feedback visuel
        animateButton(btnRespiration);
    }

    @FXML
    private void handleColoriage(ActionEvent event) {
        showColoriageView();
        animateButton(btnColoriage);
    }

    @FXML
    private void handleMusique(ActionEvent event) {
        showMusiqueView();
        animateButton(btnMusique);
    }

    @FXML
    private void handleHistoire(ActionEvent event) {
        showHistoireView();
        animateButton(btnHistoire);
    }

    // ========== MÉTHODES D'AFFICHAGE DES VUES ==========

    private void showRespirationView() {
        respirationView.setVisible(true);
        respirationView.setManaged(true);
        musiqueView.setVisible(false);
        musiqueView.setManaged(false);
        histoireView.setVisible(false);
        histoireView.setManaged(false);
        coloriageView.setVisible(false);
        coloriageView.setManaged(false);

        // Mettre à jour la navigation
        updateActiveNav("respiration");
    }

    private void showMusiqueView() {
        respirationView.setVisible(false);
        respirationView.setManaged(false);
        musiqueView.setVisible(true);
        musiqueView.setManaged(true);
        histoireView.setVisible(false);
        histoireView.setManaged(false);
        coloriageView.setVisible(false);
        coloriageView.setManaged(false);

        updateActiveNav("musique");
    }

    private void showHistoireView() {
        respirationView.setVisible(false);
        respirationView.setManaged(false);
        musiqueView.setVisible(false);
        musiqueView.setManaged(false);
        histoireView.setVisible(true);
        histoireView.setManaged(true);
        coloriageView.setVisible(false);
        coloriageView.setManaged(false);

        updateActiveNav("histoire");
    }

    private void showColoriageView() {
        respirationView.setVisible(false);
        respirationView.setManaged(false);
        musiqueView.setVisible(false);
        musiqueView.setManaged(false);
        histoireView.setVisible(false);
        histoireView.setManaged(false);
        coloriageView.setVisible(true);
        coloriageView.setManaged(true);

        updateActiveNav("coloriage");
    }

    // ========== MÉTHODES UTILITAIRES ==========

    private void animateButton(Button button) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
        st.setToX(1.1);
        st.setToY(1.1);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    private void updateActiveNav(String activeView) {
        // Retirer la classe active de tous les items de navigation
        navCours.getStyleClass().remove("active");
        navExercices.getStyleClass().remove("active");
        navParents.getStyleClass().remove("active");
        navGamification.getStyleClass().remove("active");
        navCalme.getStyleClass().remove("active");
        navEmotions.getStyleClass().remove("active");

        // Ajouter la classe active à l'item "Session De Calme"
        navCalme.getStyleClass().add("active");
    }

    // ========== CONFIGURATION DES VUES ==========

    private void setupRespirationView() {
        btnStartBreathing.setOnAction(e -> startBreathingExercise());
        btnPauseBreathing.setOnAction(e -> pauseBreathingExercise());

        breathingSpeedSlider.setMin(2);
        breathingSpeedSlider.setMax(8);
        breathingSpeedSlider.setValue(4);

        breathingInstruction.setText("Prêt à commencer ?");
    }

    private void setupMusiqueView() {
        musicGenreCombo.setItems(FXCollections.observableArrayList(
                "Musique calme", "Sons de nature", "Classique", "Méditation"
        ));

        musicPlaylist.setItems(FXCollections.observableArrayList(
                "meditation", "nature", "calme", "classique"
        ));

        musicPlaylist.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        loadMusic(newVal);
                    }
                }
        );

        btnPlayMusic.setOnAction(e -> playMusic());
        btnPauseMusic.setOnAction(e -> pauseMusic());
        btnStopMusic.setOnAction(e -> stopMusic());

        musicVolumeSlider.setValue(50);
        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newVal.doubleValue() / 100);
            }
        });
    }

    private void setupHistoireView() {
        initializeStories();

        storyCategoryCombo.setItems(FXCollections.observableArrayList(
                "Toutes", "Animaux", "Amis"
        ));

        btnPrevStory.setOnAction(e -> navigateStory(-1));
        btnNextStory.setOnAction(e -> navigateStory(1));
        btnReadStory.setOnAction(e -> readCurrentStory());

        storyProgressSlider.setMin(0);
        storyProgressSlider.setMax(stories.length - 1);
        storyProgressSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentStoryIndex = newVal.intValue();
            displayCurrentStory();
        });

        displayCurrentStory();
    }

    private void setupColoriageView() {
        graphicsContext = coloringCanvas.getGraphicsContext2D();
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(2);

        coloringCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            lastX = e.getX();
            lastY = e.getY();
            drawingTooltip.setText("Dessine ! 🎨");
        });

        coloringCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            double x = e.getX();
            double y = e.getY();

            graphicsContext.setStroke(colorPicker.getValue());
            graphicsContext.setLineWidth(brushSizeSlider.getValue());
            graphicsContext.strokeLine(lastX, lastY, x, y);

            lastX = x;
            lastY = y;
        });

        coloringCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            drawingTooltip.setText("Continue ou choisis une couleur !");
        });

        btnClearCanvas.setOnAction(e -> {
            graphicsContext.clearRect(0, 0, coloringCanvas.getWidth(), coloringCanvas.getHeight());
        });

        btnSaveDrawing.setOnAction(e -> saveDrawing());

        coloringTemplateCombo.setItems(FXCollections.observableArrayList(
                "Papillon", "Fleur", "Poisson", "Arc-en-ciel", "Nuage"
        ));

        coloringTemplateCombo.setOnAction(e -> loadTemplate(coloringTemplateCombo.getValue()));

        coloringCanvas.setWidth(800);
        coloringCanvas.setHeight(500);
    }

    private void setupNavigation() {
        // Vous pouvez ajouter des handlers pour la navigation du bas si nécessaire
        navCalme.getStyleClass().add("active");
    }

    // ========== MÉTHODES RESPIRATION ==========

    private void startBreathingExercise() {
        if (breathingTimeline != null) {
            breathingTimeline.stop();
        }

        breathCount = 0;
        double speed = breathingSpeedSlider.getValue();

        breathingTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> animateBreathIn()),
                new KeyFrame(Duration.seconds(speed), e -> animateBreathOut()),
                new KeyFrame(Duration.seconds(speed * 2), e -> {
                    breathCount++;
                    timerLabel.setText(breathCount + "/" + MAX_BREATHS);
                    if (breathCount < MAX_BREATHS) {
                        animateBreathIn();
                    } else {
                        stopBreathingExercise();
                    }
                })
        );
        breathingTimeline.setCycleCount(Timeline.INDEFINITE);
        breathingTimeline.play();
    }

    private void animateBreathIn() {
        ScaleTransition st = new ScaleTransition(Duration.seconds(breathingSpeedSlider.getValue()), breathingCircle);
        st.setToX(1.5);
        st.setToY(1.5);
        st.play();
        breathingInstruction.setText("Inspire... 🌬️");
    }

    private void animateBreathOut() {
        ScaleTransition st = new ScaleTransition(Duration.seconds(breathingSpeedSlider.getValue()), breathingCircle);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
        breathingInstruction.setText("Expire... 💨");
    }

    private void pauseBreathingExercise() {
        if (breathingTimeline != null) {
            breathingTimeline.pause();
            breathingInstruction.setText("Exercice en pause");
        }
    }

    private void stopBreathingExercise() {
        if (breathingTimeline != null) {
            breathingTimeline.stop();
            breathingCircle.setScaleX(1.0);
            breathingCircle.setScaleY(1.0);
            breathingInstruction.setText("Bravo ! Exercice terminé ! 🌟");
            timerLabel.setText("");
        }
    }

    // ========== MÉTHODES MUSIQUE ==========

    private void loadMusic(String musicName) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            // À remplacer par vos fichiers réels
            String musicFile = getClass().getResource("/com/projet/focuskid/nature.mp3").toExternalForm();
            Media media = new Media(musicFile);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            currentMusicLabel.setText("Lecture: " + musicName);
        } catch (Exception e) {
            currentMusicLabel.setText("Musique non disponible");
            showAlert("Information", "La musique n'est pas encore disponible dans cette version.");
        }
    }

    private void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    // ========== MÉTHODES HISTOIRE ==========

    private void initializeStories() {
        stories = new Story[] {
                new Story(
                        "Le chat et les souris",
                         "il était une fois dans une petite ville un vieux couple qui vivait avec un chat nommé Milo mais le chat n'était pas le seul animal dans la maison une famille de souris y vivait egalement dans un petit trou du mer chaque jour les souris traversaient la maison pour chercher de la nourriture en prenant garde de ne pas se faire attraper attention milo arrive derriere toi . chaque fois que les soris sortaient de leu abri millau leur chasse.",
                         "/com/projet/focuskid/souris.png",
                        "/com/projet/focuskid/souris.mp3",
                        "Animaux"
                ),
                new Story(
                        "Les Trois Petits Cochons", "Il était une fois trois petits cochons  qui vivaient avec leur maman dans une petite maison. Un jour, la maman appela ses trois fils pour leur dire quelque chose d'important. Je voudrais que vous partiez d'ici et construisiez votre maison, mais prenez garde qu'elle soit bien solide pour que le grand méchant loup ne puisse entrer et vous mangez. ",
                        "/com/projet/focuskid/cochons.png",
                        "/com/projet/focuskid/cochons.mp3",
                        "Amis"
                ),

        };
    }

    private void navigateStory(int direction) {
        currentStoryIndex = (currentStoryIndex + direction + stories.length) % stories.length;
        storyProgressSlider.setValue(currentStoryIndex);
        displayCurrentStory();
    }

    private void displayCurrentStory() {
        Story story = stories[currentStoryIndex];
        storyTitle.setText(story.title);
        storyContent.setText(story.content);

        // Charger l'image si disponible
        try {
            Image image = new Image(getClass().getResourceAsStream(story.imagePath));
            storyImage.setImage(image);
        } catch (Exception e) {
            // Image par défaut
        }
    }

    private void readCurrentStory() {
        showAlert("Information", "La lecture audio sera disponible dans la prochaine version !");
    }

    // ========== MÉTHODES COLORIAGE ==========

    private void loadTemplate(String template) {
        graphicsContext.clearRect(0, 0, coloringCanvas.getWidth(), coloringCanvas.getHeight());
        graphicsContext.setStroke(Color.LIGHTGRAY);
        graphicsContext.setLineWidth(1);

        switch(template) {
            case "Papillon":
                graphicsContext.strokeOval(300, 150, 200, 200);
                graphicsContext.strokeOval(150, 200, 150, 150);
                graphicsContext.strokeOval(500, 200, 150, 150);
                break;
            case "Fleur":
                for (int i = 0; i < 8; i++) {
                    double angle = i * Math.PI / 4;
                    double x = 400 + 150 * Math.cos(angle);
                    double y = 250 + 150 * Math.sin(angle);
                    graphicsContext.strokeOval(x-50, y-50, 100, 100);
                }
                graphicsContext.strokeOval(350, 200, 100, 100);
                break;
            case "Poisson":
                graphicsContext.strokeOval(300, 200, 200, 100);
                graphicsContext.strokeLine(500, 250, 550, 200);
                graphicsContext.strokeLine(500, 250, 550, 300);
                break;
            case "Arc-en-ciel":
                double y = 200;
                for (int i = 0; i < 7; i++) {
                    graphicsContext.strokeOval(200, y, 400, 100);
                    y += 20;
                }
                break;
            case "Nuage":
                graphicsContext.strokeOval(250, 200, 150, 100);
                graphicsContext.strokeOval(350, 180, 180, 120);
                graphicsContext.strokeOval(480, 200, 150, 100);
                break;
        }

        drawingTooltip.setText("Template " + template + " chargé ! Colorie-le !");
    }

    private void saveDrawing() {
        // Implémenter la sauvegarde
        drawingTooltip.setText("Dessin sauvegardé ! ✨");

        // Animation de confirmation
        ScaleTransition st = new ScaleTransition(Duration.millis(200), btnSaveDrawing);
        st.setToX(1.1);
        st.setToY(1.1);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    // ========== MÉTHODES UTILITAIRES ==========

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ========== CLASSE INTERNE POUR LES HISTOIRES ==========

    private class Story {
        String title;
        String content;
        String imagePath;
        String audioPath;
        String category;

        Story(String title, String content, String imagePath, String audioPath, String category) {
            this.title = title;
            this.content = content;
            this.imagePath = imagePath;
            this.audioPath = audioPath;
            this.category = category;
        }
    }
}