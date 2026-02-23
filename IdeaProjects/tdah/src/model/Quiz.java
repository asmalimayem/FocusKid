package model;

public class Quiz {
    private int idQuiz;
    private int idCours;
    private String titre;
    private String description;
    private String difficulte;
    private int pointsTotal;
    private int dureeTotale;
    private boolean estActif;

    public Quiz() {}

    public Quiz(int idCours, String titre, String description, String difficulte, int pointsTotal, int dureeTotale) {
        this.idCours = idCours;
        this.titre = titre;
        this.description = description;
        this.difficulte = difficulte;
        this.pointsTotal = pointsTotal;
        this.dureeTotale = dureeTotale;
        this.estActif = true;
    }

    public int getIdQuiz() { return idQuiz; }
    public void setIdQuiz(int idQuiz) { this.idQuiz = idQuiz; }

    public int getIdCours() { return idCours; }
    public void setIdCours(int idCours) { this.idCours = idCours; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulte() { return difficulte; }
    public void setDifficulte(String difficulte) { this.difficulte = difficulte; }

    public int getPointsTotal() { return pointsTotal; }
    public void setPointsTotal(int pointsTotal) { this.pointsTotal = pointsTotal; }

    public int getDureeTotale() { return dureeTotale; }
    public void setDureeTotale(int dureeTotale) { this.dureeTotale = dureeTotale; }

    public boolean isEstActif() { return estActif; }
    public void setEstActif(boolean estActif) { this.estActif = estActif; }
}