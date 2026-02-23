package service;

import dao.*;
import model.*;
import java.util.List;

public class ExerciceService {

    private ExerciceDAO exerciceDAO = new ExerciceDAO();
    private QuestionExerciceDAO questionDAO = new QuestionExerciceDAO();
    private CorrectionExerciceDAO correctionDAO = new CorrectionExerciceDAO();
    private ReponseUserExerciceDAO reponseDAO = new ReponseUserExerciceDAO();

    // Gestion des exercices
    public void creerExercice(Exercice exercice) {
        exerciceDAO.ajouter(exercice);
    }

    public void modifierExercice(Exercice exercice) {
        exerciceDAO.modifier(exercice);
    }

    public void supprimerExercice(int id) {
        exerciceDAO.supprimer(id);
    }

    public List<Exercice> getAllExercices() {
        return exerciceDAO.getAll();
    }

    // Gestion des questions
    public void ajouterQuestion(QuestionExercice question) {
        questionDAO.ajouter(question);
    }

    public List<QuestionExercice> getQuestionsByExercice(int idExercice) {
        return questionDAO.getByExercice(idExercice);
    }

    // Gestion des corrections
    public void ajouterCorrection(CorrectionExercice correction) {
        correctionDAO.ajouter(correction);
    }

    public CorrectionExercice getCorrectionByQuestion(int idQuestion) {
        return correctionDAO.getByQuestion(idQuestion);
    }

    // Gestion des réponses
    public void enregistrerReponse(ReponseUserExercice reponse) {
        // Vérifier si la réponse est correcte
        CorrectionExercice correction = correctionDAO.getByQuestion(reponse.getIdQuestionExercice());
        if (correction != null && correction.getSolution().equalsIgnoreCase(reponse.getReponse())) {
            reponse.setEstCorrecte(true);
        }
        reponseDAO.ajouter(reponse);
    }

    public List<ReponseUserExercice> getReponsesUser(int idUser, int idExercice) {
        return reponseDAO.getByUserAndExercice(idUser, idExercice);
    }

    // Calcul du score
    public int calculerScore(int idUser, int idExercice) {
        List<ReponseUserExercice> reponses = reponseDAO.getByUserAndExercice(idUser, idExercice);
        int score = 0;
        for (ReponseUserExercice r : reponses) {
            if (r.isEstCorrecte()) {
                score += 10; // Ou selon votre système de points
            }
        }
        return score;
    }
}