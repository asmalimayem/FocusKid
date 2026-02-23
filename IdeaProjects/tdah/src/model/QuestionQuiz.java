package model;

public class QuestionQuiz {
    private int idQuestion;
    private int idQuiz;
    private String enonce;
    private String typeQuestion;
    private int points;
    private int tempsLimite;
    private int ordre;
    private String correction;

    public QuestionQuiz() {}

    public QuestionQuiz(int idQuiz, String enonce, String typeQuestion, int points, int tempsLimite, int ordre) {
        this.idQuiz = idQuiz;
        this.enonce = enonce;
        this.typeQuestion = typeQuestion;
        this.points = points;
        this.tempsLimite = tempsLimite;
        this.ordre = ordre;
    }

    public int getIdQuestion() { return idQuestion; }
    public void setIdQuestion(int idQuestion) { this.idQuestion = idQuestion; }

    public int getIdQuiz() { return idQuiz; }
    public void setIdQuiz(int idQuiz) { this.idQuiz = idQuiz; }

    public String getEnonce() { return enonce; }
    public void setEnonce(String enonce) { this.enonce = enonce; }

    public String getTypeQuestion() { return typeQuestion; }
    public void setTypeQuestion(String typeQuestion) { this.typeQuestion = typeQuestion; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getTempsLimite() { return tempsLimite; }
    public void setTempsLimite(int tempsLimite) { this.tempsLimite = tempsLimite; }

    public int getOrdre() { return ordre; }
    public void setOrdre(int ordre) { this.ordre = ordre; }

    public String getCorrection() { return correction; }
    public void setCorrection(String correction) { this.correction = correction; }
}