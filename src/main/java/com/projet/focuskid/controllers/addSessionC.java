package com.projet.focuskid.controllers;

import com.projet.focuskid.entities.SessionDeCalme;
import com.projet.focuskid.services.ServiceSessionDeCalme;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Timestamp;
import java.time.Instant;

public class addSessionC extends BaseController {

    @FXML private ComboBox<String> enfantCombo;
    @FXML private ComboBox<String> typeActiviteCombo;
    @FXML private Slider dureeSlider;
    @FXML private Label dureeValue;
    @FXML private TextField noteParentField;
    @FXML private Label messageLabel;
    @FXML private Button btnSave;
    @FXML private Button btnReset;

    // Boutons de déclencheur
    @FXML private ToggleGroup triggerGroup;
    @FXML private RadioButton triggerEnfant;
    @FXML private RadioButton triggerIA;
    @FXML private RadioButton triggerParent;

    private ServiceSessionDeCalme service = new ServiceSessionDeCalme();

    @FXML
    public void initialize() {
        super.initialize();
        System.out.println("✅ addSessionC initialisé");

        setupBindings();
        loadComboBoxes();

        // Initialiser le groupe de boutons radio
        if (triggerGroup == null && triggerEnfant != null) {
            triggerGroup = new ToggleGroup();
            triggerEnfant.setToggleGroup(triggerGroup);
            triggerIA.setToggleGroup(triggerGroup);
            triggerParent.setToggleGroup(triggerGroup);
            triggerEnfant.setSelected(true); // Sélection par défaut
        }
    }

    private void setupBindings() {
        if (dureeSlider != null && dureeValue != null) {
            dureeValue.textProperty().bind(
                    dureeSlider.valueProperty().asString("%.0f min")
            );
        }
    }

    private void loadComboBoxes() {
        // Types d'activités
        if (typeActiviteCombo != null) {
            typeActiviteCombo.getItems().addAll(
                    "Respiration",
                    "Méditation",
                    "Coloriage",
                    "Musique",
                    "Yoga",
                    "Histoire calme",
                    "Exercice de relaxation"
            );
        }

        // Enfants (à remplacer par des données de la BDD)
        if (enfantCombo != null) {
            enfantCombo.getItems().addAll(
                    "Lucas (ID: 1)",
                    "Emma (ID: 2)"
            );
        }
    }

    @FXML
    private void handleSave() {
        System.out.println("💾 Tentative de sauvegarde...");

        try {
            // Validation des champs
            if (typeActiviteCombo.getValue() == null) {
                showMessage("Veuillez sélectionner un type d'activité", false);
                return;
            }

            if (enfantCombo.getValue() == null) {
                showMessage("Veuillez sélectionner un enfant", false);
                return;
            }

            // Extraire l'ID de l'enfant (à adapter selon votre format)
            int enfantId = 1; // Valeur par défaut
            String selectedEnfant = enfantCombo.getValue();
            if (selectedEnfant.contains("ID: 1")) {
                enfantId = 1;
            } else if (selectedEnfant.contains("ID: 2")) {
                enfantId = 2;
            }

            // Récupérer le déclencheur sélectionné
            String declencheur = "enfant";
            if (triggerIA != null && triggerIA.isSelected()) {
                declencheur = "ia_detection";
            } else if (triggerParent != null && triggerParent.isSelected()) {
                declencheur = "parent";
            }

            // Créer la session
            SessionDeCalme session = new SessionDeCalme();
            session.setEnfantId(enfantId);
            session.setTypeActivite(typeActiviteCombo.getValue());
            session.setDeclencheur(declencheur);
            session.setDureePrevue((int) dureeSlider.getValue());
            session.setDureeReelle(0); // À remplir plus tard
            session.setFeedbackEnfant(0); // À remplir plus tard
            session.setNoteParent(noteParentField.getText());
            session.setHorodatage(Timestamp.from(Instant.now()));

            // Appel au service
            System.out.println("Ajout de la session: " + session);
            service.ajouter(session);

            showMessage("✅ Session ajoutée avec succès !", true);
            handleReset(); // Réinitialiser le formulaire

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("❌ Erreur: " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleReset() {
        System.out.println("🔄 Réinitialisation du formulaire");

        if (typeActiviteCombo != null) typeActiviteCombo.setValue(null);
        if (enfantCombo != null) enfantCombo.setValue(null);
        if (dureeSlider != null) dureeSlider.setValue(5);
        if (noteParentField != null) noteParentField.clear();
        if (triggerEnfant != null) triggerEnfant.setSelected(true);
    }

    @FXML
    private void handleBack() {
        System.out.println("🔄 Retour à l'accueil");
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
            // Fallback
            Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle(success ? "Succès" : "Erreur");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
}