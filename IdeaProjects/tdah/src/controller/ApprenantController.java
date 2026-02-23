package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.collections.ObservableList;
import model.*;
import dao.*;
import service.*;

import java.util.List;

public class ApprenantController {

    @FXML private ListView<Exercice> listeExercices;
    @FXML private ListView<Quiz> listeQuiz;
    @FXML private TextArea affichageEnonce;
    @FXML private TextField tfReponse;
    @FXML private Label labelTitre;
    @FXML private Label labelDifficulte;
    @FXML private Label labelPoints;
    @FXML private Label labelDuree;
    @FXML private Button btnValider;
    @FXML private Button btnVoirCorrection;
    @FXML private TextArea affichageCorrection;

    private ExerciceDAO exerciceDAO = new ExerciceDAO();
    private QuizDAO quizDAO = new QuizDAO();
    private QuestionExerciceDAO questionExerciceDAO = new QuestionExerciceDAO();
    private QuestionQuizDAO questionQuizDAO = new QuestionQuizDAO();
    private CorrectionExerciceDAO correctionExerciceDAO = new CorrectionExerciceDAO();
    private CorrectionQuizDAO correctionQuizDAO = new CorrectionQuizDAO();
    private ReponseUserExerciceDAO reponseUserExerciceDAO = new ReponseUserExerciceDAO();
    private ReponseUserQuizDAO reponseUserQuizDAO = new ReponseUserQuizDAO();

    private int userId = 1; // À remplacer par l'utilisateur connecté
    private Exercice exerciceEnCours;
    private Quiz quizEnCours;
    private QuestionExercice questionExerciceEnCours;
    private QuestionQuiz questionQuizEnCours;
    private int indexQuestionActuelle = 0;
    private List<QuestionExercice> questionsExercice;
    private List<QuestionQuiz> questionsQuiz;

    @FXML
    public void initialize() {
        chargerExercices();
        chargerQuiz();
    }

    // ========== POUR LES EXERCICES ==========

    private void chargerExercices() {
        listeExercices.setItems(FXCollections.observableArrayList(exerciceDAO.getAll()));
    }

    @FXML
    public void selectionnerExercice() {
        exerciceEnCours = listeExercices.getSelectionModel().getSelectedItem();
        if (exerciceEnCours != null) {
            questionsExercice = questionExerciceDAO.getByExercice(exerciceEnCours.getIdExercice());
            indexQuestionActuelle = 0;
            quizEnCours = null;
            afficherQuestionExercice();
        }
    }

    private void afficherQuestionExercice() {
        if (questionsExercice != null && !questionsExercice.isEmpty() && indexQuestionActuelle < questionsExercice.size()) {
            questionExerciceEnCours = questionsExercice.get(indexQuestionActuelle);
            affichageEnonce.setText(questionExerciceEnCours.getEnonce());
            labelTitre.setText(exerciceEnCours.getTitre() + " - Question " + (indexQuestionActuelle + 1));
            labelDifficulte.setText("Difficulté: " + exerciceEnCours.getDifficulte());
            labelPoints.setText("Points: " + exerciceEnCours.getPointsTotal());
            labelDuree.setText("Durée: " + exerciceEnCours.getDuree() + "s");
            tfReponse.clear();
            btnValider.setVisible(true);
            btnVoirCorrection.setVisible(false);
            affichageCorrection.setVisible(false);
        }
    }

    @FXML
    public void validerReponseExercice() {
        if (questionExerciceEnCours != null) {
            ReponseUserExercice reponse = new ReponseUserExercice(
                    userId,
                    exerciceEnCours.getIdExercice(),
                    questionExerciceEnCours.getIdQuestionExercice(),
                    tfReponse.getText(),
                    0 // temps à calculer
            );

            // Vérifier si la réponse est correcte
            CorrectionExercice correction = correctionExerciceDAO.getByQuestion(questionExerciceEnCours.getIdQuestionExercice());
            if (correction != null && correction.getSolution().equalsIgnoreCase(tfReponse.getText())) {
                reponse.setEstCorrecte(true);
            }

            reponseUserExerciceDAO.ajouter(reponse);

            // Passer à la question suivante
            indexQuestionActuelle++;
            if (indexQuestionActuelle < questionsExercice.size()) {
                afficherQuestionExercice();
            } else {
                terminerExercice();
            }
        }
    }

    private void terminerExercice() {
        affichageEnonce.setText("Exercice terminé !");
        btnValider.setVisible(false);
        btnVoirCorrection.setVisible(true);
    }

