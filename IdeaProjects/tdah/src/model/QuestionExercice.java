package model;

public class QuestionExercice {
    private int idQuestionExercice;
    private int idExercice;
    private String enonce;
    private int ordre;
    private String correction;

    public QuestionExercice() {}

    public QuestionExercice(int idExercice, String enonce, int ordre) {
        this.idExercice = idExercice;
        this.enonce = enonce;
        this.ordre = ordre;
    }

    public int getIdQuestionExercice() { return idQuestionExercice; }
    public void setIdQuestionExercice(int idQuestionExercice) { this.idQuestionExercice = idQuestionExercice; }

    public int getIdExercice() { return idExercice; }
    public void setIdExercice(int idExercice) { this.idExercice = idExercice; }

    public String getEnonce() { return enonce; }
    public void setEnonce(String enonce) { this.enonce = enonce; }

    public int getOrdre() { return ordre; }
    public void setOrdre(int ordre) { this.ordre = ordre; }

    public String getCorrection() { return correction; }
    public void setCorrection(String correction) { this.correction = correction; }
}