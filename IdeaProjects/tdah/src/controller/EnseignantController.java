package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Exercice;
import model.Quiz;
import model.QuestionExercice;
import model.QuestionQuiz;
import model.CorrectionExercice;
import model.CorrectionQuiz;
import dao.ExerciceDAO;
import dao.QuestionExerciceDAO;
import dao.QuizDAO;
import dao.QuestionQuizDAO;
import dao.CorrectionExerciceDAO;
import dao.CorrectionQuizDAO;
import utils.DatabaseConnection;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EnseignantController implements Initializable {

    // ========== COMPOSANTS EXERCICE ==========
    @FXML private ListView<Exercice> listExercices;
    @FXML private TextField tfTitreExercice;
    @FXML private TextArea taDescriptionExercice;
    @FXML private ComboBox<String> cbDifficulteExercice;
    @FXML private TextField tfPointsExercice;
    @FXML private TextField tfDureeExercice;
    @FXML private TextField searchExerciceField;
    @FXML private Label totalExercicesLabel;
    @FXML private Label lblModeExercice;
    @FXML private Label lblExerciceId;
    @FXML private Button btnSaveExercice;
    @FXML private Button btnAnnulerExercice;
    @FXML private TextArea taNouvelleQuestion;
    @FXML private TextArea taNouvelleCorrection;
    @FXML private TableView<QuestionExercice> tableQuestionsExercice;
    @FXML private TableColumn<QuestionExercice, Integer> colNumQuestion;
    @FXML private TableColumn<QuestionExercice, String> colEnonceQuestion;
    @FXML private TableColumn<QuestionExercice, String> colCorrectionQuestion;
    @FXML private TableColumn<QuestionExercice, Void> colActionsQuestion;

    // ========== COMPOSANTS QUIZ ==========
    @FXML private ListView<Quiz> listQuiz;
    @FXML private TextField tfTitreQuiz;
    @FXML private TextArea taDescriptionQuiz;
    @FXML private ComboBox<String> cbDifficulteQuiz;
    @FXML private TextField tfPointsQuiz;
    @FXML private TextField tfDureeQuiz;
    @FXML private ComboBox<String> cbTypeQuestion;
    @FXML private TextArea taEnonceQuestionQuiz;
    @FXML private TextField tfPointsQuestion;
    @FXML private TextField tfTempsLimite;
    @FXML private TextField searchQuizField;
    @FXML private Label totalQuizLabel;
    @FXML private Label lblModeQuiz;
    @FXML private Label lblQuizId;
    @FXML private Button btnSaveQuiz;
    @FXML private Button btnAnnulerQuiz;
    @FXML private TableView<QuestionQuiz> tableQuestionsQuiz;
    @FXML private TableColumn<QuestionQuiz, Integer> colNumQuestionQuiz;
    @FXML private TableColumn<QuestionQuiz, String> colTypeQuestionQuiz;
    @FXML private TableColumn<QuestionQuiz, String> colEnonceQuestionQuiz;
    @FXML private TableColumn<QuestionQuiz, String> colCorrectionQuiz;
    @FXML private TextField tfCorrectionQuiz;

    // ========== COMPOSANTS STATISTIQUES ==========
    @FXML private Label statTotalExercices;
    @FXML private Label statTotalQuestions;
    @FXML private Label statTotalQuiz;

    // ========== DAOs (garde les pour le chargement) ==========
    private ExerciceDAO exerciceDAO = new ExerciceDAO();
    private QuestionExerciceDAO questionExerciceDAO = new QuestionExerciceDAO();
    private QuizDAO quizDAO = new QuizDAO();
    private QuestionQuizDAO questionQuizDAO = new QuestionQuizDAO();
    private CorrectionExerciceDAO correctionExerciceDAO = new CorrectionExerciceDAO();
    private CorrectionQuizDAO correctionQuizDAO = new CorrectionQuizDAO();

    // ========== VARIABLES D'ÉTAT ==========
    private boolean modeEditionExercice = false;
    private Exercice exerciceEnEdition;
    private ObservableList<QuestionExercice> questionsEnAttente = FXCollections.observableArrayList();
    private boolean modeEditionQuiz = false;
    private Quiz quizEnEdition;
    private ObservableList<QuestionQuiz> questionsQuizEnAttente = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialisation du contrôleur...");

        cbDifficulteExercice.setItems(FXCollections.observableArrayList("facile", "moyen", "difficile"));
        cbDifficulteQuiz.setItems(FXCollections.observableArrayList("facile", "moyen", "difficile"));
        cbTypeQuestion.setItems(FXCollections.observableArrayList("qcm", "vrai_faux"));

        setupQuestionsTable();
        setupQuizQuestionsTable();
        setupCustomLists();
        chargerExercices();
        chargerQuiz();
        setupListeners();
        nouvelExercice();
        nouveauQuiz();

        System.out.println("Initialisation terminée!");
    }

    private void setupCustomLists() {
        listExercices.setCellFactory(param -> new ListCell<Exercice>() {
            @Override
            protected void updateItem(Exercice exercice, boolean empty) {
                super.updateItem(exercice, empty);
                if (empty || exercice == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    Label lblTitre = new Label(exercice.getTitre());
                    lblTitre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                    Label lblDetails = new Label(String.format("[%s] %d pts - %ds",
                            exercice.getDifficulte(), exercice.getPointsTotal(), exercice.getDuree()));
                    lblDetails.setStyle("-fx-text-fill: #666; -fx-font-size: 11px;");
                    VBox vbox = new VBox(3, lblTitre, lblDetails);
                    hbox.getChildren().add(vbox);
                    setGraphic(hbox);
                }
            }
        });

        listQuiz.setCellFactory(param -> new ListCell<Quiz>() {
            @Override
            protected void updateItem(Quiz quiz, boolean empty) {
                super.updateItem(quiz, empty);
                if (empty || quiz == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    Label lblTitre = new Label(quiz.getTitre());
                    lblTitre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                    Label lblDetails = new Label(String.format("[%s] %d pts - %ds",
                            quiz.getDifficulte(), quiz.getPointsTotal(), quiz.getDureeTotale()));
                    lblDetails.setStyle("-fx-text-fill: #666; -fx-font-size: 11px;");
                    VBox vbox = new VBox(3, lblTitre, lblDetails);
                    hbox.getChildren().add(vbox);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void setupListeners() {
        // Listener pour la sélection d'exercice
        listExercices.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        modeEditionExercice = true;
                        exerciceEnEdition = newSelection;
                        lblModeExercice.setText("✏️ Modification Exercice");
                        lblExerciceId.setText("ID: " + newSelection.getIdExercice());
                        remplirFormulaireExercice(newSelection);
                        questionsEnAttente.clear();
                        List<QuestionExercice> questions = questionExerciceDAO.getByExercice(newSelection.getIdExercice());
                        for (QuestionExercice q : questions) {
                            CorrectionExercice corr = correctionExerciceDAO.getByQuestion(q.getIdQuestionExercice());
                            if (corr != null) q.setCorrection(corr.getSolution());
                        }
                        questionsEnAttente.addAll(questions);
                        tableQuestionsExercice.setItems(questionsEnAttente);
                        btnSaveExercice.setText("💾 Mettre à jour");
                    }
                });

        // ===== NOUVEAU LISTENER POUR QUIZ =====
        listQuiz.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        modeEditionQuiz = true;
                        quizEnEdition = newSelection;
                        lblModeQuiz.setText("✏️ Modification Quiz");
                        lblQuizId.setText("ID: " + newSelection.getIdQuiz());
                        remplirFormulaireQuiz(newSelection);

                        // Charger les questions du quiz sélectionné
                        List<QuestionQuiz> questions = questionQuizDAO.getByQuiz(newSelection.getIdQuiz());
                        questionsQuizEnAttente.clear();
                        questionsQuizEnAttente.addAll(questions);
                        tableQuestionsQuiz.setItems(questionsQuizEnAttente);
                        btnSaveQuiz.setText("💾 Mettre à jour");
                    }
                    // Si pas de sélection, on garde les questions existantes
                });
        // ===== FIN DU NOUVEAU LISTENER =====

        // Recherche dynamique
        if (searchExerciceField != null) {
            searchExerciceField.textProperty().addListener((obs, oldVal, newVal) -> filterExercices(newVal));
        }
        if (searchQuizField != null) {
            searchQuizField.textProperty().addListener((obs, oldVal, newVal) -> filterQuiz(newVal));
        }
    }

    private void setupQuestionsTable() {
        colNumQuestion.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        questionsEnAttente.indexOf(cellData.getValue()) + 1).asObject());

        colEnonceQuestion.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEnonce()));
        colEnonceQuestion.setCellFactory(TextFieldTableCell.forTableColumn());
        colEnonceQuestion.setOnEditCommit(event -> {
            event.getRowValue().setEnonce(event.getNewValue());
        });

        colCorrectionQuestion.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCorrection()));
        colCorrectionQuestion.setCellFactory(TextFieldTableCell.forTableColumn());
        colCorrectionQuestion.setOnEditCommit(event -> {
            event.getRowValue().setCorrection(event.getNewValue());
        });

        colActionsQuestion.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("✏️");
            private final Button btnDelete = new Button("🗑️");
            private final HBox pane = new HBox(5, btnEdit, btnDelete);

            {
                btnEdit.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-cursor: hand;");
                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");

                btnEdit.setOnAction(event -> {
                    QuestionExercice question = getTableView().getItems().get(getIndex());
                    ouvrirDialogueEditionQuestion(question);
                });

                // ===== CODE À AJOUTER ICI =====
                btnDelete.setOnAction(event -> {
                    QuestionExercice question = getTableView().getItems().get(getIndex());
                    if (question.getIdQuestionExercice() > 0) {
                        // La question existe en base, on la supprime juste de l'interface
                        // Elle sera supprimée de la base quand on cliquera sur "Sauvegarder"
                        questionsEnAttente.remove(question);
                        tableQuestionsExercice.setItems(questionsEnAttente);
                        showAlert("Info", "Question marquée pour suppression. Cliquez sur 'Sauvegarder en base' pour confirmer.");
                    } else {
                        // Nouvelle question non encore sauvegardée
                        questionsEnAttente.remove(question);
                        tableQuestionsExercice.setItems(questionsEnAttente);
                    }
                });
                // ===== FIN DU CODE À AJOUTER =====
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        tableQuestionsExercice.setEditable(true);
    }

    private void setupQuizQuestionsTable() {
        colNumQuestionQuiz.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        questionsQuizEnAttente.indexOf(cellData.getValue()) + 1).asObject());

        colTypeQuestionQuiz.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTypeQuestion()));

        colEnonceQuestionQuiz.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEnonce()));
        colEnonceQuestionQuiz.setCellFactory(TextFieldTableCell.forTableColumn());
        colEnonceQuestionQuiz.setOnEditCommit(event -> {
            event.getRowValue().setEnonce(event.getNewValue());
        });

        colCorrectionQuiz.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCorrection()));
        colCorrectionQuiz.setCellFactory(TextFieldTableCell.forTableColumn());
        colCorrectionQuiz.setOnEditCommit(event -> {
            event.getRowValue().setCorrection(event.getNewValue());
        });

        // Ajouter une colonne d'actions si elle n'existe pas déjà
        // Récupérer la dernière colonne (celle des actions)
        TableColumn<QuestionQuiz, Void> actionCol = (TableColumn<QuestionQuiz, Void>) tableQuestionsQuiz.getColumns().get(4);

        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("✏️");
            private final Button btnDelete = new Button("🗑️");
            private final HBox pane = new HBox(5, btnEdit, btnDelete);

            {
                btnEdit.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-cursor: hand;");
                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");

                btnEdit.setOnAction(event -> {
                    QuestionQuiz question = getTableView().getItems().get(getIndex());
                    ouvrirDialogueEditionQuestionQuiz(question);
                });

                btnDelete.setOnAction(event -> {
                    QuestionQuiz question = getTableView().getItems().get(getIndex());
                    if (question.getIdQuestion() > 0) {
                        // Question existe en base
                        questionsQuizEnAttente.remove(question);
                        tableQuestionsQuiz.setItems(questionsQuizEnAttente);
                        showAlert("Info", "Question marquée pour suppression. Cliquez sur 'Sauvegarder en base' pour confirmer.");
                    } else {
                        // Nouvelle question
                        questionsQuizEnAttente.remove(question);
                        tableQuestionsQuiz.setItems(questionsQuizEnAttente);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        tableQuestionsQuiz.setEditable(true);
    }

    @FXML
    public void nouvelExercice() {
        modeEditionExercice = false;
        exerciceEnEdition = null;
        lblModeExercice.setText("➡️ Nouvel Exercice");
        lblExerciceId.setText("ID: -");
        clearExerciceForm();
        questionsEnAttente.clear();
        tableQuestionsExercice.setItems(questionsEnAttente);
        btnSaveExercice.setText("💾 Créer l'exercice");
    }

    @FXML
    public void sauvegarderExercice() {
        if (!modeEditionExercice) {
            ajouterExerciceComplet();
        } else {
            modifierExerciceComplet();
        }
    }

    private int getPremierCoursId() {
        String query = "SELECT id_cours FROM cours LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt("id_cours");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @FXML
    public void ajouterExerciceComplet() {
        Connection conn = null;
        try {
            // Validation
            if (tfTitreExercice.getText().trim().isEmpty() ||
                    cbDifficulteExercice.getValue() == null ||
                    tfPointsExercice.getText().trim().isEmpty() ||
                    tfDureeExercice.getText().trim().isEmpty()) {
                showAlert("Attention", "Veuillez remplir tous les champs");
                return;
            }

            if (questionsEnAttente.isEmpty()) {
                showAlert("Attention", "Ajoutez au moins une question");
                return;
            }

            System.out.println("=== DÉBUT AJOUT EXERCICE ===");

            // 1. Récupérer l'ID du cours
            int idCours = getPremierCoursId();
            if (idCours == -1) {
                showAlert("Erreur", "Aucun cours trouvé");
                return;
            }

            // 2. Connexion et transaction
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 3. Insérer l'exercice
            String sqlExercice = "INSERT INTO exercice (id_cours, titre, description, difficulte, points_total, duree, est_actif) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtExercice = conn.prepareStatement(sqlExercice, Statement.RETURN_GENERATED_KEYS);
            stmtExercice.setInt(1, idCours);
            stmtExercice.setString(2, tfTitreExercice.getText().trim());
            stmtExercice.setString(3, taDescriptionExercice.getText().trim());
            stmtExercice.setString(4, cbDifficulteExercice.getValue());
            stmtExercice.setInt(5, Integer.parseInt(tfPointsExercice.getText().trim()));
            stmtExercice.setInt(6, Integer.parseInt(tfDureeExercice.getText().trim()));
            stmtExercice.setBoolean(7, true);

            stmtExercice.executeUpdate();

            ResultSet rsExercice = stmtExercice.getGeneratedKeys();
            int idExercice = -1;
            if (rsExercice.next()) {
                idExercice = rsExercice.getInt(1);
                System.out.println("✅ ID exercice: " + idExercice);
            } else {
                throw new SQLException("Impossible de récupérer l'ID de l'exercice");
            }

            // 4. Insérer les questions et corrections
            int ordre = 1;
            int questionsReussies = 0;
            for (QuestionExercice question : questionsEnAttente) {
                // Insérer la question
                String sqlQuestion = "INSERT INTO question_exercice (id_exercice, enonce, ordre) VALUES (?, ?, ?)";
                PreparedStatement stmtQuestion = conn.prepareStatement(sqlQuestion, Statement.RETURN_GENERATED_KEYS);
                stmtQuestion.setInt(1, idExercice);
                stmtQuestion.setString(2, question.getEnonce().trim());
                stmtQuestion.setInt(3, ordre);
                stmtQuestion.executeUpdate();

                ResultSet rsQuestion = stmtQuestion.getGeneratedKeys();
                if (rsQuestion.next()) {
                    int idQuestion = rsQuestion.getInt(1);
                    questionsReussies++;
                    System.out.println("  ✅ Question " + ordre + " ID: " + idQuestion);

                    // Insérer la correction
                    if (question.getCorrection() != null && !question.getCorrection().trim().isEmpty()) {
                        String sqlCorrection = "INSERT INTO correction_exercice (id_exercice, id_question_exercice, solution) VALUES (?, ?, ?)";
                        PreparedStatement stmtCorrection = conn.prepareStatement(sqlCorrection);
                        stmtCorrection.setInt(1, idExercice);
                        stmtCorrection.setInt(2, idQuestion);
                        stmtCorrection.setString(3, question.getCorrection().trim());
                        stmtCorrection.executeUpdate();
                        System.out.println("    ✅ Correction ajoutée");
                    }
                }
                ordre++;
            }

            // 5. Valider la transaction
            conn.commit();
            System.out.println("🎉 TRANSACTION RÉUSSIE! " + questionsReussies + "/" + questionsEnAttente.size() + " questions");

            // 6. Recharger et réinitialiser
            showAlert("Succès", "✅ Exercice ajouté avec " + questionsReussies + " questions");
            chargerExercices();
            nouvelExercice();

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("↩️ Rollback effectué");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("❌ Erreur SQL: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur SQL", e.getMessage());

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les points et la durée doivent être des nombres");
        }
    }

    @FXML
    public void modifierExerciceComplet() {
        if (exerciceEnEdition == null) return;

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Mettre à jour l'exercice
            String sqlUpdateExercice = "UPDATE exercice SET titre=?, description=?, difficulte=?, points_total=?, duree=? WHERE id_exercice=?";
            PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateExercice);
            stmtUpdate.setString(1, tfTitreExercice.getText().trim());
            stmtUpdate.setString(2, taDescriptionExercice.getText().trim());
            stmtUpdate.setString(3, cbDifficulteExercice.getValue());
            stmtUpdate.setInt(4, Integer.parseInt(tfPointsExercice.getText().trim()));
            stmtUpdate.setInt(5, Integer.parseInt(tfDureeExercice.getText().trim()));
            stmtUpdate.setInt(6, exerciceEnEdition.getIdExercice());
            stmtUpdate.executeUpdate();

            // 2. Supprimer les anciennes questions et corrections
            String sqlDeleteCorrections = "DELETE FROM correction_exercice WHERE id_exercice = ?";
            PreparedStatement stmtDelCorr = conn.prepareStatement(sqlDeleteCorrections);
            stmtDelCorr.setInt(1, exerciceEnEdition.getIdExercice());
            stmtDelCorr.executeUpdate();

            String sqlDeleteQuestions = "DELETE FROM question_exercice WHERE id_exercice = ?";
            PreparedStatement stmtDelQuest = conn.prepareStatement(sqlDeleteQuestions);
            stmtDelQuest.setInt(1, exerciceEnEdition.getIdExercice());
            stmtDelQuest.executeUpdate();

            // 3. Ajouter les nouvelles questions
            int ordre = 1;
            int questionsReussies = 0;
            for (QuestionExercice question : questionsEnAttente) {
                String sqlQuestion = "INSERT INTO question_exercice (id_exercice, enonce, ordre) VALUES (?, ?, ?)";
                PreparedStatement stmtQuestion = conn.prepareStatement(sqlQuestion, Statement.RETURN_GENERATED_KEYS);
                stmtQuestion.setInt(1, exerciceEnEdition.getIdExercice());
                stmtQuestion.setString(2, question.getEnonce().trim());
                stmtQuestion.setInt(3, ordre);
                stmtQuestion.executeUpdate();

                ResultSet rsQuestion = stmtQuestion.getGeneratedKeys();
                if (rsQuestion.next()) {
                    int idQuestion = rsQuestion.getInt(1);
                    questionsReussies++;

                    if (question.getCorrection() != null && !question.getCorrection().trim().isEmpty()) {
                        String sqlCorrection = "INSERT INTO correction_exercice (id_exercice, id_question_exercice, solution) VALUES (?, ?, ?)";
                        PreparedStatement stmtCorrection = conn.prepareStatement(sqlCorrection);
                        stmtCorrection.setInt(1, exerciceEnEdition.getIdExercice());
                        stmtCorrection.setInt(2, idQuestion);
                        stmtCorrection.setString(3, question.getCorrection().trim());
                        stmtCorrection.executeUpdate();
                    }
                }
                ordre++;
            }

            conn.commit();
            showAlert("Succès", "✅ Exercice modifié avec " + questionsReussies + " questions");
            chargerExercices();
            nouvelExercice();

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            showAlert("Erreur SQL", e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les points et la durée doivent être des nombres");
        }
    }

    @FXML
    public void ajouterQuestionAvecCorrection() {
        String question = taNouvelleQuestion.getText().trim();
        String correction = taNouvelleCorrection.getText().trim();

        if (question.isEmpty()) {
            showAlert("Attention", "Veuillez saisir l'énoncé de la question");
            return;
        }

        QuestionExercice q = new QuestionExercice();
        q.setEnonce(question);
        q.setOrdre(questionsEnAttente.size() + 1);
        q.setCorrection(correction);

        questionsEnAttente.add(q);
        tableQuestionsExercice.setItems(questionsEnAttente);
        taNouvelleQuestion.clear();
        taNouvelleCorrection.clear();

        showAlert("Succès", "✅ Question ajoutée à la liste");
    }

    private void ouvrirDialogueEditionQuestion(QuestionExercice question) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier la question");
        dialog.setHeaderText("Modification de la question");

        ButtonType modifierButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifierButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField enonceField = new TextField(question.getEnonce());
        enonceField.setPrefWidth(400);
        TextField correctionField = new TextField(question.getCorrection());
        correctionField.setPrefWidth(400);

        grid.add(new Label("Énoncé:"), 0, 0);
        grid.add(enonceField, 1, 0);
        grid.add(new Label("Correction:"), 0, 1);
        grid.add(correctionField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == modifierButtonType) {
                question.setEnonce(enonceField.getText().trim());
                question.setCorrection(correctionField.getText().trim());
                tableQuestionsExercice.refresh();
                showAlert("Succès", "Question modifiée");
            }
        });
    }

    @FXML
    public void annulerExercice() {
        nouvelExercice();
    }

    @FXML
    public void nouveauQuiz() {
        modeEditionQuiz = false;
        quizEnEdition = null;
        lblModeQuiz.setText("➡️ Nouveau Quiz");
        lblQuizId.setText("ID: -");
        clearQuizForm();
        questionsQuizEnAttente.clear();
        tableQuestionsQuiz.setItems(questionsQuizEnAttente);
        btnSaveQuiz.setText("💾 Créer le quiz");
    }

    @FXML
    public void sauvegarderQuiz() {
        if (!modeEditionQuiz) {
            ajouterQuizComplet();
        } else {
            modifierQuizComplet();
        }
    }
    @FXML
    public void ajouterQuizComplet() {
        try {
            if (tfTitreQuiz.getText().trim().isEmpty() ||
                    cbDifficulteQuiz.getValue() == null ||
                    tfPointsQuiz.getText().trim().isEmpty() ||
                    tfDureeQuiz.getText().trim().isEmpty()) {
                showAlert("Attention", "Veuillez remplir tous les champs");
                return;
            }

            int idCours = getPremierCoursId();
            if (idCours == -1) {
                showAlert("Erreur", "Aucun cours trouvé");
                return;
            }

            // Créer le quiz
            Quiz quiz = new Quiz(
                    idCours,
                    tfTitreQuiz.getText().trim(),
                    taDescriptionQuiz.getText().trim(),
                    cbDifficulteQuiz.getValue(),
                    Integer.parseInt(tfPointsQuiz.getText().trim()),
                    Integer.parseInt(tfDureeQuiz.getText().trim())
            );

            quizDAO.ajouter(quiz);

            if (quiz.getIdQuiz() <= 0) {
                showAlert("Erreur", "Impossible de créer le quiz");
                return;
            }

            // Sauvegarder les questions si elles existent
            int questionsSauvegardees = 0;
            for (QuestionQuiz question : questionsQuizEnAttente) {
                question.setIdQuiz(quiz.getIdQuiz());
                questionQuizDAO.ajouter(question);

                if (question.getCorrection() != null && !question.getCorrection().isEmpty()) {
                    CorrectionQuiz correction = new CorrectionQuiz(
                            question.getIdQuestion(),
                            question.getCorrection(),
                            true
                    );
                    correctionQuizDAO.ajouter(correction);
                }
                questionsSauvegardees++;
            }

            String message = "✅ Quiz ajouté avec succès";
            if (questionsSauvegardees > 0) {
                message += " (" + questionsSauvegardees + " questions)";
            }

            showAlert("Succès", message);
            chargerQuiz();
            nouveauQuiz();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les points et la durée doivent être des nombres");
        }
    }
    @FXML
    public void modifierQuizComplet() {
        if (quizEnEdition == null) return;

        try {
            quizEnEdition.setTitre(tfTitreQuiz.getText().trim());
            quizEnEdition.setDescription(taDescriptionQuiz.getText().trim());
            quizEnEdition.setDifficulte(cbDifficulteQuiz.getValue());
            quizEnEdition.setPointsTotal(Integer.parseInt(tfPointsQuiz.getText().trim()));
            quizEnEdition.setDureeTotale(Integer.parseInt(tfDureeQuiz.getText().trim()));

            quizDAO.modifier(quizEnEdition);

            // Mettre à jour les questions si nécessaire
            if (!questionsQuizEnAttente.isEmpty()) {
                for (QuestionQuiz question : questionsQuizEnAttente) {
                    if (question.getIdQuestion() == 0) {
                        // Nouvelle question à ajouter
                        question.setIdQuiz(quizEnEdition.getIdQuiz());
                        questionQuizDAO.ajouter(question);

                        if (question.getCorrection() != null && !question.getCorrection().isEmpty()) {
                            CorrectionQuiz correction = new CorrectionQuiz(
                                    question.getIdQuestion(),
                                    question.getCorrection(),
                                    true
                            );
                            correctionQuizDAO.ajouter(correction);
                        }
                    }
                }
            }

            showAlert("Succès", "✅ Quiz modifié");
            chargerQuiz();
            nouveauQuiz();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les points et la durée doivent être des nombres");
        }
    }
    @FXML
    public void ajouterQuestionQuizAvecCorrection() {
        try {
            System.out.println("=== AJOUT QUESTION QUIZ ===");

            // Récupérer les valeurs
            String enonce = taEnonceQuestionQuiz.getText().trim();
            String type = cbTypeQuestion.getValue();

            // TEST : valeurs par défaut si vides
            int points = 10;
            int temps = 30;
            String correction = "correction test";

            try {
                if (!tfPointsQuestion.getText().trim().isEmpty()) {
                    points = Integer.parseInt(tfPointsQuestion.getText().trim());
                }
                if (!tfTempsLimite.getText().trim().isEmpty()) {
                    temps = Integer.parseInt(tfTempsLimite.getText().trim());
                }
                if (!tfCorrectionQuiz.getText().trim().isEmpty()) {
                    correction = tfCorrectionQuiz.getText().trim();
                }
            } catch (NumberFormatException e) {
                // Garder les valeurs par défaut
            }

            if (enonce.isEmpty()) {
                enonce = "Question test"; // Valeur par défaut
            }

            if (type == null) {
                type = "qcm"; // Valeur par défaut
            }

            System.out.println("Création question: " + enonce + ", " + type + ", " + points + ", " + temps);

            // Créer et ajouter la question
            QuestionQuiz question = new QuestionQuiz();
            question.setEnonce(enonce);
            question.setTypeQuestion(type);
            question.setPoints(points);
            question.setTempsLimite(temps);
            question.setOrdre(questionsQuizEnAttente.size() + 1);
            question.setCorrection(correction);

            questionsQuizEnAttente.add(question);
            tableQuestionsQuiz.setItems(questionsQuizEnAttente);

            System.out.println("✅ Question ajoutée, total: " + questionsQuizEnAttente.size());

            // Rafraîchir le tableau
            tableQuestionsQuiz.refresh();

            // Vider les champs
            taEnonceQuestionQuiz.clear();
            tfPointsQuestion.clear();
            tfTempsLimite.clear();
            tfCorrectionQuiz.clear();

            showAlert("Succès", "✅ Question ajoutée (Total: " + questionsQuizEnAttente.size() + ")");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML
    public void annulerQuiz() {
        nouveauQuiz();
    }

    @FXML
    public void supprimerExercice() {
        Exercice exercice = listExercices.getSelectionModel().getSelectedItem();
        if (exercice == null) {
            showAlert("Attention", "Sélectionnez un exercice");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "🗑️ Supprimer l'exercice ?\n\nTitre: " + exercice.getTitre(),
                ButtonType.YES, ButtonType.NO);

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                exerciceDAO.supprimerAvecDependances(exercice.getIdExercice());
                chargerExercices();
                nouvelExercice();
                showAlert("Succès", "Exercice supprimé");
            }
        });
    }

    @FXML
    public void supprimerQuiz() {
        Quiz quiz = listQuiz.getSelectionModel().getSelectedItem();
        if (quiz == null) {
            showAlert("Attention", "Sélectionnez un quiz");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "🗑️ Supprimer le quiz ?\n\nTitre: " + quiz.getTitre(),
                ButtonType.YES, ButtonType.NO);

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                quizDAO.supprimerAvecDependances(quiz.getIdQuiz());
                chargerQuiz();
                nouveauQuiz();
                showAlert("Succès", "Quiz supprimé");
            }
        });
    }

    private void chargerExercices() {
        List<Exercice> exercices = exerciceDAO.getAll();
        listExercices.setItems(FXCollections.observableArrayList(exercices));
        if (totalExercicesLabel != null) totalExercicesLabel.setText(String.valueOf(exercices.size()));
        if (statTotalExercices != null) statTotalExercices.setText(String.valueOf(exercices.size()));
    }

    private void chargerQuiz() {
        List<Quiz> quizs = quizDAO.getAll();
        listQuiz.setItems(FXCollections.observableArrayList(quizs));
        if (totalQuizLabel != null) totalQuizLabel.setText(String.valueOf(quizs.size()));
        if (statTotalQuiz != null) statTotalQuiz.setText(String.valueOf(quizs.size()));
    }

    private void filterExercices(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            chargerExercices();
        } else {
            String search = searchText.toLowerCase();
            List<Exercice> filtered = exerciceDAO.getAll().stream()
                    .filter(e -> e.getTitre().toLowerCase().contains(search))
                    .collect(Collectors.toList());
            listExercices.setItems(FXCollections.observableArrayList(filtered));
        }
    }

    private void filterQuiz(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            chargerQuiz();
        } else {
            String search = searchText.toLowerCase();
            List<Quiz> filtered = quizDAO.getAll().stream()
                    .filter(q -> q.getTitre().toLowerCase().contains(search))
                    .collect(Collectors.toList());
            listQuiz.setItems(FXCollections.observableArrayList(filtered));
        }
    }

    private void updateStatistiques() {
        List<Exercice> exercices = exerciceDAO.getAll();
        List<Quiz> quizs = quizDAO.getAll();
        if (statTotalExercices != null) statTotalExercices.setText(String.valueOf(exercices.size()));
        if (statTotalQuiz != null) statTotalQuiz.setText(String.valueOf(quizs.size()));
        int totalQuestions = 0;
        for (Exercice ex : exercices) totalQuestions += questionExerciceDAO.getByExercice(ex.getIdExercice()).size();
        for (Quiz qz : quizs) totalQuestions += questionQuizDAO.getByQuiz(qz.getIdQuiz()).size();
        if (statTotalQuestions != null) statTotalQuestions.setText(String.valueOf(totalQuestions));
    }

    private void remplirFormulaireExercice(Exercice exercice) {
        tfTitreExercice.setText(exercice.getTitre());
        taDescriptionExercice.setText(exercice.getDescription());
        cbDifficulteExercice.setValue(exercice.getDifficulte());
        tfPointsExercice.setText(String.valueOf(exercice.getPointsTotal()));
        tfDureeExercice.setText(String.valueOf(exercice.getDuree()));
    }

    private void remplirFormulaireQuiz(Quiz quiz) {
        tfTitreQuiz.setText(quiz.getTitre());
        taDescriptionQuiz.setText(quiz.getDescription());
        cbDifficulteQuiz.setValue(quiz.getDifficulte());
        tfPointsQuiz.setText(String.valueOf(quiz.getPointsTotal()));
        tfDureeQuiz.setText(String.valueOf(quiz.getDureeTotale()));
    }

    private void clearExerciceForm() {
        tfTitreExercice.clear();
        taDescriptionExercice.clear();
        cbDifficulteExercice.setValue(null);
        tfPointsExercice.clear();
        tfDureeExercice.clear();
    }

    private void clearQuizForm() {
        tfTitreQuiz.clear();
        taDescriptionQuiz.clear();
        cbDifficulteQuiz.setValue(null);
        tfPointsQuiz.clear();
        tfDureeQuiz.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML public void ajouterExercice() { sauvegarderExercice(); }
    @FXML public void modifierExercice() { modifierExerciceComplet(); }
    @FXML public void consulterExercices() { chargerExercices(); }
    @FXML public void ajouterQuestionExercice() { ajouterQuestionAvecCorrection(); }
    @FXML public void ajouterQuiz() { sauvegarderQuiz(); }
    @FXML public void modifierQuiz() { modifierQuizComplet(); }
    @FXML public void consulterQuiz() { chargerQuiz(); }
    @FXML public void ajouterQuestionQuiz() { ajouterQuestionQuizAvecCorrection(); }
    @FXML public void rafraichirStatistiques() { updateStatistiques(); }
    // ========== MÉTHODES POUR CHARGER/SAUVEGARDER LES QUESTIONS ==========

    @FXML
    public void chargerQuestionsDepuisBase() {
        Exercice exercice = listExercices.getSelectionModel().getSelectedItem();
        if (exercice == null) {
            showAlert("Attention", "Sélectionnez d'abord un exercice");
            return;
        }

        try {
            List<QuestionExercice> questionsBase = questionExerciceDAO.getByExercice(exercice.getIdExercice());

            // Ajouter les corrections
            for (QuestionExercice q : questionsBase) {
                CorrectionExercice corr = correctionExerciceDAO.getByQuestion(q.getIdQuestionExercice());
                if (corr != null) {
                    q.setCorrection(corr.getSolution());
                }
            }

            questionsEnAttente.clear();
            questionsEnAttente.addAll(questionsBase);
            tableQuestionsExercice.setItems(questionsEnAttente);

            showAlert("Info", questionsBase.size() + " questions chargées depuis la base");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les questions: " + e.getMessage());
        }
    }
    @FXML
    public void sauvegarderQuestionsSeulement() {
        Exercice exercice = listExercices.getSelectionModel().getSelectedItem();

        if (exercice == null) {
            showAlert("Attention", "Veuillez d'abord sélectionner un exercice");
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            System.out.println("=== SAUVEGARDE DES QUESTIONS ===");
            System.out.println("Exercice ID: " + exercice.getIdExercice());
            System.out.println("Questions en attente: " + questionsEnAttente.size());

            // Récupérer les IDs des questions actuelles dans la base
            List<Integer> idsDansBase = new ArrayList<>();
            String sqlGetIds = "SELECT id_question_exercice FROM question_exercice WHERE id_exercice = ?";
            PreparedStatement getIdsStmt = conn.prepareStatement(sqlGetIds);
            getIdsStmt.setInt(1, exercice.getIdExercice());
            ResultSet rsIds = getIdsStmt.executeQuery();
            while (rsIds.next()) {
                idsDansBase.add(rsIds.getInt("id_question_exercice"));
            }

            // Supprimer les questions qui ne sont plus dans la liste
            for (int idDansBase : idsDansBase) {
                boolean existeDansListe = false;
                for (QuestionExercice q : questionsEnAttente) {
                    if (q.getIdQuestionExercice() == idDansBase) {
                        existeDansListe = true;
                        break;
                    }
                }

                if (!existeDansListe) {
                    // Supprimer la correction d'abord
                    String sqlDelCorr = "DELETE FROM correction_exercice WHERE id_question_exercice = ?";
                    PreparedStatement delCorr = conn.prepareStatement(sqlDelCorr);
                    delCorr.setInt(1, idDansBase);
                    delCorr.executeUpdate();
                    delCorr.close();

                    // Supprimer la question
                    String sqlDelQuest = "DELETE FROM question_exercice WHERE id_question_exercice = ?";
                    PreparedStatement delQuest = conn.prepareStatement(sqlDelQuest);
                    delQuest.setInt(1, idDansBase);
                    delQuest.executeUpdate();
                    delQuest.close();

                    System.out.println("🗑️ Question supprimée ID: " + idDansBase);
                }
            }

            // Ajouter ou mettre à jour les questions actuelles
            int reussites = 0;
            for (QuestionExercice question : questionsEnAttente) {
                if (question.getIdQuestionExercice() > 0) {
                    // Mise à jour de la question existante
                    String updateSql = "UPDATE question_exercice SET enonce = ?, ordre = ? WHERE id_question_exercice = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setString(1, question.getEnonce());
                    updateStmt.setInt(2, question.getOrdre());
                    updateStmt.setInt(3, question.getIdQuestionExercice());
                    updateStmt.executeUpdate();
                    updateStmt.close();

                    // Gérer la correction
                    if (question.getCorrection() != null && !question.getCorrection().isEmpty()) {
                        String checkCorrSql = "SELECT id_correction FROM correction_exercice WHERE id_question_exercice = ?";
                        PreparedStatement checkCorr = conn.prepareStatement(checkCorrSql);
                        checkCorr.setInt(1, question.getIdQuestionExercice());
                        ResultSet rs = checkCorr.executeQuery();

                        if (rs.next()) {
                            // Correction existe, mise à jour
                            String updateCorrSql = "UPDATE correction_exercice SET solution = ? WHERE id_question_exercice = ?";
                            PreparedStatement updateCorr = conn.prepareStatement(updateCorrSql);
                            updateCorr.setString(1, question.getCorrection());
                            updateCorr.setInt(2, question.getIdQuestionExercice());
                            updateCorr.executeUpdate();
                            updateCorr.close();
                        } else {
                            // Correction n'existe pas, insertion
                            String insertCorrSql = "INSERT INTO correction_exercice (id_exercice, id_question_exercice, solution) VALUES (?, ?, ?)";
                            PreparedStatement insertCorr = conn.prepareStatement(insertCorrSql);
                            insertCorr.setInt(1, exercice.getIdExercice());
                            insertCorr.setInt(2, question.getIdQuestionExercice());
                            insertCorr.setString(3, question.getCorrection());
                            insertCorr.executeUpdate();
                            insertCorr.close();
                        }
                        checkCorr.close();
                    }
                    System.out.println("✅ Question mise à jour ID: " + question.getIdQuestionExercice());

                } else {
                    // Nouvelle question
                    String insertSql = "INSERT INTO question_exercice (id_exercice, enonce, ordre) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    insertStmt.setInt(1, exercice.getIdExercice());
                    insertStmt.setString(2, question.getEnonce());
                    insertStmt.setInt(3, question.getOrdre());
                    insertStmt.executeUpdate();

                    ResultSet genKeys = insertStmt.getGeneratedKeys();
                    if (genKeys.next()) {
                        int idQuestion = genKeys.getInt(1);
                        question.setIdQuestionExercice(idQuestion);

                        if (question.getCorrection() != null && !question.getCorrection().isEmpty()) {
                            String insertCorrSql = "INSERT INTO correction_exercice (id_exercice, id_question_exercice, solution) VALUES (?, ?, ?)";
                            PreparedStatement insertCorr = conn.prepareStatement(insertCorrSql);
                            insertCorr.setInt(1, exercice.getIdExercice());
                            insertCorr.setInt(2, idQuestion);
                            insertCorr.setString(3, question.getCorrection());
                            insertCorr.executeUpdate();
                            insertCorr.close();
                        }
                        System.out.println("✅ Nouvelle question ID: " + idQuestion);
                    }
                    genKeys.close();
                    insertStmt.close();
                }
                reussites++;
            }

            conn.commit();
            System.out.println("🎉 " + reussites + " questions sauvegardées");

            // Fermer les statements
            getIdsStmt.close();

            // Recharger les questions depuis la base avec une NOUVELLE connexion
            List<QuestionExercice> nouvellesQuestions = questionExerciceDAO.getByExercice(exercice.getIdExercice());
            for (QuestionExercice q : nouvellesQuestions) {
                CorrectionExercice corr = correctionExerciceDAO.getByQuestion(q.getIdQuestionExercice());
                if (corr != null) q.setCorrection(corr.getSolution());
            }

            questionsEnAttente.clear();
            questionsEnAttente.addAll(nouvellesQuestions);
            tableQuestionsExercice.setItems(questionsEnAttente);

            showAlert("Succès", reussites + " questions sauvegardées");

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("↩️ Rollback effectué");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", e.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("🔌 Connexion fermée");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

// ========== MÉTHODES POUR LES QUESTIONS DE QUIZ ==========

    @FXML
    public void chargerQuestionsQuizDepuisBase() {
        Quiz quiz = listQuiz.getSelectionModel().getSelectedItem();
        if (quiz == null) {
            showAlert("Attention", "Sélectionnez d'abord un quiz");
            return;
        }

        List<QuestionQuiz> questionsBase = questionQuizDAO.getByQuiz(quiz.getIdQuiz());
        questionsQuizEnAttente.clear();
        questionsQuizEnAttente.addAll(questionsBase);
        tableQuestionsQuiz.setItems(FXCollections.observableArrayList(questionsQuizEnAttente));

        showAlert("Info", questionsBase.size() + " questions chargées depuis la base");
    }

    @FXML
    public void sauvegarderQuestionsQuizSeulement() {
        Quiz quiz = listQuiz.getSelectionModel().getSelectedItem();
        if (quiz == null) {
            showAlert("Attention", "Sélectionnez d'abord un quiz");
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            System.out.println("=== SAUVEGARDE DES QUESTIONS QUIZ ===");
            System.out.println("Quiz ID: " + quiz.getIdQuiz());
            System.out.println("Questions en attente: " + questionsQuizEnAttente.size());

            // Récupérer les IDs des questions actuelles dans la base
            List<Integer> idsDansBase = new ArrayList<>();
            String sqlGetIds = "SELECT id_question FROM question_quiz WHERE id_quiz = ?";
            PreparedStatement getIdsStmt = conn.prepareStatement(sqlGetIds);
            getIdsStmt.setInt(1, quiz.getIdQuiz());
            ResultSet rsIds = getIdsStmt.executeQuery();
            while (rsIds.next()) {
                idsDansBase.add(rsIds.getInt("id_question"));
            }

            // Supprimer les questions qui ne sont plus dans la liste
            for (int idDansBase : idsDansBase) {
                boolean existeDansListe = false;
                for (QuestionQuiz q : questionsQuizEnAttente) {
                    if (q.getIdQuestion() == idDansBase) {
                        existeDansListe = true;
                        break;
                    }
                }

                if (!existeDansListe) {
                    // Supprimer la correction d'abord
                    String sqlDelCorr = "DELETE FROM correction_quiz WHERE id_question = ?";
                    PreparedStatement delCorr = conn.prepareStatement(sqlDelCorr);
                    delCorr.setInt(1, idDansBase);
                    delCorr.executeUpdate();
                    delCorr.close();

                    // Supprimer la question
                    String sqlDelQuest = "DELETE FROM question_quiz WHERE id_question = ?";
                    PreparedStatement delQuest = conn.prepareStatement(sqlDelQuest);
                    delQuest.setInt(1, idDansBase);
                    delQuest.executeUpdate();
                    delQuest.close();

                    System.out.println("🗑️ Question supprimée ID: " + idDansBase);
                }
            }

            // Ajouter ou mettre à jour les questions actuelles
            int reussites = 0;
            for (QuestionQuiz question : questionsQuizEnAttente) {
                if (question.getIdQuestion() > 0) {
                    // Mise à jour de la question existante
                    String updateSql = "UPDATE question_quiz SET enonce = ?, type_question = ?, points = ?, temps_limite = ?, ordre = ? WHERE id_question = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setString(1, question.getEnonce());
                    updateStmt.setString(2, question.getTypeQuestion());
                    updateStmt.setInt(3, question.getPoints());
                    updateStmt.setInt(4, question.getTempsLimite());
                    updateStmt.setInt(5, question.getOrdre());
                    updateStmt.setInt(6, question.getIdQuestion());
                    updateStmt.executeUpdate();
                    updateStmt.close();

                    // Gérer la correction
                    if (question.getCorrection() != null && !question.getCorrection().isEmpty()) {
                        String checkCorrSql = "SELECT id_correction FROM correction_quiz WHERE id_question = ?";
                        PreparedStatement checkCorr = conn.prepareStatement(checkCorrSql);
                        checkCorr.setInt(1, question.getIdQuestion());
                        ResultSet rs = checkCorr.executeQuery();

                        if (rs.next()) {
                            // Correction existe, mise à jour
                            String updateCorrSql = "UPDATE correction_quiz SET contenu = ? WHERE id_question = ?";
                            PreparedStatement updateCorr = conn.prepareStatement(updateCorrSql);
                            updateCorr.setString(1, question.getCorrection());
                            updateCorr.setInt(2, question.getIdQuestion());
                            updateCorr.executeUpdate();
                            updateCorr.close();
                        } else {
                            // Correction n'existe pas, insertion
                            String insertCorrSql = "INSERT INTO correction_quiz (id_question, contenu, est_correcte) VALUES (?, ?, ?)";
                            PreparedStatement insertCorr = conn.prepareStatement(insertCorrSql);
                            insertCorr.setInt(1, question.getIdQuestion());
                            insertCorr.setString(2, question.getCorrection());
                            insertCorr.setBoolean(3, true);
                            insertCorr.executeUpdate();
                            insertCorr.close();
                        }
                        checkCorr.close();
                    }
                    System.out.println("✅ Question mise à jour ID: " + question.getIdQuestion());

                } else {
                    // Nouvelle question
                    String insertSql = "INSERT INTO question_quiz (id_quiz, enonce, type_question, points, temps_limite, ordre) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    insertStmt.setInt(1, quiz.getIdQuiz());
                    insertStmt.setString(2, question.getEnonce());
                    insertStmt.setString(3, question.getTypeQuestion());
                    insertStmt.setInt(4, question.getPoints());
                    insertStmt.setInt(5, question.getTempsLimite());
                    insertStmt.setInt(6, question.getOrdre());
                    insertStmt.executeUpdate();

                    ResultSet genKeys = insertStmt.getGeneratedKeys();
                    if (genKeys.next()) {
                        int idQuestion = genKeys.getInt(1);
                        question.setIdQuestion(idQuestion);

                        if (question.getCorrection() != null && !question.getCorrection().isEmpty()) {
                            String insertCorrSql = "INSERT INTO correction_quiz (id_question, contenu, est_correcte) VALUES (?, ?, ?)";
                            PreparedStatement insertCorr = conn.prepareStatement(insertCorrSql);
                            insertCorr.setInt(1, idQuestion);
                            insertCorr.setString(2, question.getCorrection());
                            insertCorr.setBoolean(3, true);
                            insertCorr.executeUpdate();
                            insertCorr.close();
                        }
                        System.out.println("✅ Nouvelle question ID: " + idQuestion);
                    }
                    insertStmt.close();
                }
                reussites++;
            }

            conn.commit();
            System.out.println("🎉 " + reussites + " questions sauvegardées");

            // Recharger les questions depuis la base
            List<QuestionQuiz> nouvellesQuestions = questionQuizDAO.getByQuiz(quiz.getIdQuiz());
            questionsQuizEnAttente.clear();
            questionsQuizEnAttente.addAll(nouvellesQuestions);
            tableQuestionsQuiz.setItems(questionsQuizEnAttente);

            showAlert("Succès", reussites + " questions sauvegardées");

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("↩️ Rollback effectué");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            showAlert("Erreur", e.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void testButton() {
        System.out.println("✅ Le bouton fonctionne!");
        showAlert("Test", "Le bouton est bien connecté");
    }
    private void ouvrirDialogueEditionQuestionQuiz(QuestionQuiz question) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier la question");
        dialog.setHeaderText("Modification de la question");

        ButtonType modifierButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifierButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField enonceField = new TextField(question.getEnonce());
        enonceField.setPrefWidth(400);

        ComboBox<String> typeField = new ComboBox<>();
        typeField.setItems(FXCollections.observableArrayList("qcm", "vrai_faux"));
        typeField.setValue(question.getTypeQuestion());

        TextField pointsField = new TextField(String.valueOf(question.getPoints()));
        TextField tempsField = new TextField(String.valueOf(question.getTempsLimite()));
        TextField correctionField = new TextField(question.getCorrection());

        grid.add(new Label("Énoncé:"), 0, 0);
        grid.add(enonceField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Points:"), 0, 2);
        grid.add(pointsField, 1, 2);
        grid.add(new Label("Temps (s):"), 0, 3);
        grid.add(tempsField, 1, 3);
        grid.add(new Label("Correction:"), 0, 4);
        grid.add(correctionField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == modifierButtonType) {
                try {
                    question.setEnonce(enonceField.getText().trim());
                    question.setTypeQuestion(typeField.getValue());
                    question.setPoints(Integer.parseInt(pointsField.getText().trim()));
                    question.setTempsLimite(Integer.parseInt(tempsField.getText().trim()));
                    question.setCorrection(correctionField.getText().trim());
                    tableQuestionsQuiz.refresh();
                    showAlert("Succès", "Question modifiée");
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Les points et le temps doivent être des nombres");
                }
            }
        });
    }
}