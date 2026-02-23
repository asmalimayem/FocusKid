package com.projet.focuskid.controllers;

import com.projet.focuskid.entities.SessionDeCalme;
import com.projet.focuskid.services.ServiceSessionDeCalme;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.sql.Timestamp;
import java.util.List;

public class listSessionsC extends BaseController {

    @FXML private FlowPane cardsContainer;
    @FXML private TextField searchField;
    @FXML private Label totalSessions;
    @FXML private Label avgDuration;
    @FXML private Label avgFeedback;
    @FXML private VBox emptyMessage;

    private ServiceSessionDeCalme service = new ServiceSessionDeCalme();
    private List<SessionDeCalme> allSessions;

    @FXML
    public void initialize() {
        super.initialize();
        System.out.println("✅ listSessionsC initialisé");

        loadSessions();
        setupSearch();
    }

    private void loadSessions() {
        try {
            allSessions = service.getAll(null);
            updateStats();
            displaySessions(allSessions);

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Erreur chargement: " + e.getMessage(), false);
        }
    }

    private void displaySessions(List<SessionDeCalme> sessions) {
        cardsContainer.getChildren().clear();

        if (sessions.isEmpty()) {
            emptyMessage.setVisible(true);
            emptyMessage.setManaged(true);
            return;
        }

        emptyMessage.setVisible(false);
        emptyMessage.setManaged(false);

        for (SessionDeCalme session : sessions) {
            VBox card = createSessionCard(session);
            cardsContainer.getChildren().add(card);
        }
    }

    private VBox createSessionCard(SessionDeCalme session) {
        // Couleur de fond selon le type d'activité
        String cardColor = getColorForActivity(session.getTypeActivite());

        // Carte principale
        VBox card = new VBox(10);
        card.getStyleClass().add("session-card");
        card.setStyle("-fx-background-color: " + cardColor + ";");
        card.setPrefWidth(220);
        card.setPrefHeight(180);
        card.setPadding(new Insets(15));

        // En-tête avec icône et type
        HBox header = new HBox(10);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label typeLabel = new Label(session.getTypeActivite());
        typeLabel.getStyleClass().add("card-type");
        typeLabel.setWrapText(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label idLabel = new Label("#" + session.getId());
        idLabel.getStyleClass().add("card-id");

        header.getChildren().addAll(typeLabel, spacer, idLabel);

        // Détails
        VBox details = new VBox(8);

        // Durée
        HBox durationBox = new HBox(5);
        durationBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label durationIcon = new Label("⏱️");
        Label durationText = new Label(session.getDureeReelle() + " min");
        durationText.getStyleClass().add("card-detail");
        durationBox.getChildren().addAll(durationIcon, durationText);

        // Feedback avec étoiles
        HBox feedbackBox = new HBox(5);
        feedbackBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label feedbackIcon = new Label("⭐");
        Label feedbackText = new Label(session.getFeedbackEnfant() + "/5");
        feedbackText.getStyleClass().add("card-detail");
        feedbackBox.getChildren().addAll(feedbackIcon, feedbackText);

        // Date
        HBox dateBox = new HBox(5);
        dateBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label dateIcon = new Label("📅");
        Timestamp horodatage = session.getHorodatage();
        String dateStr = horodatage != null ?
                horodatage.toLocalDateTime().toLocalDate().toString() : "Date inconnue";
        Label dateText = new Label(dateStr);
        dateText.getStyleClass().add("card-detail");
        dateBox.getChildren().addAll(dateIcon, dateText);

        details.getChildren().addAll(durationBox, feedbackBox, dateBox);

        // Note parent (si existe)
        if (session.getNoteParent() != null && !session.getNoteParent().isEmpty()) {
            HBox noteBox = new HBox(5);
            noteBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            Label noteIcon = new Label("📝");
            Label noteText = new Label(session.getNoteParent());
            noteText.getStyleClass().add("card-note");
            noteText.setWrapText(true);
            noteBox.getChildren().addAll(noteIcon, noteText);
            details.getChildren().add(noteBox);
        }

        // Déclencheur (badge)
        HBox footer = new HBox();
        footer.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        Label triggerBadge = new Label(getTriggerEmoji(session.getDeclencheur1()) + " " + session.getDeclencheur1());
        triggerBadge.getStyleClass().add("trigger-badge");
        footer.getChildren().add(triggerBadge);

        card.getChildren().addAll(header, new Separator(), details, footer);

        // Double-clic pour modifier
        card.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleEdit(session);
            }
        });

        // Effet au survol
        card.setOnMouseEntered(e ->
                card.setStyle("-fx-background-color: " + cardColor + "; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.3, 0, 5); -fx-scale-x: 1.02; -fx-scale-y: 1.02;")
        );
        card.setOnMouseExited(e ->
                card.setStyle("-fx-background-color: " + cardColor + "; -fx-effect: null; -fx-scale-x: 1; -fx-scale-y: 1;")
        );

        return card;
    }

    private String getColorForActivity(String type) {
        if (type == null) return "#E1F5FE"; // Bleu par défaut

        switch (type.toLowerCase()) {
            case "respiration": return "#E8F5E9"; // Vert clair
            case "méditation": return "#E1F5FE"; // Bleu clair
            case "coloriage": return "#FFF3E0"; // Orange clair
            case "musique": return "#F3E5F5"; // Violet clair
            case "yoga": return "#E0F2F1"; // Turquoise clair
            case "histoire calme": return "#FFEBEE"; // Rouge clair
            default: return "#E1F5FE"; // Bleu par défaut
        }
    }

    private String getTriggerEmoji(String declencheur) {
        if (declencheur == null) return "❓";

        switch (declencheur.toLowerCase()) {
            case "enfant": return "👶";
            case "ia_detection": return "🤖";
            case "parent": return "👪";
            default: return "❓";
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterSessions(newValue);
        });
    }

    private void filterSessions(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            displaySessions(allSessions);
            return;
        }

        List<SessionDeCalme> filtered = allSessions.stream()
                .filter(s -> s.getTypeActivite().toLowerCase().contains(searchText.toLowerCase()) ||
                        (s.getNoteParent() != null && s.getNoteParent().toLowerCase().contains(searchText.toLowerCase())))
                .toList();

        displaySessions(filtered);
    }

    private void updateStats() {
        int total = allSessions.size();
        totalSessions.setText(String.valueOf(total));

        if (total > 0) {
            double avgDur = allSessions.stream()
                    .mapToInt(SessionDeCalme::getDureeReelle)
                    .average()
                    .orElse(0);
            avgDuration.setText(String.format("%.1f min", avgDur));

            double avgFeed = allSessions.stream()
                    .filter(s -> s.getFeedbackEnfant() > 0)
                    .mapToInt(SessionDeCalme::getFeedbackEnfant)
                    .average()
                    .orElse(0);
            avgFeedback.setText(String.format("%.1f/5", avgFeed));
        }
    }

    private void handleEdit(SessionDeCalme session) {
        try {
            // Vous pouvez passer l'ID par une variable statique ou un paramètre
            System.out.println("Modification session ID: " + session.getId());
            navigateTo("updateSession.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Erreur ouverture modification: " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleRefresh() {
        loadSessions();
        showMessage("Liste actualisée !", true);
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