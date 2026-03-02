package com.projet.fouckid.controllers;

import com.projet.fouckid.entities.Jeu;
import com.projet.fouckid.entities.Question;
import com.projet.fouckid.entities.Score;
import com.projet.fouckid.services.ServiceJeu;
import com.projet.fouckid.services.ServiceQuestion;
import com.projet.fouckid.services.ServiceScore;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

public class adminDashboardGamificationController implements Initializable {

    // ========== ÉLÉMENTS DE MISE EN PAGE ==========
    @FXML private StackPane contentArea;
    @FXML private BorderPane mainBorderPane;

    // ========== ÉLÉMENTS DE LA SIDEBAR ==========
    @FXML private Button btnEntityJeu;
    @FXML private Button btnEntityQuestion;
    @FXML private Button btnEntityScore;
    @FXML private Label currentEntityLabel;
    @FXML private Button btnAdd;
    @FXML private Button btnList;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;

    // ========== VUES JEU ==========
    @FXML private VBox jeuListView;
    @FXML private VBox jeuAddView;
    @FXML private VBox jeuUpdateView;
    @FXML private VBox jeuDeleteView;

    @FXML private TextField jeuSearchField;
    @FXML private Label totalJeux;
    @FXML private FlowPane jeuxCardsContainer;
    @FXML private VBox jeuEmptyMessage;

    @FXML private TextField jeuTitreField;
    @FXML private ComboBox<String> jeuTypeCombo;
    @FXML private ComboBox<String> jeuNiveauCombo;
    @FXML private Label jeuAddMessageLabel;
    @FXML private Button btnJeuSave;
    @FXML private Button btnJeuReset;

    @FXML private ComboBox<Jeu> jeuSelector;
    @FXML private Label jeuId;
    @FXML private TextField jeuUpdateTitreField;
    @FXML private ComboBox<String> jeuUpdateTypeCombo;
    @FXML private ComboBox<String> jeuUpdateNiveauCombo;
    @FXML private Label jeuUpdateMessageLabel;
    @FXML private Button btnJeuUpdate;
    @FXML private Button btnJeuCancel;

    @FXML private ListView<Jeu> jeuDeleteList;
    @FXML private Label jeuDeleteMessageLabel;
    @FXML private Button btnJeuDeleteSelected;
    @FXML private Button btnJeuRefreshList;

    // ========== VUES QUESTION ==========
    @FXML private VBox questionListView;
    @FXML private VBox questionAddView;
    @FXML private VBox questionUpdateView;
    @FXML private VBox questionDeleteView;

    @FXML private TextField questionSearchField;
    @FXML private Label totalQuestions;
    @FXML private FlowPane questionsCardsContainer;
    @FXML private VBox questionEmptyMessage;

    @FXML private ComboBox<Jeu> questionJeuCombo;
    @FXML private TextField questionTextArea;
    @FXML private TextField questionOptionA;
    @FXML private TextField questionOptionB;
    @FXML private TextField questionOptionC;
    @FXML private ComboBox<String> questionBonneReponseCombo;
    @FXML private Label questionAddMessageLabel;
    @FXML private Button btnQuestionSave;
    @FXML private Button btnQuestionReset;

    @FXML private ComboBox<Question> questionSelector;
    @FXML private Label questionId;
    @FXML private ComboBox<Jeu> questionUpdateJeuCombo;
    @FXML private TextField questionUpdateTextArea;
    @FXML private TextField questionUpdateOptionA;
    @FXML private TextField questionUpdateOptionB;
    @FXML private TextField questionUpdateOptionC;
    @FXML private ComboBox<String> questionUpdateBonneReponseCombo;
    @FXML private Label questionUpdateMessageLabel;
    @FXML private Button btnQuestionUpdate;
    @FXML private Button btnQuestionCancel;

    @FXML private ListView<Question> questionDeleteList;
    @FXML private Label questionDeleteMessageLabel;
    @FXML private Button btnQuestionDeleteSelected;
    @FXML private Button btnQuestionRefreshList;

    // ========== VUES SCORE ==========
    @FXML private VBox scoreListView;
    @FXML private VBox scoreAddView;
    @FXML private VBox scoreUpdateView;
    @FXML private VBox scoreDeleteView;

    @FXML private TextField scoreSearchField;
    @FXML private Label totalScores;
    @FXML private Label avgScore;
    @FXML private FlowPane scoresCardsContainer;
    @FXML private VBox scoreEmptyMessage;

    @FXML private TextField scoreUtilisateurId;
    @FXML private ComboBox<Jeu> scoreJeuCombo;
    @FXML private TextField scorePointsField;
    @FXML private Label scoreAddMessageLabel;
    @FXML private Button btnScoreSave;
    @FXML private Button btnScoreReset;

    @FXML private ComboBox<Score> scoreSelector;
    @FXML private Label scoreId;
    @FXML private TextField scoreUpdateUtilisateurId;
    @FXML private ComboBox<Jeu> scoreUpdateJeuCombo;
    @FXML private TextField scoreUpdatePointsField;
    @FXML private DatePicker scoreUpdateDatePicker;
    @FXML private Label scoreUpdateMessageLabel;
    @FXML private Button btnScoreUpdate;
    @FXML private Button btnScoreCancel;

    @FXML private ListView<Score> scoreDeleteList;
    @FXML private Label scoreDeleteMessageLabel;
    @FXML private Button btnScoreDeleteSelected;
    @FXML private Button btnScoreRefreshList;

    // ========== SERVICES ==========
    private ServiceJeu serviceJeu = new ServiceJeu();
    private ServiceQuestion serviceQuestion = new ServiceQuestion();
    private ServiceScore serviceScore = new ServiceScore();

    // ========== DONNÉES ==========
    private List<Jeu> allJeux;
    private List<Question> allQuestions;
    private List<Score> allScores;
    private Jeu currentJeu;
    private Question currentQuestion;
    private Score currentScore;

    // ========== ENUM POUR LE TYPE D'ENTITÉ ==========
    private enum EntityType { JEU, QUESTION, SCORE }
    private EntityType currentEntity = EntityType.JEU;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ AdminGamificationController initialisé");

        Platform.runLater(() -> {
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
        });

