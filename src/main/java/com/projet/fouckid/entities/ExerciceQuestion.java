package com.projet.fouckid.entities;


public class ExerciceQuestion {

    private int id;
    private int jeuId;
    private String consigne;
    private String reponseAttendue;
    private String niveau; // FACILE, MOYEN, DIFFICILE

    public ExerciceQuestion() {
    }

    public ExerciceQuestion(int jeuId, String consigne, String reponseAttendue, String niveau) {
        this.jeuId = jeuId;
        this.consigne = consigne;
        this.reponseAttendue = reponseAttendue;
        this.niveau = niveau;
    }

    public ExerciceQuestion(int id, int jeuId, String consigne, String reponseAttendue, String niveau) {
        this.id = id;
        this.jeuId = jeuId;
        this.consigne = consigne;
        this.reponseAttendue = reponseAttendue;
        this.niveau = niveau;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJeuId() { return jeuId; }
    public void setJeuId(int jeuId) { this.jeuId = jeuId; }

    public String getConsigne() { return consigne; }
    public void setConsigne(String consigne) { this.consigne = consigne; }

    public String getReponseAttendue() { return reponseAttendue; }
    public void setReponseAttendue(String reponseAttendue) { this.reponseAttendue = reponseAttendue; }

    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }

    @Override
    public String toString() {
        return "ExerciceQuestion{" +
                "id=" + id +
                ", jeuId=" + jeuId +
                ", consigne='" + consigne + '\'' +
                ", niveau='" + niveau + '\'' +
                '}';
    }
}