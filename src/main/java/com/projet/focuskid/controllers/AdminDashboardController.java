package com.projet.focuskid.controllers;

import com.projet.focuskid.entities.SessionDeCalme;
import com.projet.focuskid.services.ServiceSessionDeCalme;
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
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    // ========== ÉLÉMENTS DE MISE EN PAGE ==========
    @FXML private StackPane contentArea;
    @FXML private BorderPane mainBorderPane;

    // ========== LES DIFFÉRENTES VUES ==========
    @FXML private VBox listView;
    @FXML private VBox addView;
    @FXML private VBox updateView;
    @FXML private VBox deleteView;

    // ========== SIDEBAR ==========
    @FXML private Button btnAdd;
    @FXML private Button btnList;
    @FXML private Button btnRefresh;
    @FXML private Button btnDelete;

    // ========== VUE LISTE ==========
    @FXML private TextField searchField;
    @FXML private Label totalSessions;
    @FXML private Label avgDuration;
    @FXML private Label avgFeedback;
    @FXML private FlowPane cardsContainer;
    @FXML private VBox emptyMessage;

    // ========== VUE AJOUT ==========
    @FXML private ComboBox<String> enfantCombo;
    @FXML private ComboBox<String> typeActiviteCombo;
    @FXML private Slider dureeSlider;
    @FXML private Label dureeValue;
    // FIX: Durée réelle aussi dans le formulaire d'ajout
    @FXML private Slider dureeReelleSlider;
    @FXML private Label dureeReelleValue;
    @FXML private TextField noteParentField;
    @FXML private Label addMessageLabel;
    @FXML private ToggleGroup triggerGroup;
    @FXML private RadioButton triggerEnfant;
    @FXML private RadioButton triggerIA;
    @FXML private RadioButton triggerParent;
    // FIX: Feedback dans le formulaire d'ajout
    @FXML private ToggleGroup addFeedbackGroup;
    @FXML private RadioButton addFeedback1;
    @FXML private RadioButton addFeedback2;
    @FXML private RadioButton addFeedback3;
    @FXML private RadioButton addFeedback4;
    @FXML private RadioButton addFeedback5;
    @FXML private Button btnSave;
    @FXML private Button btnReset;

    // ========== VUE MODIFICATION ==========
    @FXML private ComboBox<SessionDeCalme> sessionSelector;
    @FXML private Label sessionId;
    @FXML private ComboBox<String> updateTypeCombo;
    @FXML private Slider updateDureeSlider;
    @FXML private Label updateDureeValue;
    // FIX: Durée prévue dans la modification
    @FXML private Slider updateDureePrevueSlider;
    @FXML private Label updateDureePrevueValue;
    @FXML private TextField updateNoteField;
    @FXML private Label updateMessageLabel;
    @FXML private ToggleGroup feedbackGroup;
    @FXML private RadioButton feedback1;
    @FXML private RadioButton feedback2;
    @FXML private RadioButton feedback3;
    @FXML private RadioButton feedback4;
    @FXML private RadioButton feedback5;
    @FXML private ComboBox<String> updateDeclencheurCombo;
    @FXML private DatePicker updateDatePicker;
    @FXML private Button btnCancel;
    @FXML public Button btnUpdate;

    // ========== VUE SUPPRESSION ==========
    @FXML private ListView<SessionDeCalme> deleteSessionsList;
    @FXML private Label deleteMessageLabel;
    @FXML private Button btnDeleteSelected;
    @FXML private Button btnRefreshList;

    // ========== DONNÉES ==========
    private ServiceSessionDeCalme service = new ServiceSessionDeCalme();
    private List<SessionDeCalme> allSessions;
    private SessionDeCalme currentSession;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ AdminDashboardController initialisé");

        // FIX 1: Plein écran à l'ouverture
        Platform.runLater(() -> {
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setFullScreen(true);
            stage.setFullScreenExitHint(""); // Masque le message d'aide
        });

        initializeViews();
        showListView();
        setupAnimations();
        refreshAllData();
    }

    // ========== INITIALISATION ==========

    private void initializeViews() {
        setupListViews();
        setupAddView();
        setupUpdateView();
        setupDeleteView();

        // FIX 2: Zonage coloré par bordure pour chaque vue (couleurs vives)
        styleViewBorder(listView,   "#4CAF50"); // vert = liste
        styleViewBorder(addView,    "#FF9800"); // orange = ajout
        styleViewBorder(updateView, "#2196F3"); // bleu = modification
        styleViewBorder(deleteView, "#F44336"); // rouge = suppression
    }

    private void styleViewBorder(VBox view, String color) {
        if (view != null) {
            view.setStyle(view.getStyle()
                    + "-fx-border-color: " + color + "; -fx-border-width: 6; -fx-border-radius: 40;");
        }
    }

    private void setupAnimations() {
        setupButtonAnimation(btnAdd);
        setupButtonAnimation(btnList);
        setupButtonAnimation(btnRefresh);
        setupButtonAnimation(btnDelete);
    }

    private void setupButtonAnimation(Button button) {
        if (button == null) return;
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.25); st.setToY(1.25); st.play();
            DropShadow glow = new DropShadow();
            glow.setColor(Color.GOLD); glow.setRadius(20);
            button.setEffect(glow);
        });
        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.0); st.setToY(1.0); st.play();
            button.setEffect(null);
        });
        button.setOnMousePressed(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(80), button);
            st.setToX(0.92); st.setToY(0.92); st.play();
            // FIX: Feedback visuel immédiat au clic (changement de couleur)
            button.setStyle("-fx-background-color: #FFD700; -fx-background-radius: 15;");
        });
        button.setOnMouseReleased(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(80), button);
            st.setToX(1.0); st.setToY(1.0); st.play();
            button.setStyle("");
        });
    }

    // ========== NAVIGATION (handlers FXML) ==========

    @FXML
    private void handleShowAdd() {
        flashContent();
        showAddView();
    }

    @FXML
    private void handleShowList() {
        flashContent();
        showListView();
    }

    @FXML
    private void handleShowUpdate() {
        flashContent();
        showUpdateView();
    }

    @FXML
    private void handleShowDelete() {
        flashContent();
        showDeleteView();
    }

    /** Animation de flash lors du changement de vue */
    private void flashContent() {
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,       e -> contentArea.setOpacity(0.5)),
                new KeyFrame(Duration.millis(120), e -> contentArea.setOpacity(1.0))
        );
        tl.play();
    }

    private void showListView() {
        switchView(listView);
        refreshSessionsList();
    }

    private void showAddView() {
        switchView(addView);
        resetAddForm();
    }

    private void showUpdateView() {
        switchView(updateView);
        loadSessionsForSelector();
        resetUpdateForm();
    }

    private void showDeleteView() {
        switchView(deleteView);
        loadSessionsForDelete();
    }

    private void switchView(VBox viewToShow) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(180), contentArea);
        fadeOut.setFromValue(1.0); fadeOut.setToValue(0.2);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(220), contentArea);
        fadeIn.setFromValue(0.2); fadeIn.setToValue(1.0);

        fadeOut.setOnFinished(e -> {
            listView.setVisible(false);   listView.setManaged(false);
            addView.setVisible(false);    addView.setManaged(false);
            updateView.setVisible(false); updateView.setManaged(false);
            deleteView.setVisible(false); deleteView.setManaged(false);

            viewToShow.setVisible(true);
            viewToShow.setManaged(true);
            fadeIn.play();
        });
        fadeOut.play();
    }

    // ========== CONFIGURATION DES VUES ==========

    private void setupListViews() {
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> filterSessions(newVal));
        }
    }

    private void setupAddView() {
        // FIX 3: setupRadioButtonToggle appliqué correctement AVANT de lier le groupe
        // Le ToggleGroup dans le FXML est partagé donc la désélection fonctionne nativement.
        // On ne surcharge plus setOnMouseClicked pour éviter le bug de désélection.

        if (dureeSlider != null && dureeValue != null) {
            dureeValue.textProperty().bind(dureeSlider.valueProperty().asString("%.0f min"));
        }
        if (dureeReelleSlider != null && dureeReelleValue != null) {
            dureeReelleValue.textProperty().bind(dureeReelleSlider.valueProperty().asString("%.0f min"));
        }
        if (typeActiviteCombo != null) {
            typeActiviteCombo.getItems().addAll(
                    "Respiration",   "Coloriage", "Musique",
                     "Histoire calme" );
        }
        if (enfantCombo != null) {
            enfantCombo.getItems().addAll(
                    "Lucas (ID: 1)", "Emma (ID: 2)", "Thomas (ID: 3)");
        }
    }

    private void setupUpdateView() {
        if (updateDureeSlider != null && updateDureeValue != null) {
            updateDureeValue.textProperty().bind(updateDureeSlider.valueProperty().asString("%.0f min"));
        }
        if (updateDureePrevueSlider != null && updateDureePrevueValue != null) {
            updateDureePrevueValue.textProperty().bind(
                    updateDureePrevueSlider.valueProperty().asString("%.0f min"));
        }
        if (updateTypeCombo != null) {
            updateTypeCombo.getItems().addAll(
                    "Respiration",  "Coloriage", "Musique",
                     "Histoire calme"  );
        }
        if (updateDeclencheurCombo != null && updateDeclencheurCombo.getItems().isEmpty()) {
            updateDeclencheurCombo.getItems().addAll("enfant", "ia_detection", "parent");
        }
        if (sessionSelector != null) {
            sessionSelector.setConverter(new StringConverter<SessionDeCalme>() {
                @Override public String toString(SessionDeCalme s) {
                    if (s == null) return "";
                    return "📅 Session #" + s.getId() + " — " + s.getTypeActivite();
                }
                @Override public SessionDeCalme fromString(String str) { return null; }
            });
            sessionSelector.setOnAction(event -> {
                currentSession = sessionSelector.getValue();
                if (currentSession != null) loadSessionData();
            });
        }
    }

    private void setupDeleteView() {
        if (deleteSessionsList != null) {
            deleteSessionsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            deleteSessionsList.setCellFactory(param -> new ListCell<SessionDeCalme>() {
                @Override
                protected void updateItem(SessionDeCalme item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null); setGraphic(null);
                    } else {
                        setText("🗑️ ID " + item.getId()
                                + " | " + item.getTypeActivite()
                                + " | ⏱️ " + item.getDureeReelle() + " min"
                                + " | ⭐ " + item.getFeedbackEnfant() + "/5");
                        setOnMouseEntered(e -> {
                            ScaleTransition st = new ScaleTransition(Duration.millis(120), this);
                            st.setToX(1.01); st.setToY(1.01); st.play();
                        });
                        setOnMouseExited(e -> {
                            ScaleTransition st = new ScaleTransition(Duration.millis(120), this);
                            st.setToX(1.0); st.setToY(1.0); st.play();
                        });
                    }
                }
            });
        }
    }

    // ========== GESTION DES DONNÉES ==========

    private void refreshAllData() {
        try {
            allSessions = service.getAll(null);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void refreshSessionsList() {
        refreshAllData();
        displaySessions(allSessions);
        updateStats();
    }

    private void displaySessions(List<SessionDeCalme> sessions) {
        if (cardsContainer == null) return;
        cardsContainer.getChildren().clear();

        if (sessions == null || sessions.isEmpty()) {
            if (emptyMessage != null) {
                emptyMessage.setVisible(true); emptyMessage.setManaged(true);
            }
            return;
        }
        if (emptyMessage != null) {
            emptyMessage.setVisible(false); emptyMessage.setManaged(false);
        }

        int delay = 0;
        for (SessionDeCalme session : sessions) {
            VBox card = createSessionCard(session);
            card.setOpacity(0);
            cardsContainer.getChildren().add(card);

            // FIX: Animation d'apparition décalée pour rendre l'affichage dynamique
            PauseTransition pause = new PauseTransition(Duration.millis(delay));
            pause.setOnFinished(e -> {
                FadeTransition ft = new FadeTransition(Duration.millis(350), card);
                ft.setFromValue(0); ft.setToValue(1); ft.play();
                ScaleTransition st = new ScaleTransition(Duration.millis(350), card);
                st.setFromX(0.85); st.setFromY(0.85);
                st.setToX(1.0);   st.setToY(1.0); st.play();
            });
            pause.play();
            delay += 60;
        }
    }

    private VBox createSessionCard(SessionDeCalme session) {
        VBox card = new VBox(10);
        card.getStyleClass().add("session-card");
        card.setPrefWidth(300);
        card.setPadding(new Insets(18));

        String color = getColorForActivity(session.getTypeActivite());
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 25; "
                + "-fx-border-radius: 25; -fx-border-color: rgba(255,255,255,0.7); -fx-border-width: 3;");

        Label typeLabel = new Label("🎯 " + session.getTypeActivite());
        typeLabel.getStyleClass().add("card-type");

        Label dureeLabel = new Label("⏱️ " + session.getDureeReelle() + " min  |  ⭐ "
                + session.getFeedbackEnfant() + "/5");
        dureeLabel.getStyleClass().add("card-detail");

        Label triggerLabel = new Label("🔔 " + getTriggerEmoji(session.getDeclencheur1()));
        triggerLabel.getStyleClass().add("card-detail");

        card.getChildren().addAll(typeLabel, dureeLabel, triggerLabel);

        String note = session.getNoteParent();
        if (note != null && !note.isEmpty()) {
            Label noteLabel = new Label("📝 " + note);
            noteLabel.getStyleClass().add("card-note");
            card.getChildren().add(noteLabel);
        }

        // FIX: Animation hover avec feedback visuel immédiat
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

    private String getColorForActivity(String activity) {
        if (activity == null) return "#FFEBEE";
        switch (activity) {
            case "Respiration": return "#E1F5FE";

            case "Coloriage":   return "#FFF3E0";
            case "Musique":     return "#F3E5F5";

            case "Histoire calme": return "#FFF8E1";
            default: return "#FCE4EC";
        }
    }

    private String getTriggerEmoji(String trigger) {
        if (trigger == null) return "❓ Inconnu";
        switch (trigger) {
            case "enfant":       return "👶 Enfant";
            case "ia_detection": return "🤖 IA";
            case "parent":       return "👪 Parent";
            default:             return "❓ " + trigger;
        }
    }

    private void filterSessions(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            displaySessions(allSessions); return;
        }
        List<SessionDeCalme> filtered = allSessions.stream()
                .filter(s -> s.getTypeActivite() != null
                        && s.getTypeActivite().toLowerCase().contains(searchText.toLowerCase()))
                .toList();
        displaySessions(filtered);
    }

    private void updateStats() {
        if (totalSessions == null || allSessions == null) return;
        int total = allSessions.size();
        totalSessions.setText(String.valueOf(total));
        if (total > 0) {
            if (avgDuration != null) {
                double avgDur = allSessions.stream()
                        .mapToInt(SessionDeCalme::getDureeReelle).average().orElse(0);
                avgDuration.setText(String.format("%.1f min", avgDur));
            }
            if (avgFeedback != null) {
                double avgFeed = allSessions.stream()
                        .filter(s -> s.getFeedbackEnfant() > 0)
                        .mapToInt(SessionDeCalme::getFeedbackEnfant).average().orElse(0);
                avgFeedback.setText(String.format("%.1f/5 ⭐", avgFeed));
            }
        }
    }

    // ========== HANDLERS - VUE AJOUT ==========

    @FXML
    private void handleSave() {
        try {
            if (typeActiviteCombo.getValue() == null) {
                showFeedback(addMessageLabel, "❓ Choisis une activité !", false);
                animateError(typeActiviteCombo); return;
            }
            if (enfantCombo.getValue() == null) {
                showFeedback(addMessageLabel, "👶 Choisis un enfant !", false);
                animateError(enfantCombo); return;
            }
            if (triggerGroup.getSelectedToggle() == null) {
                showFeedback(addMessageLabel, "🔔 Choisis un déclencheur !", false);
                return;
            }

            SessionDeCalme session = new SessionDeCalme();
            String enfantSelection = enfantCombo.getValue();
            int enfantId = Integer.parseInt(enfantSelection.replaceAll("[^0-9]", ""));
            session.setEnfantId(enfantId);
            session.setTypeActivite(typeActiviteCombo.getValue());

            // Déclencheur
            RadioButton selectedTrigger = (RadioButton) triggerGroup.getSelectedToggle();
            String triggerText = selectedTrigger.getText();
            if (triggerText.contains("Enfant")) session.setDeclencheur("enfant");
            else if (triggerText.contains("IA"))  session.setDeclencheur("ia_detection");
            else                                   session.setDeclencheur("parent");

            session.setDureePrevue((int) dureeSlider.getValue());
            session.setDureeReelle((int) dureeReelleSlider.getValue());

            // FIX: Feedback récupéré depuis le nouveau groupe d'ajout
            int feedbackValue = 1;
            if (addFeedbackGroup != null && addFeedbackGroup.getSelectedToggle() != null) {
                RadioButton fb = (RadioButton) addFeedbackGroup.getSelectedToggle();
                feedbackValue = Integer.parseInt(fb.getText().split(" ")[0]);
            }
            session.setFeedbackEnfant(feedbackValue);
            session.setNoteParent(noteParentField.getText());
            session.setHorodatage(Timestamp.from(Instant.now()));

            service.ajouter(session);
            animateSuccess(btnSave);
            showFeedback(addMessageLabel, "✅ Super ! Session ajoutée ! 🌟", true);
            handleReset();
            refreshAllData();

        } catch (Exception e) {
            e.printStackTrace();
            showFeedback(addMessageLabel, "❌ Oups ! " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleReset() {
        resetAddForm();
        showFeedback(addMessageLabel, "✨ Formulaire réinitialisé !", true);
    }

    private void resetAddForm() {
        if (typeActiviteCombo != null) typeActiviteCombo.setValue(null);
        if (enfantCombo != null) enfantCombo.setValue(null);
        if (dureeSlider != null) dureeSlider.setValue(5);
        if (dureeReelleSlider != null) dureeReelleSlider.setValue(5);
        if (noteParentField != null) noteParentField.clear();
        // FIX: On désélectionne proprement
        if (triggerGroup != null) triggerGroup.selectToggle(null);
        if (addFeedbackGroup != null) addFeedbackGroup.selectToggle(null);
        if (addMessageLabel != null) { addMessageLabel.setVisible(false); addMessageLabel.setManaged(false); }
    }

    // ========== HANDLERS - VUE MODIFICATION ==========

    private void loadSessionsForSelector() {
        try {
            List<SessionDeCalme> sessions = service.getAll(null);
            if (sessionSelector != null) sessionSelector.getItems().setAll(sessions);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadSessionData() {
        if (currentSession == null) return;
        if (updateTypeCombo != null) updateTypeCombo.setValue(currentSession.getTypeActivite());
        if (updateDureeSlider != null) updateDureeSlider.setValue(currentSession.getDureeReelle());
        if (updateDureePrevueSlider != null) updateDureePrevueSlider.setValue(currentSession.getDureePrevue());
        if (updateNoteField != null) updateNoteField.setText(currentSession.getNoteParent());
        if (updateDeclencheurCombo != null) updateDeclencheurCombo.setValue(currentSession.getDeclencheur1());

        // FIX: sélectionner le bon RadioButton de feedback
        if (feedbackGroup != null) {
            feedbackGroup.selectToggle(null);
            int fb = currentSession.getFeedbackEnfant();
            switch (fb) {
                case 1: feedback1.setSelected(true); break;
                case 2: feedback2.setSelected(true); break;
                case 3: feedback3.setSelected(true); break;
                case 4: feedback4.setSelected(true); break;
                case 5: feedback5.setSelected(true); break;
            }
        }
        if (updateDatePicker != null && currentSession.getHorodatage() != null) {
            updateDatePicker.setValue(currentSession.getHorodatage().toLocalDateTime().toLocalDate());
        }
    }

    private void resetUpdateForm() {
        if (updateTypeCombo != null) updateTypeCombo.setValue(null);
        if (updateDureeSlider != null) updateDureeSlider.setValue(5);
        if (updateDureePrevueSlider != null) updateDureePrevueSlider.setValue(5);
        if (updateNoteField != null) updateNoteField.clear();
        if (updateDeclencheurCombo != null) updateDeclencheurCombo.setValue(null);
        // FIX: désélection propre du ToggleGroup
        if (feedbackGroup != null) feedbackGroup.selectToggle(null);
        if (updateDatePicker != null) updateDatePicker.setValue(null);
        currentSession = null;
    }

    @FXML
    private void handleUpdate() {
        try {
            if (currentSession == null) {
                showFeedback(updateMessageLabel, "❓ Choisis une session à modifier !", false);
                animateError(sessionSelector); return;
            }
            if (updateTypeCombo.getValue() != null)
                currentSession.setTypeActivite(updateTypeCombo.getValue());

            currentSession.setDureeReelle((int) updateDureeSlider.getValue());
            currentSession.setDureePrevue((int) updateDureePrevueSlider.getValue());

            if (updateNoteField.getText() != null)
                currentSession.setNoteParent(updateNoteField.getText());
            if (updateDeclencheurCombo.getValue() != null)
                currentSession.setDeclencheur(updateDeclencheurCombo.getValue());

            if (feedbackGroup.getSelectedToggle() != null) {
                RadioButton selected = (RadioButton) feedbackGroup.getSelectedToggle();
                int fb = Integer.parseInt(selected.getText().split(" ")[0]);
                currentSession.setFeedbackEnfant(fb);
            }
            if (updateDatePicker.getValue() != null) {
                currentSession.setHorodatage(
                        Timestamp.valueOf(updateDatePicker.getValue().atStartOfDay()));
            }

            service.modifier(currentSession);
            animateSuccess(btnUpdate);
            showFeedback(updateMessageLabel, "✅ Session modifiée avec succès ! ✨", true);
            loadSessionsForSelector();
            refreshAllData();
            resetUpdateForm();

        } catch (Exception e) {
            e.printStackTrace();
            showFeedback(updateMessageLabel, "❌ Erreur : " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleCancel() {
        resetUpdateForm();
        showFeedback(updateMessageLabel, "🔄 Formulaire réinitialisé", true);
    }

    // ========== HANDLERS - VUE SUPPRESSION ==========

    private void loadSessionsForDelete() {
        try {
            List<SessionDeCalme> sessions = service.getAll(null);
            if (deleteSessionsList != null)
                deleteSessionsList.setItems(FXCollections.observableArrayList(sessions));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleDeleteSelected() {
        if (deleteSessionsList == null) return;
        ObservableList<SessionDeCalme> selected =
                deleteSessionsList.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()) {
            showFeedback(deleteMessageLabel, "❓ Sélectionne au moins une session !", false);
            animateError(deleteSessionsList); return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer " + selected.size() + " session(s) ?");
        confirm.setContentText("⚠️ Cette action est irréversible !");
        DialogPane dp = confirm.getDialogPane();
        try {
            dp.getStylesheets().add(
                    getClass().getResource("/com/projet/focuskid/style.css").toExternalForm());
            dp.getStyleClass().add("confirmation-dialog");
        } catch (Exception ignored) {}

        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                int ok = 0;
                for (SessionDeCalme s : selected) {
                    try { service.supprimer(s.getId()); ok++; }
                    catch (Exception ex) { System.err.println("Erreur suppression " + s.getId()); }
                }
                loadSessionsForDelete();
                refreshAllData();
                showFeedback(deleteMessageLabel, "✅ " + ok + " session(s) supprimée(s) ! 🗑️", ok == selected.size());
            }
        });
    }

    @FXML
    private void handleRefreshList() {
        loadSessionsForDelete();
        refreshSessionsList();
        showMessage("🔄 Liste actualisée !", true);
    }

    // ========== UTILITAIRES ==========

    /**
     * Affiche un message sur un label cible avec animation et disparition automatique.
     */
    private void showFeedback(Label label, String message, boolean success) {
        if (label == null) return;
        label.setText(message);
        label.setVisible(true);
        label.setManaged(true);
        label.setStyle(success
                ? "-fx-background-color: rgba(76,175,80,0.25); -fx-text-fill: #2E7D32; "
                  + "-fx-padding: 14 24; -fx-background-radius: 25; -fx-font-size: 19px;"
                : "-fx-background-color: rgba(244,67,54,0.25); -fx-text-fill: #C62828; "
                  + "-fx-padding: 14 24; -fx-background-radius: 25; -fx-font-size: 19px;");

        // Animation d'entrée
        ScaleTransition st = new ScaleTransition(Duration.millis(280), label);
        st.setFromX(0.5); st.setFromY(0.5); st.setToX(1); st.setToY(1); st.play();

        // Disparition après 3s
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Platform.runLater(() -> {
                    FadeTransition ft = new FadeTransition(Duration.millis(500), label);
                    ft.setFromValue(1); ft.setToValue(0);
                    ft.setOnFinished(e -> { label.setVisible(false); label.setManaged(false); });
                    ft.play();
                });
            } catch (InterruptedException ignored) {}
        }).start();
    }

    /** Méthode de compatibilité qui choisit le bon label selon la vue active */
    private void showMessage(String message, boolean success) {
        Label target = null;
        if (addView != null && addView.isVisible())       target = addMessageLabel;
        else if (updateView != null && updateView.isVisible()) target = updateMessageLabel;
        else if (deleteView != null && deleteView.isVisible()) target = deleteMessageLabel;
        showFeedback(target, message, success);
    }

    private void animateError(Node node) {
        if (node == null) return;
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,         e -> node.setStyle("-fx-border-color: #F44336; -fx-border-width: 3;")),
                new KeyFrame(Duration.millis(80),   e -> node.setStyle("-fx-border-color: #F44336; -fx-border-width: 3; -fx-rotate: 2;")),
                new KeyFrame(Duration.millis(160),  e -> node.setStyle("-fx-border-color: #F44336; -fx-border-width: 3; -fx-rotate: -2;")),
                new KeyFrame(Duration.millis(240),  e -> node.setStyle("-fx-border-color: #F44336; -fx-border-width: 3; -fx-rotate: 0;")),
                new KeyFrame(Duration.millis(900),  e -> node.setStyle(""))
        );
        tl.play();
    }

    private void animateSuccess(Node node) {
        if (node == null) return;
        ScaleTransition st = new ScaleTransition(Duration.millis(180), node);
        st.setToX(1.25); st.setToY(1.25); st.setAutoReverse(true); st.setCycleCount(2); st.play();
        // FIX: Feedback visuel - couleur verte immédiate
        node.setStyle("-fx-background-color: #66BB6A; -fx-text-fill: white;");
        PauseTransition pt = new PauseTransition(Duration.millis(700));
        pt.setOnFinished(e -> node.setStyle(""));
        pt.play();
    }
}
