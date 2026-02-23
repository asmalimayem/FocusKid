package service;

import dao.*;
import model.*;
import java.util.List;

public class QuizService {

    private QuizDAO quizDAO = new QuizDAO();
    private QuestionQuizDAO questionDAO = new QuestionQuizDAO();
    private CorrectionQuizDAO correctionDAO = new CorrectionQuizDAO();
    private ReponseUserQuizDAO reponseDAO = new ReponseUserQuizDAO();

    // Gestion des quiz
    public void creerQuiz(Quiz quiz) {
        quizDAO.ajouter(quiz);
    }

    public void modifierQuiz(Quiz quiz) {
        quizDAO.modifier(quiz);
    }

    public void supprimerQuiz(int id) {
        quizDAO.supprimer(id);
    }

    public List<Quiz> getAllQuiz() {
        return quizDAO.getAll();
    }

    // Gestion des questions
    public void ajouterQuestion(QuestionQuiz question) {
        questionDAO.ajouter(question);
    }

    public List<QuestionQuiz> getQuestionsByQuiz(int idQuiz) {
        return questionDAO.getByQuiz(idQuiz);
    }

    // Gestion des corrections
    public void ajouterCorrections(int idQuestion, List<String> reponsesCorrectes) {
        // Supprimer les anciennes corrections
        correctionDAO.supprimerParQuestion(idQuestion);

        // Ajouter les nouvelles corrections
        for (String reponse : reponsesCorrectes) {
            CorrectionQuiz correction = new CorrectionQuiz(idQuestion, reponse, true);
            correctionDAO.ajouter(correction);
        }
    }

    public List<CorrectionQuiz> getCorrectionsByQuestion(int idQuestion) {
        return correctionDAO.getByQuestion(idQuestion);
    }

    // Gestion des réponses
    public void enregistrerReponse(ReponseUserQuiz reponse) {
        reponseDAO.ajouter(reponse);
    }

    public List<ReponseUserQuiz> getReponsesUser(int idUser, int idQuiz) {
        return reponseDAO.getByUserAndQuiz(idUser, idQuiz);
    }

    // Validation d'une réponse
    public boolean validerReponse(int idQuestion, String reponse) {
        List<CorrectionQuiz> corrections = correctionDAO.getByQuestion(idQuestion);
        for (CorrectionQuiz c : corrections) {
            if (c.getContenu().equalsIgnoreCase(reponse) && c.isEstCorrecte()) {
                return true;
            }
        }
        return false;
    }

    // Calcul du score
    public int calculerScore(int idUser, int idQuiz) {
        List<ReponseUserQuiz> reponses = reponseDAO.getByUserAndQuiz(idUser, idQuiz);
        int score = 0;
        for (ReponseUserQuiz r : reponses) {
            if (r.isEstCorrecte()) {
                QuestionQuiz q = questionDAO.getById(r.getIdQuestion());
                score += q.getPoints();
            }
        }
        return score;
    }
}