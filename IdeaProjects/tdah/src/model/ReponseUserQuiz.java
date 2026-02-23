package model;

import java.time.LocalDateTime;

public class ReponseUserQuiz {
    private int idRepUser;
    private int idUser;
    private int idQuiz;
    private int idQuestion;
    private int idCorrection;
    private boolean estCorrecte;
    private int tempsReponse; // en secondes
    private LocalDateTime dateReponse;

    public ReponseUserQuiz() {}

    public ReponseUserQuiz(int idUser, int idQuiz, int idQuestion,
                           int idCorrection, boolean estCorrecte, int tempsReponse) {
        this.idUser = idUser;
        this.idQuiz = idQuiz;
        this.idQuestion = idQuestion;
        this.idCorrection = idCorrection;
        this.estCorrecte = estCorrecte;
        this.tempsReponse = tempsReponse;
        this.dateReponse = LocalDateTime.now();
    }

    // Getters et Setters
    public int getIdRepUser() { return idRepUser; }
    public void setIdRepUser(int idRepUser) { this.idRepUser = idRepUser; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public int getIdQuiz() { return idQuiz; }
    public void setIdQuiz(int idQuiz) { this.idQuiz = idQuiz; }

    public int getIdQuestion() { return idQuestion; }
    public void setIdQuestion(int idQuestion) { this.idQuestion = idQuestion; }

    public int getIdCorrection() { return idCorrection; }
    public void setIdCorrection(int idCorrection) { this.idCorrection = idCorrection; }

    public boolean isEstCorrecte() { return estCorrecte; }
    public void setEstCorrecte(boolean estCorrecte) { this.estCorrecte = estCorrecte; }

    public int getTempsReponse() { return tempsReponse; }
    public void setTempsReponse(int tempsReponse) { this.tempsReponse = tempsReponse; }

    public LocalDateTime getDateReponse() { return dateReponse; }
    public void setDateReponse(LocalDateTime dateReponse) { this.dateReponse = dateReponse; }
}