        initializeViews();
        handleSelectJeu(); // Sélectionner Jeu par défaut
        setupAnimations();
        refreshAllData();
    }

    // ========== INITIALISATION ==========

    private void initializeViews() {
        setupJeuViews();
        setupQuestionViews();
        setupScoreViews();

        // Style des bordures
        styleViewBorder(jeuListView, "#9C27B0");
        styleViewBorder(jeuAddView, "#FF9800");
        styleViewBorder(jeuUpdateView, "#2196F3");
        styleViewBorder(jeuDeleteView, "#F44336");

        styleViewBorder(questionListView, "#FF5722");
        styleViewBorder(questionAddView, "#FF9800");
        styleViewBorder(questionUpdateView, "#2196F3");
        styleViewBorder(questionDeleteView, "#F44336");

        styleViewBorder(scoreListView, "#4CAF50");
        styleViewBorder(scoreAddView, "#FF9800");
        styleViewBorder(scoreUpdateView, "#2196F3");
        styleViewBorder(scoreDeleteView, "#F44336");
    }

    private void styleViewBorder(VBox view, String color) {
        if (view != null) {
            view.setStyle(view.getStyle()
                    + "-fx-border-color: " + color + "; -fx-border-width: 6; -fx-border-radius: 40;");
        }
    }

    private void setupAnimations() {
        setupButtonAnimation(btnEntityJeu);
        setupButtonAnimation(btnEntityQuestion);
        setupButtonAnimation(btnEntityScore);
        setupButtonAnimation(btnAdd);
        setupButtonAnimation(btnList);
        setupButtonAnimation(btnUpdate);
        setupButtonAnimation(btnDelete);
        setupButtonAnimation(btnJeuSave);
        setupButtonAnimation(btnJeuUpdate);
        setupButtonAnimation(btnJeuReset);
        setupButtonAnimation(btnJeuCancel);
        setupButtonAnimation(btnJeuDeleteSelected);
        setupButtonAnimation(btnQuestionSave);
        setupButtonAnimation(btnQuestionUpdate);
        setupButtonAnimation(btnQuestionReset);
        setupButtonAnimation(btnQuestionCancel);
        setupButtonAnimation(btnQuestionDeleteSelected);
        setupButtonAnimation(btnScoreSave);
        setupButtonAnimation(btnScoreUpdate);
        setupButtonAnimation(btnScoreReset);
        setupButtonAnimation(btnScoreCancel);
        setupButtonAnimation(btnScoreDeleteSelected);
    }

    private void setupButtonAnimation(Button button) {
        if (button == null) return;
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.2);
            st.setToY(1.2);
            st.play();
            DropShadow glow = new DropShadow();
            glow.setColor(Color.GOLD);
            glow.setRadius(20);
            button.setEffect(glow);
        });
        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            button.setEffect(null);
        });
        button.setOnMousePressed(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(80), button);
            st.setToX(0.92);
            st.setToY(0.92);
            st.play();
            button.setStyle("-fx-background-color: #FFD700; -fx-background-radius: 15;");
        });
        button.setOnMouseReleased(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(80), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            button.setStyle("");
        });
    }

    // ========== CONFIGURATION DES VUES ==========

    private void setupJeuViews() {
        if (jeuSearchField != null) {
            jeuSearchField.textProperty().addListener((obs, oldVal, newVal) -> filterJeux(newVal));
        }

        if (jeuTypeCombo != null && jeuTypeCombo.getItems().isEmpty()) {
            jeuTypeCombo.getItems().addAll("QUIZ", "EXERCICE");
        }
        if (jeuNiveauCombo != null && jeuNiveauCombo.getItems().isEmpty()) {
            jeuNiveauCombo.getItems().addAll("FACILE", "MOYEN", "DIFFICILE");
        }

        if (jeuUpdateTypeCombo != null && jeuUpdateTypeCombo.getItems().isEmpty()) {
            jeuUpdateTypeCombo.getItems().addAll("QUIZ", "EXERCICE");
        }
        if (jeuUpdateNiveauCombo != null && jeuUpdateNiveauCombo.getItems().isEmpty()) {
            jeuUpdateNiveauCombo.getItems().addAll("FACILE", "MOYEN", "DIFFICILE");
        }

        if (jeuSelector != null) {
            jeuSelector.setConverter(new StringConverter<Jeu>() {
                @Override public String toString(Jeu j) {
                    if (j == null) return "";
                    return "🎮 " + j.getTitre() + " (" + j.getType() + ")";
                }
                @Override public Jeu fromString(String str) { return null; }
            });
            jeuSelector.setOnAction(event -> {
                currentJeu = jeuSelector.getValue();
                if (currentJeu != null) loadJeuData();
            });
        }

        if (jeuDeleteList != null) {
            jeuDeleteList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            jeuDeleteList.setCellFactory(param -> new ListCell<Jeu>() {
                @Override
                protected void updateItem(Jeu item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText("🎮 " + item.getTitre() + " | " + item.getType() + " | " + item.getNiveau());
                    }
                }
            });
        }
    }

    private void setupQuestionViews() {
        if (questionSearchField != null) {
            questionSearchField.textProperty().addListener((obs, oldVal, newVal) -> filterQuestions(newVal));
        }

        if (questionBonneReponseCombo != null && questionBonneReponseCombo.getItems().isEmpty()) {
            questionBonneReponseCombo.getItems().addAll("A", "B", "C");
        }

        if (questionUpdateBonneReponseCombo != null && questionUpdateBonneReponseCombo.getItems().isEmpty()) {
            questionUpdateBonneReponseCombo.getItems().addAll("A", "B", "C");
        }

        if (questionSelector != null) {
            questionSelector.setConverter(new StringConverter<Question>() {
                @Override public String toString(Question q) {
                    if (q == null) return "";
                    String text = q.getQuestionText();
                    if (text.length() > 30) text = text.substring(0, 27) + "...";
                    return "❓ " + text;
                }
                @Override public Question fromString(String str) { return null; }
            });
            questionSelector.setOnAction(event -> {
                currentQuestion = questionSelector.getValue();
                if (currentQuestion != null) loadQuestionData();
            });
        }

        if (questionDeleteList != null) {
            questionDeleteList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            questionDeleteList.setCellFactory(param -> new ListCell<Question>() {
                @Override
                protected void updateItem(Question item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String text = item.getQuestionText();
                        if (text.length() > 40) text = text.substring(0, 37) + "...";
                        setText("❓ ID " + item.getId() + " | " + text);
                    }
                }
            });
        }
    }

    private void setupScoreViews() {
        if (scoreSearchField != null) {
            scoreSearchField.textProperty().addListener((obs, oldVal, newVal) -> filterScores(newVal));
        }

        if (scoreSelector != null) {
            scoreSelector.setConverter(new StringConverter<Score>() {
                @Override public String toString(Score s) {
                    if (s == null) return "";
                    return "🏆 Score #" + s.getId() + " | User: " + s.getUtilisateurId() + " | Points: " + s.getPoints();
                }
                @Override public Score fromString(String str) { return null; }
            });
            scoreSelector.setOnAction(event -> {
                currentScore = scoreSelector.getValue();
                if (currentScore != null) loadScoreData();
            });
        }

        if (scoreDeleteList != null) {
            scoreDeleteList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            scoreDeleteList.setCellFactory(param -> new ListCell<Score>() {
                @Override
                protected void updateItem(Score item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText("🏆 ID " + item.getId() + " | User: " + item.getUtilisateurId() +
                                " | Points: " + item.getPoints() + " | " + item.getDatePartie().toLocalDateTime().toLocalDate());
                    }
                }
            });
        }
    }

    private void refreshJeuCombos() {
        try {
            List<Jeu> jeux = serviceJeu.getAll();
            if (questionJeuCombo != null) {
                questionJeuCombo.getItems().setAll(jeux);
                questionJeuCombo.setConverter(new StringConverter<Jeu>() {
                    @Override public String toString(Jeu j) {
                        return j == null ? "" : j.getTitre() + " (" + j.getType() + ")";
                    }
                    @Override public Jeu fromString(String str) { return null; }
                });
            }
            if (questionUpdateJeuCombo != null) {
                questionUpdateJeuCombo.getItems().setAll(jeux);
                questionUpdateJeuCombo.setConverter(new StringConverter<Jeu>() {
                    @Override public String toString(Jeu j) {
                        return j == null ? "" : j.getTitre() + " (" + j.getType() + ")";
                    }
                    @Override public Jeu fromString(String str) { return null; }
                });
            }
            if (scoreJeuCombo != null) {
                scoreJeuCombo.getItems().setAll(jeux);
                scoreJeuCombo.setConverter(new StringConverter<Jeu>() {
                    @Override public String toString(Jeu j) {
                        return j == null ? "" : j.getTitre() + " (" + j.getType() + ")";
                    }
                    @Override public Jeu fromString(String str) { return null; }
                });
            }
            if (scoreUpdateJeuCombo != null) {
                scoreUpdateJeuCombo.getItems().setAll(jeux);
                scoreUpdateJeuCombo.setConverter(new StringConverter<Jeu>() {
                    @Override public String toString(Jeu j) {
                        return j == null ? "" : j.getTitre() + " (" + j.getType() + ")";
                    }
                    @Override public Jeu fromString(String str) { return null; }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== GESTION DE LA SÉLECTION D'ENTITÉ ==========

    @FXML
    private void handleSelectJeu() {
        currentEntity = EntityType.JEU;
        currentEntityLabel.setText("Jeux 🎮");
        highlightEntityButton(btnEntityJeu);
        showJeuListView();
        refreshJeuCombos();
    }

    @FXML
    private void handleSelectQuestion() {
        currentEntity = EntityType.QUESTION;
        currentEntityLabel.setText("Questions ❓");
        highlightEntityButton(btnEntityQuestion);
        showQuestionListView();
        refreshJeuCombos();
    }

    @FXML
    private void handleSelectScore() {
        currentEntity = EntityType.SCORE;
        currentEntityLabel.setText("Scores 🏆");
        highlightEntityButton(btnEntityScore);
        showScoreListView();
        refreshJeuCombos();
    }

    private void highlightEntityButton(Button selectedButton) {
        btnEntityJeu.setStyle("");
        btnEntityQuestion.setStyle("");
        btnEntityScore.setStyle("");
        selectedButton.setStyle("-fx-background-color: rgba(255,215,0,0.3); -fx-background-radius: 20; -fx-padding: 5;");
    }

    // ========== GESTION DES ACTIONS CRUD ==========

    @FXML
    private void handleShowAdd() {
        switch (currentEntity) {
            case JEU: showJeuAddView(); break;
            case QUESTION: showQuestionAddView(); break;
            case SCORE: showScoreAddView(); break;
        }
    }

    @FXML
    private void handleShowList() {
        switch (currentEntity) {
            case JEU: showJeuListView(); break;
            case QUESTION: showQuestionListView(); break;
            case SCORE: showScoreListView(); break;
        }
    }

    @FXML
    private void handleShowUpdate() {
        switch (currentEntity) {
            case JEU: showJeuUpdateView(); break;
            case QUESTION: showQuestionUpdateView(); break;
            case SCORE: showScoreUpdateView(); break;
        }
    }

    @FXML
    private void handleShowDelete() {
        switch (currentEntity) {
            case JEU: showJeuDeleteView(); break;
            case QUESTION: showQuestionDeleteView(); break;
            case SCORE: showScoreDeleteView(); break;
        }
    }

    // ========== GESTION DES VUES ==========

    private void hideAllViews() {
        jeuListView.setVisible(false); jeuListView.setManaged(false);
        jeuAddView.setVisible(false); jeuAddView.setManaged(false);
        jeuUpdateView.setVisible(false); jeuUpdateView.setManaged(false);
        jeuDeleteView.setVisible(false); jeuDeleteView.setManaged(false);

        questionListView.setVisible(false); questionListView.setManaged(false);
        questionAddView.setVisible(false); questionAddView.setManaged(false);
        questionUpdateView.setVisible(false); questionUpdateView.setManaged(false);
        questionDeleteView.setVisible(false); questionDeleteView.setManaged(false);

        scoreListView.setVisible(false); scoreListView.setManaged(false);
        scoreAddView.setVisible(false); scoreAddView.setManaged(false);
        scoreUpdateView.setVisible(false); scoreUpdateView.setManaged(false);
        scoreDeleteView.setVisible(false); scoreDeleteView.setManaged(false);
    }

    private void showJeuListView() {
        hideAllViews();
        jeuListView.setVisible(true);
        jeuListView.setManaged(true);
        refreshJeuxList();
    }

    private void showJeuAddView() {
        hideAllViews();
        jeuAddView.setVisible(true);
        jeuAddView.setManaged(true);
        resetJeuAddForm();
    }

    private void showJeuUpdateView() {
        hideAllViews();
        jeuUpdateView.setVisible(true);
        jeuUpdateView.setManaged(true);
        loadJeuxForSelector();
        resetJeuUpdateForm();
    }

    private void showJeuDeleteView() {
        hideAllViews();
        jeuDeleteView.setVisible(true);
        jeuDeleteView.setManaged(true);
        loadJeuxForDelete();
    }

    private void showQuestionListView() {
        hideAllViews();
        questionListView.setVisible(true);
        questionListView.setManaged(true);
        refreshQuestionsList();
    }

    private void showQuestionAddView() {
        hideAllViews();
        questionAddView.setVisible(true);
        questionAddView.setManaged(true);
        resetQuestionAddForm();
    }

    private void showQuestionUpdateView() {
        hideAllViews();
        questionUpdateView.setVisible(true);
        questionUpdateView.setManaged(true);
        loadQuestionsForSelector();
        resetQuestionUpdateForm();
    }

    private void showQuestionDeleteView() {
        hideAllViews();
        questionDeleteView.setVisible(true);
        questionDeleteView.setManaged(true);
        loadQuestionsForDelete();
    }

    private void showScoreListView() {
        hideAllViews();
        scoreListView.setVisible(true);
        scoreListView.setManaged(true);
        refreshScoresList();
    }

    private void showScoreAddView() {
        hideAllViews();
        scoreAddView.setVisible(true);
        scoreAddView.setManaged(true);
        resetScoreAddForm();
    }

    private void showScoreUpdateView() {
        hideAllViews();
        scoreUpdateView.setVisible(true);
        scoreUpdateView.setManaged(true);
        loadScoresForSelector();
        resetScoreUpdateForm();
    }

    private void showScoreDeleteView() {
        hideAllViews();
        scoreDeleteView.setVisible(true);
        scoreDeleteView.setManaged(true);
        loadScoresForDelete();
    }

    // ========== GESTION DES DONNÉES ==========

    private void refreshAllData() {
        try {
            allJeux = serviceJeu.getAll();
            allQuestions = serviceQuestion.getAll();
            allScores = serviceScore.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== MÉTHODES JEU ==========

    private void refreshJeuxList() {
        refreshAllData();
        displayJeux(allJeux);
        updateJeuStats();
    }

    private void displayJeux(List<Jeu> jeux) {
        if (jeuxCardsContainer == null) return;
        jeuxCardsContainer.getChildren().clear();

        if (jeux == null || jeux.isEmpty()) {
            if (jeuEmptyMessage != null) {
                jeuEmptyMessage.setVisible(true);
                jeuEmptyMessage.setManaged(true);
            }
            return;
        }

        if (jeuEmptyMessage != null) {
            jeuEmptyMessage.setVisible(false);
            jeuEmptyMessage.setManaged(false);
        }

        int delay = 0;
        for (Jeu jeu : jeux) {
            VBox card = createJeuCard(jeu);
            card.setOpacity(0);
            jeuxCardsContainer.getChildren().add(card);

            PauseTransition pause = new PauseTransition(Duration.millis(delay));
            pause.setOnFinished(e -> {
                FadeTransition ft = new FadeTransition(Duration.millis(350), card);
                ft.setFromValue(0); ft.setToValue(1); ft.play();
                ScaleTransition st = new ScaleTransition(Duration.millis(350), card);
                st.setFromX(0.85); st.setFromY(0.85);
                st.setToX(1.0); st.setToY(1.0); st.play();
            });
            pause.play();
            delay += 60;
        }
    }

    private VBox createJeuCard(Jeu jeu) {
        VBox card = new VBox(10);
        card.getStyleClass().add("session-card");
        card.setPrefWidth(300);
        card.setPadding(new Insets(18));

        String color = getJeuColor(jeu.getType());
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 25; "
                + "-fx-border-radius: 25; -fx-border-color: rgba(255,255,255,0.7); -fx-border-width: 3;");

        Label titreLabel = new Label("🎮 " + jeu.getTitre());
        titreLabel.getStyleClass().add("card-type");

        Label typeLabel = new Label("📋 " + jeu.getType());
        typeLabel.getStyleClass().add("card-detail");

        Label niveauLabel = new Label("⭐ " + jeu.getNiveau());
        niveauLabel.getStyleClass().add("card-detail");

        card.getChildren().addAll(titreLabel, typeLabel, niveauLabel);

        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(180), card);
            st.setToX(1.06); st.setToY(1.06); st.play();
            DropShadow glow = new DropShadow();
            glow.setColor(Color.GOLD); glow.setRadius(18);
            card.setEffect(glow);
        });
        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(180), card);
            st.setToX(1.0); st.setToY(1.0); st.play();
            card.setEffect(null);
        });

        return card;
    }

    private String getJeuColor(String type) {
        if (type == null) return "#FFEBEE";
        switch (type) {
            case "QUIZ": return "#E1F5FE";
            case "EXERCICE": return "#FFF3E0";
            default: return "#FCE4EC";
        }
    }

    private void filterJeux(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            displayJeux(allJeux);
            return;
        }
        List<Jeu> filtered = allJeux.stream()
                .filter(j -> j.getTitre() != null &&
                        j.getTitre().toLowerCase().contains(searchText.toLowerCase()))
                .toList();
        displayJeux(filtered);
    }

    private void updateJeuStats() {
        if (totalJeux == null || allJeux == null) return;
        totalJeux.setText(String.valueOf(allJeux.size()));
    }

    @FXML
    private void handleJeuSave() {
        try {
            if (jeuTitreField.getText() == null || jeuTitreField.getText().trim().isEmpty()) {
                showFeedback(jeuAddMessageLabel, "❓ Donne un titre au jeu !", false);
                animateError(jeuTitreField);
                return;
            }
            if (jeuTypeCombo.getValue() == null) {
                showFeedback(jeuAddMessageLabel, "❓ Choisis un type !", false);
                animateError(jeuTypeCombo);
                return;
            }
            if (jeuNiveauCombo.getValue() == null) {
                showFeedback(jeuAddMessageLabel, "❓ Choisis un niveau !", false);
                animateError(jeuNiveauCombo);
                return;
            }

            Jeu jeu = new Jeu(
                    jeuTitreField.getText().trim(),
                    jeuTypeCombo.getValue(),
                    jeuNiveauCombo.getValue()
            );

            serviceJeu.ajouter(jeu);
            animateSuccess(btnJeuSave);
            showFeedback(jeuAddMessageLabel, "✅ Super ! Jeu ajouté ! 🎮", true);
            handleJeuReset();
            refreshJeuxList();

        } catch (Exception e) {
            e.printStackTrace();
            showFeedback(jeuAddMessageLabel, "❌ Oups ! " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleJeuReset() {
        resetJeuAddForm();
        showFeedback(jeuAddMessageLabel, "✨ Formulaire réinitialisé !", true);
    }

    private void resetJeuAddForm() {
        if (jeuTitreField != null) jeuTitreField.clear();
        if (jeuTypeCombo != null) jeuTypeCombo.setValue(null);
        if (jeuNiveauCombo != null) jeuNiveauCombo.setValue(null);
        if (jeuAddMessageLabel != null) {
            jeuAddMessageLabel.setVisible(false);
            jeuAddMessageLabel.setManaged(false);
        }
    }

    private void loadJeuxForSelector() {
        try {
            List<Jeu> jeux = serviceJeu.getAll();
            if (jeuSelector != null) jeuSelector.getItems().setAll(jeux);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadJeuData() {
        if (currentJeu == null) return;
        if (jeuId != null) jeuId.setText(String.valueOf(currentJeu.getId()));
        if (jeuUpdateTitreField != null) jeuUpdateTitreField.setText(currentJeu.getTitre());
        if (jeuUpdateTypeCombo != null) jeuUpdateTypeCombo.setValue(currentJeu.getType());
        if (jeuUpdateNiveauCombo != null) jeuUpdateNiveauCombo.setValue(currentJeu.getNiveau());
    }

    private void resetJeuUpdateForm() {
        if (jeuUpdateTitreField != null) jeuUpdateTitreField.clear();
        if (jeuUpdateTypeCombo != null) jeuUpdateTypeCombo.setValue(null);
        if (jeuUpdateNiveauCombo != null) jeuUpdateNiveauCombo.setValue(null);
        if (jeuId != null) jeuId.setText("");
        currentJeu = null;
    }

    @FXML
    private void handleJeuUpdate() {
        try {
            if (currentJeu == null) {
                showFeedback(jeuUpdateMessageLabel, "❓ Choisis un jeu à modifier !", false);
                animateError(jeuSelector);
                return;
            }

            currentJeu.setTitre(jeuUpdateTitreField.getText());
            currentJeu.setType(jeuUpdateTypeCombo.getValue());
            currentJeu.setNiveau(jeuUpdateNiveauCombo.getValue());

            serviceJeu.modifier(currentJeu);
            animateSuccess(btnJeuUpdate);
            showFeedback(jeuUpdateMessageLabel, "✅ Jeu modifié avec succès ! ✨", true);
            loadJeuxForSelector();
            refreshJeuxList();
            resetJeuUpdateForm();

        } catch (Exception e) {
            e.printStackTrace();
            showFeedback(jeuUpdateMessageLabel, "❌ Erreur : " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleJeuCancel() {
        resetJeuUpdateForm();
        showFeedback(jeuUpdateMessageLabel, "🔄 Formulaire réinitialisé", true);
    }

    private void loadJeuxForDelete() {
        try {
            List<Jeu> jeux = serviceJeu.getAll();
            if (jeuDeleteList != null) {
                jeuDeleteList.setItems(FXCollections.observableArrayList(jeux));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleJeuDeleteSelected() {
        if (jeuDeleteList == null) return;
        ObservableList<Jeu> selected = jeuDeleteList.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()) {
            showFeedback(jeuDeleteMessageLabel, "❓ Sélectionne au moins un jeu !", false);
            animateError(jeuDeleteList);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer " + selected.size() + " jeu(x) ?");
        confirm.setContentText("⚠️ Cette action est irréversible !");

        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                int ok = 0;
                for (Jeu j : selected) {
                    try {
                        serviceJeu.supprimer(j.getId());
                        ok++;
                    } catch (Exception ex) {
                        System.err.println("Erreur suppression jeu " + j.getId());
                    }
                }
                loadJeuxForDelete();
                refreshJeuxList();
                showFeedback(jeuDeleteMessageLabel, "✅ " + ok + " jeu(x) supprimé(s) ! 🗑️", ok == selected.size());
            }
        });
    }

    @FXML
    private void handleRefreshJeu() {
        loadJeuxForDelete();
        refreshJeuxList();
        showMessage("🔄 Liste actualisée !", true);
    }

    // ========== MÉTHODES QUESTION ==========

    private void refreshQuestionsList() {
        refreshAllData();
        displayQuestions(allQuestions);
        updateQuestionStats();
        refreshJeuCombos();
    }

    private void displayQuestions(List<Question> questions) {
        if (questionsCardsContainer == null) return;
        questionsCardsContainer.getChildren().clear();

        if (questions == null || questions.isEmpty()) {
            if (questionEmptyMessage != null) {
                questionEmptyMessage.setVisible(true);
                questionEmptyMessage.setManaged(true);
            }
            return;
        }

        if (questionEmptyMessage != null) {
            questionEmptyMessage.setVisible(false);
            questionEmptyMessage.setManaged(false);
        }

        int delay = 0;
        for (Question question : questions) {
            VBox card = createQuestionCard(question);
            card.setOpacity(0);
            questionsCardsContainer.getChildren().add(card);

            PauseTransition pause = new PauseTransition(Duration.millis(delay));
            pause.setOnFinished(e -> {
                FadeTransition ft = new FadeTransition(Duration.millis(350), card);
                ft.setFromValue(0); ft.setToValue(1); ft.play();
                ScaleTransition st = new ScaleTransition(Duration.millis(350), card);
                st.setFromX(0.85); st.setFromY(0.85);
                st.setToX(1.0); st.setToY(1.0); st.play();
            });
            pause.play();
            delay += 60;
        }
    }

    private VBox createQuestionCard(Question question) {
        VBox card = new VBox(10);
        card.getStyleClass().add("session-card");
        card.setPrefWidth(300);
        card.setPadding(new Insets(18));

        String color = "#FFF8E1";
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 25; "
                + "-fx-border-radius: 25; -fx-border-color: rgba(255,255,255,0.7); -fx-border-width: 3;");

        String questionText = question.getQuestionText();
        if (questionText.length() > 40) {
            questionText = questionText.substring(0, 40) + "...";
        }

        Label questionLabel = new Label("❓ " + questionText);
        questionLabel.getStyleClass().add("card-type");

        Label optionsLabel = new Label("A: " + question.getOptionA() + " | B: " + question.getOptionB() + " | C: " + question.getOptionC());
        optionsLabel.getStyleClass().add("card-detail");

        Label reponseLabel = new Label("✅ Bonne réponse: " + question.getBonneReponse());
        reponseLabel.getStyleClass().add("card-detail");

        card.getChildren().addAll(questionLabel, optionsLabel, reponseLabel);

        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(180), card);
            st.setToX(1.06); st.setToY(1.06); st.play();
            DropShadow glow = new DropShadow();
            glow.setColor(Color.GOLD); glow.setRadius(18);
            card.setEffect(glow);
        });
        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(180), card);
            st.setToX(1.0); st.setToY(1.0); st.play();
            card.setEffect(null);
        });

        return card;
    }

    private void filterQuestions(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            displayQuestions(allQuestions);
            return;
        }
        List<Question> filtered = allQuestions.stream()
                .filter(q -> q.getQuestionText() != null &&
                        q.getQuestionText().toLowerCase().contains(searchText.toLowerCase()))
                .toList();
        displayQuestions(filtered);
    }

    private void updateQuestionStats() {
        if (totalQuestions == null || allQuestions == null) return;
        totalQuestions.setText(String.valueOf(allQuestions.size()));
    }

    @FXML
    private void handleQuestionSave() {
        try {
            if (questionJeuCombo.getValue() == null) {
                showFeedback(questionAddMessageLabel, "❓ Choisis un jeu !", false);
                animateError(questionJeuCombo);
                return;
            }
            if (questionTextArea.getText() == null || questionTextArea.getText().trim().isEmpty()) {
                showFeedback(questionAddMessageLabel, "❓ Écris ta question !", false);
                animateError(questionTextArea);
                return;
            }
            if (questionOptionA.getText() == null || questionOptionA.getText().trim().isEmpty()) {
                showFeedback(questionAddMessageLabel, "❓ Remplis l'option A !", false);
                animateError(questionOptionA);
                return;
            }
            if (questionOptionB.getText() == null || questionOptionB.getText().trim().isEmpty()) {
                showFeedback(questionAddMessageLabel, "❓ Remplis l'option B !", false);
                animateError(questionOptionB);
                return;
            }
            if (questionOptionC.getText() == null || questionOptionC.getText().trim().isEmpty()) {
                showFeedback(questionAddMessageLabel, "❓ Remplis l'option C !", false);
                animateError(questionOptionC);
                return;
            }
            if (questionBonneReponseCombo.getValue() == null) {
                showFeedback(questionAddMessageLabel, "❓ Choisis la bonne réponse !", false);
                animateError(questionBonneReponseCombo);
                return;
            }

            Question question = new Question(
                    questionJeuCombo.getValue().getId(),
                    questionTextArea.getText().trim(),
                    questionOptionA.getText().trim(),
                    questionOptionB.getText().trim(),
                    questionOptionC.getText().trim(),
                    questionBonneReponseCombo.getValue()
            );

            serviceQuestion.ajouter(question);
            animateSuccess(btnQuestionSave);
            showFeedback(questionAddMessageLabel, "✅ Super ! Question ajoutée ! ❓", true);
            handleQuestionReset();
            refreshQuestionsList();

        } catch (Exception e) {
            e.printStackTrace();
            showFeedback(questionAddMessageLabel, "❌ Oups ! " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleQuestionReset() {
        resetQuestionAddForm();
        showFeedback(questionAddMessageLabel, "✨ Formulaire réinitialisé !", true);
    }

    private void resetQuestionAddForm() {
        if (questionJeuCombo != null) questionJeuCombo.setValue(null);
        if (questionTextArea != null) questionTextArea.clear();
        if (questionOptionA != null) questionOptionA.clear();
        if (questionOptionB != null) questionOptionB.clear();
        if (questionOptionC != null) questionOptionC.clear();
        if (questionBonneReponseCombo != null) questionBonneReponseCombo.setValue(null);
        if (questionAddMessageLabel != null) {
            questionAddMessageLabel.setVisible(false);
            questionAddMessageLabel.setManaged(false);
        }
    }

    private void loadQuestionsForSelector() {
        try {
            List<Question> questions = serviceQuestion.getAll();
            if (questionSelector != null) questionSelector.getItems().setAll(questions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadQuestionData() {
        if (currentQuestion == null) return;
        if (questionId != null) questionId.setText(String.valueOf(currentQuestion.getId()));
        if (questionUpdateTextArea != null) questionUpdateTextArea.setText(currentQuestion.getQuestionText());
        if (questionUpdateOptionA != null) questionUpdateOptionA.setText(currentQuestion.getOptionA());
        if (questionUpdateOptionB != null) questionUpdateOptionB.setText(currentQuestion.getOptionB());
        if (questionUpdateOptionC != null) questionUpdateOptionC.setText(currentQuestion.getOptionC());
        if (questionUpdateBonneReponseCombo != null) questionUpdateBonneReponseCombo.setValue(currentQuestion.getBonneReponse());

        if (questionUpdateJeuCombo != null && currentQuestion.getJeuId() > 0) {
            try {
                Jeu jeu = serviceJeu.getById(currentQuestion.getJeuId());
                if (jeu != null) {
                    questionUpdateJeuCombo.setValue(jeu);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetQuestionUpdateForm() {
        if (questionUpdateJeuCombo != null) questionUpdateJeuCombo.setValue(null);
        if (questionUpdateTextArea != null) questionUpdateTextArea.clear();
        if (questionUpdateOptionA != null) questionUpdateOptionA.clear();
        if (questionUpdateOptionB != null) questionUpdateOptionB.clear();
        if (questionUpdateOptionC != null) questionUpdateOptionC.clear();
        if (questionUpdateBonneReponseCombo != null) questionUpdateBonneReponseCombo.setValue(null);
        if (questionId != null) questionId.setText("");
        currentQuestion = null;
    }

    @FXML
    private void handleQuestionUpdate() {
        try {
            if (currentQuestion == null) {
                showFeedback(questionUpdateMessageLabel, "❓ Choisis une question à modifier !", false);
                animateError(questionSelector);
                return;
            }

            currentQuestion.setQuestionText(questionUpdateTextArea.getText());
            currentQuestion.setOptionA(questionUpdateOptionA.getText());
            currentQuestion.setOptionB(questionUpdateOptionB.getText());
            currentQuestion.setOptionC(questionUpdateOptionC.getText());
            currentQuestion.setBonneReponse(questionUpdateBonneReponseCombo.getValue());
            if (questionUpdateJeuCombo.getValue() != null) {
                currentQuestion.setJeuId(questionUpdateJeuCombo.getValue().getId());
            }

            serviceQuestion.modifier(currentQuestion);
            animateSuccess(btnQuestionUpdate);
            showFeedback(questionUpdateMessageLabel, "✅ Question modifiée avec succès ! ✨", true);
            loadQuestionsForSelector();
            refreshQuestionsList();
            resetQuestionUpdateForm();

        } catch (Exception e) {
            e.printStackTrace();
            showFeedback(questionUpdateMessageLabel, "❌ Erreur : " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleQuestionCancel() {
        resetQuestionUpdateForm();
        showFeedback(questionUpdateMessageLabel, "🔄 Formulaire réinitialisé", true);
    }

    private void loadQuestionsForDelete() {
        try {
            List<Question> questions = serviceQuestion.getAll();
            if (questionDeleteList != null) {
                questionDeleteList.setItems(FXCollections.observableArrayList(questions));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleQuestionDeleteSelected() {
        if (questionDeleteList == null) return;
        ObservableList<Question> selected = questionDeleteList.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()) {
            showFeedback(questionDeleteMessageLabel, "❓ Sélectionne au moins une question !", false);
            animateError(questionDeleteList);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer " + selected.size() + " question(s) ?");
        confirm.setContentText("⚠️ Cette action est irréversible !");

        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                int ok = 0;
                for (Question q : selected) {
                    try {
                        serviceQuestion.supprimer(q.getId());
                        ok++;
                    } catch (Exception ex) {
                        System.err.println("Erreur suppression question " + q.getId());
                    }
                }
                loadQuestionsForDelete();
                refreshQuestionsList();
                showFeedback(questionDeleteMessageLabel, "✅ " + ok + " question(s) supprimée(s) ! 🗑️", ok == selected.size());
            }
        });
    }

    @FXML
    private void handleRefreshQuestion() {
        loadQuestionsForDelete();
        refreshQuestionsList();
        showMessage("🔄 Liste actualisée !", true);
    }

    // ========== MÉTHODES SCORE ==========

    private void refreshScoresList() {
        refreshAllData();
        displayScores(allScores);
        updateScoreStats();
        refreshJeuCombos();
    }

    private void displayScores(List<Score> scores) {
        if (scoresCardsContainer == null) return;
        scoresCardsContainer.getChildren().clear();

        if (scores == null || scores.isEmpty()) {
            if (scoreEmptyMessage != null) {
                scoreEmptyMessage.setVisible(true);
                scoreEmptyMessage.setManaged(true);
            }
            return;
        }

        if (scoreEmptyMessage != null) {
            scoreEmptyMessage.setVisible(false);
            scoreEmptyMessage.setManaged(false);
        }

        int delay = 0;
        for (Score score : scores) {
            VBox card = createScoreCard(score);
            card.setOpacity(0);
            scoresCardsContainer.getChildren().add(card);

            PauseTransition pause = new PauseTransition(Duration.millis(delay));
            pause.setOnFinished(e -> {
                FadeTransition ft = new FadeTransition(Duration.millis(350), card);
                ft.setFromValue(0); ft.setToValue(1); ft.play();
                ScaleTransition st = new ScaleTransition(Duration.millis(350), card);
                st.setFromX(0.85); st.setFromY(0.85);
                st.setToX(1.0); st.setToY(1.0); st.play();
            });
            pause.play();
            delay += 60;
        }
    }

    private VBox createScoreCard(Score score) {
        VBox card = new VBox(10);
        card.getStyleClass().add("session-card");
        card.setPrefWidth(300);
        card.setPadding(new Insets(18));

        String color = "#E8F5E9";
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 25; "
                + "-fx-border-radius: 25; -fx-border-color: rgba(255,255,255,0.7); -fx-border-width: 3;");

        Label utilisateurLabel = new Label("👤 Utilisateur: " + score.getUtilisateurId());
        utilisateurLabel.getStyleClass().add("card-type");

        Label jeuLabel = new Label("🎮 Jeu ID: " + score.getJeuId());
        jeuLabel.getStyleClass().add("card-detail");

        Label pointsLabel = new Label("⭐ Points: " + score.getPoints());
        pointsLabel.getStyleClass().add("card-detail");

        Label dateLabel = new Label("📅 " + score.getDatePartie().toLocalDateTime().toLocalDate());
        dateLabel.getStyleClass().add("card-detail");

        card.getChildren().addAll(utilisateurLabel, jeuLabel, pointsLabel, dateLabel);

        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(180), card);
            st.setToX(1.06); st.setToY(1.06); st.play();
            DropShadow glow = new DropShadow();
            glow.setColor(Color.GOLD); glow.setRadius(18);
            card.setEffect(glow);
        });
        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(180), card);
            st.setToX(1.0); st.setToY(1.0); st.play();
            card.setEffect(null);
        });

        return card;
    }

    private void filterScores(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            displayScores(allScores);
            return;
        }
        try {
            int userId = Integer.parseInt(searchText);
            List<Score> filtered = allScores.stream()
                    .filter(s -> s.getUtilisateurId() == userId)
                    .toList();
            displayScores(filtered);
        } catch (NumberFormatException e) {
            displayScores(allScores);
        }
    }

    private void updateScoreStats() {
        if (totalScores == null || allScores == null) return;
        totalScores.setText(String.valueOf(allScores.size()));

        if (avgScore != null && !allScores.isEmpty()) {
            double avg = allScores.stream()
                    .mapToInt(Score::getPoints)
                    .average()
                    .orElse(0);
            avgScore.setText(String.format("%.1f", avg));
        }
    }

    @FXML
    private void handleScoreSave() {
        try {
            if (scoreUtilisateurId.getText() == null || scoreUtilisateurId.getText().trim().isEmpty()) {
                showFeedback(scoreAddMessageLabel, "❓ Entre l'ID utilisateur !", false);
                animateError(scoreUtilisateurId);
                return;
            }
            if (scoreJeuCombo.getValue() == null) {
                showFeedback(scoreAddMessageLabel, "❓ Choisis un jeu !", false);
                animateError(scoreJeuCombo);
                return;
            }
            if (scorePointsField.getText() == null || scorePointsField.getText().trim().isEmpty()) {
                showFeedback(scoreAddMessageLabel, "❓ Entre les points !", false);
                animateError(scorePointsField);
                return;
            }

            int utilisateurId = Integer.parseInt(scoreUtilisateurId.getText().trim());
            int points = Integer.parseInt(scorePointsField.getText().trim());

            Score score = new Score(
                    utilisateurId,
                    scoreJeuCombo.getValue().getId(),
                    points
            );

            serviceScore.ajouter(score);
            animateSuccess(btnScoreSave);
            showFeedback(scoreAddMessageLabel, "✅ Super ! Score ajouté ! 🏆", true);
            handleScoreReset();
            refreshScoresList();

        } catch (NumberFormatException e) {
            showFeedback(scoreAddMessageLabel, "❌ Les IDs et points doivent être des nombres !", false);
        } catch (Exception e) {
            e.printStackTrace();
            showFeedback(scoreAddMessageLabel, "❌ Oups ! " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleScoreReset() {
        resetScoreAddForm();
        showFeedback(scoreAddMessageLabel, "✨ Formulaire réinitialisé !", true);
    }

    private void resetScoreAddForm() {
        if (scoreUtilisateurId != null) scoreUtilisateurId.clear();
        if (scoreJeuCombo != null) scoreJeuCombo.setValue(null);
        if (scorePointsField != null) scorePointsField.clear();
        if (scoreAddMessageLabel != null) {
            scoreAddMessageLabel.setVisible(false);
            scoreAddMessageLabel.setManaged(false);
        }
    }

    private void loadScoresForSelector() {
        try {
            List<Score> scores = serviceScore.getAll();
            if (scoreSelector != null) scoreSelector.getItems().setAll(scores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadScoreData() {
        if (currentScore == null) return;
        if (scoreId != null) scoreId.setText(String.valueOf(currentScore.getId()));
        if (scoreUpdateUtilisateurId != null) scoreUpdateUtilisateurId.setText(String.valueOf(currentScore.getUtilisateurId()));
        if (scoreUpdatePointsField != null) scoreUpdatePointsField.setText(String.valueOf(currentScore.getPoints()));
        if (scoreUpdateDatePicker != null && currentScore.getDatePartie() != null) {
            scoreUpdateDatePicker.setValue(currentScore.getDatePartie().toLocalDateTime().toLocalDate());
        }

        if (scoreUpdateJeuCombo != null && currentScore.getJeuId() > 0) {
            try {
                Jeu jeu = serviceJeu.getById(currentScore.getJeuId());
                if (jeu != null) {
                    scoreUpdateJeuCombo.setValue(jeu);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetScoreUpdateForm() {
        if (scoreUpdateUtilisateurId != null) scoreUpdateUtilisateurId.clear();
        if (scoreUpdateJeuCombo != null) scoreUpdateJeuCombo.setValue(null);
        if (scoreUpdatePointsField != null) scoreUpdatePointsField.clear();
        if (scoreUpdateDatePicker != null) scoreUpdateDatePicker.setValue(null);
        if (scoreId != null) scoreId.setText("");
        currentScore = null;
    }

    @FXML
    private void handleScoreUpdate() {
        try {
            if (currentScore == null) {
                showFeedback(scoreUpdateMessageLabel, "❓ Choisis un score à modifier !", false);
                animateError(scoreSelector);
                return;
            }

            int utilisateurId = Integer.parseInt(scoreUpdateUtilisateurId.getText().trim());
            int points = Integer.parseInt(scoreUpdatePointsField.getText().trim());

            currentScore.setUtilisateurId(utilisateurId);
            if (scoreUpdateJeuCombo.getValue() != null) {
                currentScore.setJeuId(scoreUpdateJeuCombo.getValue().getId());
            }
            currentScore.setPoints(points);

            serviceScore.modifier(currentScore);
            animateSuccess(btnScoreUpdate);
            showFeedback(scoreUpdateMessageLabel, "✅ Score modifié avec succès ! ✨", true);
            loadScoresForSelector();
            refreshScoresList();
            resetScoreUpdateForm();

        } catch (NumberFormatException e) {
            showFeedback(scoreUpdateMessageLabel, "❌ Les IDs et points doivent être des nombres !", false);
        } catch (Exception e) {
            e.printStackTrace();
            showFeedback(scoreUpdateMessageLabel, "❌ Erreur : " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleScoreCancel() {
        resetScoreUpdateForm();
        showFeedback(scoreUpdateMessageLabel, "🔄 Formulaire réinitialisé", true);
    }

    private void loadScoresForDelete() {
        try {
            List<Score> scores = serviceScore.getAll();
            if (scoreDeleteList != null) {
                scoreDeleteList.setItems(FXCollections.observableArrayList(scores));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleScoreDeleteSelected() {
        if (scoreDeleteList == null) return;
        ObservableList<Score> selected = scoreDeleteList.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()) {
            showFeedback(scoreDeleteMessageLabel, "❓ Sélectionne au moins un score !", false);
            animateError(scoreDeleteList);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer " + selected.size() + " score(s) ?");
        confirm.setContentText("⚠️ Cette action est irréversible !");

        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                int ok = 0;
                for (Score s : selected) {
                    try {
                        serviceScore.supprimer(s.getId());
                        ok++;
                    } catch (Exception ex) {
                        System.err.println("Erreur suppression score " + s.getId());
                    }
                }
                loadScoresForDelete();
                refreshScoresList();
                showFeedback(scoreDeleteMessageLabel, "✅ " + ok + " score(s) supprimé(s) ! 🗑️", ok == selected.size());
            }
        });
    }

    @FXML
    private void handleRefreshScore() {
        loadScoresForDelete();
        refreshScoresList();
        showMessage("🔄 Liste actualisée !", true);
    }

    // ========== UTILITAIRES ==========

    private void showFeedback(Label label, String message, boolean success) {
        if (label == null) return;
        label.setText(message);
        label.setVisible(true);
        label.setManaged(true);
        label.setStyle(success
                ? "-fx-background-color: rgba(76,175,80,0.25); -fx-text-fill: #2E7D32; -fx-padding: 14 24; -fx-background-radius: 25; -fx-font-size: 19px;"
                : "-fx-background-color: rgba(244,67,54,0.25); -fx-text-fill: #C62828; -fx-padding: 14 24; -fx-background-radius: 25; -fx-font-size: 19px;");

        ScaleTransition st = new ScaleTransition(Duration.millis(280), label);
        st.setFromX(0.5); st.setFromY(0.5); st.setToX(1); st.setToY(1); st.play();

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Platform.runLater(() -> {
                    FadeTransition ft = new FadeTransition(Duration.millis(500), label);
                    ft.setFromValue(1); ft.setToValue(0);
                    ft.setOnFinished(e -> {
                        label.setVisible(false);
                        label.setManaged(false);
                    });
                    ft.play();
                });
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private void showMessage(String message, boolean success) {
        Label target = null;
        if (jeuListView != null && jeuListView.isVisible()) target = jeuAddMessageLabel;
        else if (questionListView != null && questionListView.isVisible()) target = questionAddMessageLabel;
        else if (scoreListView != null && scoreListView.isVisible()) target = scoreAddMessageLabel;
        showFeedback(target, message, success);
    }

    private void animateError(Node node) {
        if (node == null) return;
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO, e -> node.setStyle("-fx-border-color: #F44336; -fx-border-width: 3;")),
                new KeyFrame(Duration.millis(80), e -> node.setStyle("-fx-border-color: #F44336; -fx-border-width: 3; -fx-rotate: 2;")),
                new KeyFrame(Duration.millis(160), e -> node.setStyle("-fx-border-color: #F44336; -fx-border-width: 3; -fx-rotate: -2;")),
                new KeyFrame(Duration.millis(240), e -> node.setStyle("-fx-border-color: #F44336; -fx-border-width: 3; -fx-rotate: 0;")),
                new KeyFrame(Duration.millis(900), e -> node.setStyle(""))
        );
        tl.play();
    }

    private void animateSuccess(Node node) {
        if (node == null) return;
        ScaleTransition st = new ScaleTransition(Duration.millis(180), node);
        st.setToX(1.25); st.setToY(1.25); st.setAutoReverse(true); st.setCycleCount(2); st.play();
        node.setStyle("-fx-background-color: #66BB6A; -fx-text-fill: white;");
        PauseTransition pt = new PauseTransition(Duration.millis(700));
        pt.setOnFinished(e -> node.setStyle(""));
        pt.play();
    }
}