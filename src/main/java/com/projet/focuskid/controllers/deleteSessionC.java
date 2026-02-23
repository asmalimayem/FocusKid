package com.projet.focuskid.controllers;

import com.projet.focuskid.entities.SessionDeCalme;
import com.projet.focuskid.services.ServiceSessionDeCalme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import java.util.List;

public class deleteSessionC extends BaseController {

    @FXML private ListView<SessionDeCalme> sessionsList;
    @FXML private Label messageLabel;
    @FXML private Button btnDeleteSelected;
    @FXML private Button btnRefresh;

    private ServiceSessionDeCalme service = new ServiceSessionDeCalme();
    private ObservableList<SessionDeCalme> sessions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        super.initialize();
        System.out.println("✅ deleteSessionC initialisé");

        setupListView();
        loadSessions();
    }

    private void setupListView() {
        // Permettre la sélection multiple
        sessionsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Personnaliser l'affichage
        sessionsList.setCellFactory(param -> new ListCell<SessionDeCalme>() {
            @Override
            protected void updateItem(SessionDeCalme item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID " + item.getId() + " | " + item.getTypeActivite() +
                            " | Durée: " + item.getDureeReelle() + "min | Feedback: " +
                            item.getFeedbackEnfant() + "/5");

                    // Style alterné
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #f9f9f9;");
                    } else {
                        setStyle("-fx-background-color: white;");
                    }
                }
            }
        });
    }

    private void loadSessions() {
        try {
            List<SessionDeCalme> allSessions = service.getAll(null);
            sessions.setAll(allSessions);
            sessionsList.setItems(sessions);

            System.out.println("✅ " + sessions.size() + " sessions chargées");

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Erreur chargement: " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleDeleteSelected() {
        ObservableList<SessionDeCalme> selected = sessionsList.getSelectionModel().getSelectedItems();

        if (selected.isEmpty()) {
            showMessage("Veuillez sélectionner au moins une session", false);
            return;
        }

        // Demander confirmation
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer " + selected.size() + " session(s) ?");
        confirm.setContentText("Cette action est irréversible !");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteSessions(selected);
            }
        });
    }

    private void deleteSessions(ObservableList<SessionDeCalme> sessionsToDelete) {
        int successCount = 0;
        int errorCount = 0;

        for (SessionDeCalme session : sessionsToDelete) {
            try {
                service.supprimer(session.getId());
                successCount++;
            } catch (Exception e) {
                errorCount++;
                System.err.println("Erreur suppression session " + session.getId() + ": " + e.getMessage());
            }
        }

        // Recharger la liste
        loadSessions();

        // Afficher le résultat
        String message = successCount + " session(s) supprimée(s)";
        if (errorCount > 0) {
            message += ", " + errorCount + " erreur(s)";
            showMessage("⚠️ " + message, false);
        } else {
            showMessage("✅ " + message, true);
        }
    }

    @FXML
    private void handleRefresh() {
        loadSessions();
        showMessage("Liste actualisée", true);
    }

    @FXML
    private void handleBack() {
        navigateTo("home.fxml");
    }

    @Override
    protected void showMessage(String message, boolean success) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setVisible(true);
            messageLabel.setManaged(true);

            if (success) {
                messageLabel.setStyle("-fx-background-color: rgba(76, 175, 80, 0.2); -fx-text-fill: #388E3C;");
            } else {
                messageLabel.setStyle("-fx-background-color: rgba(244, 67, 54, 0.2); -fx-text-fill: #D32F2F;");
            }

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    javafx.application.Platform.runLater(() -> {
                        messageLabel.setVisible(false);
                        messageLabel.setManaged(false);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle(success ? "Succès" : "Erreur");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
}