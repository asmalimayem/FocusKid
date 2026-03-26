package tn.edu.esprit.entities;

import java.sql.Date;
import java.sql.Time;

public class CarnetEducatif {

    private int id;

    // A. Informations générales
    private Date dateEtude;
    private Time heureDebut;
    private Time heureFin;
    private int dureeTotale;
    private String lieu;

    // B. Activité
    private String matiere;
    private String typeActivite;
    private String niveauDifficulte;

    // C. Comportement & concentration
    private int niveauConcentration;
    private int niveauAgitation;
    private int nombreInterruptions;
    private int tempsAvantPerteConcentration;

    // D. Autonomie
    private boolean travailleSeul;
    private boolean demandeAide;
    private int niveauAutonomie;

    // E. Résultat
    private boolean travailTermine;
    private String difficultes;
    private String pointsPositifs;

    public CarnetEducatif() {
    }

    public CarnetEducatif(Date dateEtude, Time heureDebut, Time heureFin, int dureeTotale, String lieu,
            String matiere, String typeActivite, String niveauDifficulte,
            int niveauConcentration, int niveauAgitation, int nombreInterruptions, int tempsAvantPerteConcentration,
            boolean travailleSeul, boolean demandeAide, int niveauAutonomie,
            boolean travailTermine, String difficultes, String pointsPositifs) {

        this.dateEtude = dateEtude;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.dureeTotale = dureeTotale;
        this.lieu = lieu;
        this.matiere = matiere;
        this.typeActivite = typeActivite;
        this.niveauDifficulte = niveauDifficulte;
        this.niveauConcentration = niveauConcentration;
        this.niveauAgitation = niveauAgitation;
        this.nombreInterruptions = nombreInterruptions;
        this.tempsAvantPerteConcentration = tempsAvantPerteConcentration;
        this.travailleSeul = travailleSeul;
        this.demandeAide = demandeAide;
        this.niveauAutonomie = niveauAutonomie;
        this.travailTermine = travailTermine;
        this.difficultes = difficultes;
        this.pointsPositifs = pointsPositifs;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDateEtude() { return dateEtude; }
    public void setDateEtude(Date dateEtude) { this.dateEtude = dateEtude; }

    public Time getHeureDebut() { return heureDebut; }
    public void setHeureDebut(Time heureDebut) { this.heureDebut = heureDebut; }

    public Time getHeureFin() { return heureFin; }
    public void setHeureFin(Time heureFin) { this.heureFin = heureFin; }

    public int getDureeTotale() { return dureeTotale; }
    public void setDureeTotale(int dureeTotale) { this.dureeTotale = dureeTotale; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getMatiere() { return matiere; }
    public void setMatiere(String matiere) { this.matiere = matiere; }

    public String getTypeActivite() { return typeActivite; }
    public void setTypeActivite(String typeActivite) { this.typeActivite = typeActivite; }

    public String getNiveauDifficulte() { return niveauDifficulte; }
    public void setNiveauDifficulte(String niveauDifficulte) { this.niveauDifficulte = niveauDifficulte; }

    public int getNiveauConcentration() { return niveauConcentration; }
    public void setNiveauConcentration(int niveauConcentration) { this.niveauConcentration = niveauConcentration; }

    public int getNiveauAgitation() { return niveauAgitation; }
    public void setNiveauAgitation(int niveauAgitation) { this.niveauAgitation = niveauAgitation; }

    public int getNombreInterruptions() { return nombreInterruptions; }
    public void setNombreInterruptions(int nombreInterruptions) { this.nombreInterruptions = nombreInterruptions; }

    public int getTempsAvantPerteConcentration() { return tempsAvantPerteConcentration; }
    public void setTempsAvantPerteConcentration(int tempsAvantPerteConcentration) { this.tempsAvantPerteConcentration = tempsAvantPerteConcentration; }

    public boolean isTravailleSeul() { return travailleSeul; }
    public void setTravailleSeul(boolean travailleSeul) { this.travailleSeul = travailleSeul; }

    public boolean isDemandeAide() { return demandeAide; }
    public void setDemandeAide(boolean demandeAide) { this.demandeAide = demandeAide; }

    public int getNiveauAutonomie() { return niveauAutonomie; }
    public void setNiveauAutonomie(int niveauAutonomie) { this.niveauAutonomie = niveauAutonomie; }

    public boolean isTravailTermine() { return travailTermine; }
    public void setTravailTermine(boolean travailTermine) { this.travailTermine = travailTermine; }

    public String getDifficultes() { return difficultes; }
    public void setDifficultes(String difficultes) { this.difficultes = difficultes; }

    public String getPointsPositifs() { return pointsPositifs; }
    public void setPointsPositifs(String pointsPositifs) { this.pointsPositifs = pointsPositifs; }

    @Override
    public String toString() {
        return "CarnetEducatif{" +
                "id=" + id +
                ", dateEtude=" + dateEtude +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", dureeTotale=" + dureeTotale +
                ", lieu='" + lieu + '\'' +
                ", matiere='" + matiere + '\'' +
                ", typeActivite='" + typeActivite + '\'' +
                ", niveauDifficulte='" + niveauDifficulte + '\'' +
                ", niveauConcentration=" + niveauConcentration +
                ", niveauAgitation=" + niveauAgitation +
                ", nombreInterruptions=" + nombreInterruptions +
                ", tempsAvantPerteConcentration=" + tempsAvantPerteConcentration +
                ", travailleSeul=" + travailleSeul +
                ", demandeAide=" + demandeAide +
                ", niveauAutonomie=" + niveauAutonomie +
                ", travailTermine=" + travailTermine +
                ", difficultes='" + difficultes + '\'' +
                ", pointsPositifs='" + pointsPositifs + '\'' +
                '}';
    }
}
