package model;

public class CorrectionExercice {
    private int idCorrection;
    private int idExercice;
    private Integer idQuestionExercice;
    private String solution;

    public CorrectionExercice() {}

    public CorrectionExercice(int idExercice, Integer idQuestionExercice, String solution) {
        this.idExercice = idExercice;
        this.idQuestionExercice = idQuestionExercice;
        this.solution = solution;
    }

    public int getIdCorrection() { return idCorrection; }
    public void setIdCorrection(int idCorrection) { this.idCorrection = idCorrection; }

    public int getIdExercice() { return idExercice; }
    public void setIdExercice(int idExercice) { this.idExercice = idExercice; }

    public Integer getIdQuestionExercice() { return idQuestionExercice; }
    public void setIdQuestionExercice(Integer idQuestionExercice) { this.idQuestionExercice = idQuestionExercice; }

    public String getSolution() { return solution; }
    public void setSolution(String solution) { this.solution = solution; }
}