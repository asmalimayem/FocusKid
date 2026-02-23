package model;

public class Exercice {
    private int idExercice;
    private int idCours;
    private String titre;
    private String description;
    private String difficulte;
    private int pointsTotal;
    private int duree;
    private boolean estActif;

    public Exercice() {}

    public Exercice(int idCours, String titre, String description, String difficulte, int pointsTotal, int duree) {
        this.idCours = idCours;
        this.titre = titre;
        this.description = description;
        this.difficulte = difficulte;
        this.pointsTotal = pointsTotal;
        this.duree = duree;
        this.estActif = true;
    }

    public int getIdExercice() { return idExercice; }
    public void setIdExercice(int idExercice) { this.idExercice = idExercice; }

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

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    public boolean isEstActif() { return estActif; }
    public void setEstActif(boolean estActif) { this.estActif = estActif; }
}