    @FXML
    public void voirCorrectionExercice() {
        if (exerciceEnCours != null) {
            java.util.List<CorrectionExercice> corrections = correctionExerciceDAO.getByExercice(exerciceEnCours.getIdExercice());
            StringBuilder sb = new StringBuilder("CORRECTION DE L'EXERCICE:\n\n");
            for (CorrectionExercice c : corrections) {
                QuestionExercice q = questionExerciceDAO.getById(c.getIdQuestionExercice());
                sb.append("Question: ").append(q.getEnonce()).append("\n");
                sb.append("Réponse correcte: ").append(c.getSolution()).append("\n\n");
            }
            affichageCorrection.setText(sb.toString());
            affichageCorrection.setVisible(true);
        }
    }

    // ========== POUR LES QUIZ ==========

    private void chargerQuiz() {
        listeQuiz.setItems(FXCollections.observableArrayList(quizDAO.getAll()));
    }

    @FXML
    public void selectionnerQuiz() {
        quizEnCours = listeQuiz.getSelectionModel().getSelectedItem();
        if (quizEnCours != null) {
            questionsQuiz = questionQuizDAO.getByQuiz(quizEnCours.getIdQuiz());
            indexQuestionActuelle = 0;
            exerciceEnCours = null;
            afficherQuestionQuiz();
        }
    }

    private void afficherQuestionQuiz() {
        if (questionsQuiz != null && !questionsQuiz.isEmpty() && indexQuestionActuelle < questionsQuiz.size()) {
            questionQuizEnCours = questionsQuiz.get(indexQuestionActuelle);
            affichageEnonce.setText(questionQuizEnCours.getEnonce());
            labelTitre.setText(quizEnCours.getTitre() + " - Question " + (indexQuestionActuelle + 1));
            labelDifficulte.setText("Difficulté: " + quizEnCours.getDifficulte());
            labelPoints.setText("Points: " + questionQuizEnCours.getPoints());
            labelDuree.setText("Temps: " + questionQuizEnCours.getTempsLimite() + "s");
            tfReponse.clear();
            btnValider.setVisible(true);
            btnVoirCorrection.setVisible(false);
            affichageCorrection.setVisible(false);

            // Adapter l'interface selon le type de question
            if ("qcm".equals(questionQuizEnCours.getTypeQuestion())) {
                tfReponse.setPromptText("Entrez A, B, C ou D");
            } else if ("vrai_faux".equals(questionQuizEnCours.getTypeQuestion())) {
                tfReponse.setPromptText("Entrez 'vrai' ou 'faux'");
            }
        }
    }

    @FXML
    public void validerReponseQuiz() {
        if (questionQuizEnCours != null) {
            // Récupérer la correction
            java.util.List<CorrectionQuiz> corrections = correctionQuizDAO.getByQuestion(questionQuizEnCours.getIdQuestion());
            boolean correcte = false;
            int idCorrection = 0;

            for (CorrectionQuiz c : corrections) {
                if (c.getContenu().equalsIgnoreCase(tfReponse.getText())) {
                    correcte = c.isEstCorrecte();
                    idCorrection = c.getIdCorrection();
                    break;
                }
            }

            ReponseUserQuiz reponse = new ReponseUserQuiz(
                    userId,
                    quizEnCours.getIdQuiz(),
                    questionQuizEnCours.getIdQuestion(),
                    idCorrection,
                    correcte,
                    0 // temps à calculer
            );

            reponseUserQuizDAO.ajouter(reponse);

            // Passer à la question suivante
            indexQuestionActuelle++;
            if (indexQuestionActuelle < questionsQuiz.size()) {
                afficherQuestionQuiz();
            } else {
                terminerQuiz();
            }
        }
    }

    private void terminerQuiz() {
        affichageEnonce.setText("Quiz terminé !");
        btnValider.setVisible(false);
        btnVoirCorrection.setVisible(true);
    }

    @FXML
    public void voirCorrectionQuiz() {
        if (quizEnCours != null) {
            java.util.List<QuestionQuiz> questions = questionQuizDAO.getByQuiz(quizEnCours.getIdQuiz());
            StringBuilder sb = new StringBuilder("CORRECTION DU QUIZ:\n\n");

            for (QuestionQuiz q : questions) {
                sb.append("Question: ").append(q.getEnonce()).append("\n");
                java.util.List<CorrectionQuiz> corrections = correctionQuizDAO.getByQuestion(q.getIdQuestion());
                sb.append("Réponses correctes: ");
                for (CorrectionQuiz c : corrections) {
                    if (c.isEstCorrecte()) {
                        sb.append(c.getContenu()).append(" ");
                    }
                }
                sb.append("\n\n");
            }

            affichageCorrection.setText(sb.toString());
            affichageCorrection.setVisible(true);
        }
    }
}