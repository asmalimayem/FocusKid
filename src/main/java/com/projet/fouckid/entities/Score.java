package com.projet.fouckid.entities;


import java.sql.Timestamp;

public class Score {

    private int id;
    private int utilisateurId;
    private int jeuId;
    private int points;
    private Timestamp datePartie;

    public Score() {
    }

    public Score(int utilisateurId, int jeuId, int points) {
        this.utilisateurId = utilisateurId;
        this.jeuId = jeuId;
        this.points = points;
    }

    public Score(int id, int utilisateurId, int jeuId, int points, Timestamp datePartie) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.jeuId = jeuId;
        this.points = points;
        this.datePartie = datePartie;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }

    public int getJeuId() { return jeuId; }
    public void setJeuId(int jeuId) { this.jeuId = jeuId; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public Timestamp getDatePartie() { return datePartie; }
    public void setDatePartie(Timestamp datePartie) { this.datePartie = datePartie; }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", utilisateurId=" + utilisateurId +
                ", jeuId=" + jeuId +
                ", points=" + points +
                ", datePartie=" + datePartie +
                '}';
    }
}
