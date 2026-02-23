package model;

public class CorrectionQuiz {
    private int idCorrection;
    private int idQuestion;
    private String contenu;
    private boolean estCorrecte;

    public CorrectionQuiz() {}

    public CorrectionQuiz(int idQuestion, String contenu, boolean estCorrecte) {
        this.idQuestion = idQuestion;
        this.contenu = contenu;
        this.estCorrecte = estCorrecte;
    }

    public int getIdCorrection() { return idCorrection; }
    public void setIdCorrection(int idCorrection) { this.idCorrection = idCorrection; }

    public int getIdQuestion() { return idQuestion; }
    public void setIdQuestion(int idQuestion) { this.idQuestion = idQuestion; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public boolean isEstCorrecte() { return estCorrecte; }
    public void setEstCorrecte(boolean estCorrecte) { this.estCorrecte = estCorrecte; }
}