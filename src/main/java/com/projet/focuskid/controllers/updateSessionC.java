package com.projet.focuskid.controllers;

import com.projet.focuskid.entities.SessionDeCalme;
import com.projet.focuskid.services.ServiceSessionDeCalme;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.util.List;

public class updateSessionC extends BaseController {

    @FXML private ComboBox<SessionDeCalme> sessionSelector;
    @FXML private Label sessionId;
    @FXML private ComboBox<String> typeActiviteCombo;
    @FXML private Slider dureeSlider;
    @FXML private Label dureeValue;
    @FXML private TextField noteParentField;
    @FXML private Label messageLabel;

    // RadioButtons pour le feedback
    @FXML private ToggleGroup feedbackGroup;
    @FXML private RadioButton feedback1;
    @FXML private RadioButton feedback2;
    @FXML private RadioButton feedback3;
    @FXML private RadioButton feedback4;
    @FXML private RadioButton feedback5;

    @FXML private Button btnUpdate;
    @FXML private Button btnCancel;

    private ServiceSessionDeCalme service = new ServiceSessionDeCalme();
    private SessionDeCalme currentSession;

    @FXML
    public void initialize() {
        super.initialize();
        System.out.println("✅ updateSessionC initialisé");

        // Vérification des composants
        System.out.println("btnUpdate = " + (btnUpdate != null ? "✅" : "❌"));

        setupBindings();
        loadTypeActivites();
        loadSessions();
        setupSessionSelector();
    }

    private void setupBindings() {
        if (dureeSlider != null && dureeValue != null) {
            dureeValue.textProperty().bind(
                    dureeSlider.valueProperty().asString("%.0f min")
            );
        }
    }

    private void loadTypeActivites() {
        if (typeActiviteCombo != null) {
            typeActiviteCombo.getItems().addAll(
                    "Respiration", "Méditation", "Coloriage",
                    "Musique", "Yoga", "Histoire calme"
            );
        }
    }

    private void loadSessions() {
        try {
            List<SessionDeCalme> sessions = service.getAll(null);
            sessionSelector.getItems().setAll(sessions);

            // Personnaliser l'affichage
            sessionSelector.setConverter(new StringConverter<SessionDeCalme>() {
                @Override
                public String toString(SessionDeCalme session) {
                    if (session == null) return "";
                    return "ID " + session.getId() + " - " + session.getTypeActivite() +
                            " (" + session.getHorodatage() + ")";
                }

                @Override
                public SessionDeCalme fromString(String string) {
                    return null;
                }
            });

            System.out.println("✅ " + sessions.size() + " sessions chargées");

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Erreur chargement: " + e.getMessage(), false);
        }
    }

    private void setupSessionSelector() {
        sessionSelector.setOnAction(event -> {
            currentSession = sessionSelector.getValue();
            if (currentSession != null) {
                loadSessionData();
            }
        });
    }

    private void loadSessionData() {
        if (currentSession == null) return;

        sessionId.setText(String.valueOf(currentSession.getId()));
        typeActiviteCombo.setValue(currentSession.getTypeActivite());
        dureeSlider.setValue(currentSession.getDureeReelle());
        noteParentField.setText(currentSession.getNoteParent());

        // Sélectionner le feedback
        int feedback = currentSession.getFeedbackEnfant();
        switch (feedback) {
            case 1: feedback1.setSelected(true); break;
            case 2: feedback2.setSelected(true); break;
            case 3: feedback3.setSelected(true); break;
            case 4: feedback4.setSelected(true); break;
            case 5: feedback5.setSelected(true); break;
            default: feedback3.setSelected(true); break;
        }

        System.out.println("✅ Session chargée: ID=" + currentSession.getId());
    }

    @FXML
    private void handleUpdate() {
        System.out.println("🟢 Clic sur le bouton Mettre à jour");

        try {
            // Validation
            if (currentSession == null) {
                showMessage("Veuillez sélectionner une session", false);
                return;
            }

            if (typeActiviteCombo.getValue() == null || typeActiviteCombo.getValue().isEmpty()) {
                showMessage("Veuillez sélectionner un type d'activité", false);
                return;
            }

            // Mettre à jour les valeurs
            currentSession.setTypeActivite(typeActiviteCombo.getValue());
            currentSession.setDureeReelle((int) dureeSlider.getValue());
            currentSession.setNoteParent(noteParentField.getText());

            // Récupérer le feedback
            int feedback = 3;
            if (feedbackGroup != null && feedbackGroup.getSelectedToggle() != null) {
                RadioButton selected = (RadioButton) feedbackGroup.getSelectedToggle();
                if (selected == feedback1) feedback = 1;
                else if (selected == feedback2) feedback = 2;
                else if (selected == feedback3) feedback = 3;
                else if (selected == feedback4) feedback = 4;
                else if (selected == feedback5) feedback = 5;
            }
            currentSession.setFeedbackEnfant(feedback);

            // Afficher les valeurs pour déboguer
            System.out.println("Mise à jour session ID " + currentSession.getId() + ":");
            System.out.println("  Type: " + currentSession.getTypeActivite());
            System.out.println("  Durée: " + currentSession.getDureeReelle());
            System.out.println("  Feedback: " + feedback);
            System.out.println("  Note: " + currentSession.getNoteParent());

            // Appel au service
            service.modifier(currentSession);

            showMessage("✅ Session mise à jour avec succès !", true);

            // Rafraîchir la liste
            loadSessions();

            // Réinitialiser la sélection
            sessionSelector.setValue(null);
            currentSession = null;
            sessionId.setText("");

            // Réinitialiser le formulaire
            typeActiviteCombo.setValue(null);
            dureeSlider.setValue(5);
            noteParentField.clear();
            feedback3.setSelected(true);

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la mise à jour:");
            e.printStackTrace();
            showMessage("❌ Erreur: " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleCancel() {
        System.out.println("Annulation");
        if (currentSession != null) {
            loadSessionData(); // Recharger les données originales
        } else {
            typeActiviteCombo.setValue(null);
            dureeSlider.setValue(5);
            noteParentField.clear();
            feedback3.setSelected(true);
        }
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
                messageLabel.setStyle("-fx-background-color: rgba(76, 175, 80, 0.2); -fx-text-fill: #388E3C; -fx-padding: 10; -fx-background-radius: 5;");
            } else {
                messageLabel.setStyle("-fx-background-color: rgba(244, 67, 54, 0.2); -fx-text-fill: #D32F2F; -fx-padding: 10; -fx-background-radius: 5;");
            }

            // Auto-hide après 3 secondes
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