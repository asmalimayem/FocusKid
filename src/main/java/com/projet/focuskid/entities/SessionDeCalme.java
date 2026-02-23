package com.projet.focuskid.entities;

import java.sql.Timestamp;
import java.util.Objects;

public class SessionDeCalme {
    private int id;
    private int enfantId;
    private String typeActivite; // respiration, musique, coloriage, etc.
    private String declencheur;   // enfant, ia_detection, parent
    private int etatInitialId;
    private int etatFinalId;
    private int dureePrevue;     // en secondes
    private int dureeReelle;     // en secondes
    private Timestamp horodatage;
    private int feedbackEnfant;  // de 1 à 5
    private String noteParent;

    // Constructeur vide
    public SessionDeCalme() {
    }

    // Constructeur sans ID (utile pour l'insertion/CREATE)
    public SessionDeCalme(int enfantId, String typeActivite, String declencheur, int dureePrevue, int dureeReelle) {
        this.enfantId = enfantId;
        this.typeActivite = typeActivite;
        this.declencheur = declencheur;
        this.dureePrevue = dureePrevue;
        this.dureeReelle = dureeReelle;
    }

    // Constructeur complet (utile pour la lecture/READ)
    public SessionDeCalme(int id, int enfantId, String typeActivite, String declencheur, int etatInitialId, int etatFinalId, int dureePrevue, int dureeReelle, Timestamp horodatage, int feedbackEnfant, String noteParent) {
        this.id = id;
        this.enfantId = enfantId;
        this.typeActivite = typeActivite;
        this.declencheur = declencheur;
        this.etatInitialId = etatInitialId;
        this.etatFinalId = etatFinalId;
        this.dureePrevue = dureePrevue;
        this.dureeReelle = dureeReelle;
        this.horodatage = horodatage;
        this.feedbackEnfant = feedbackEnfant;
        this.noteParent = noteParent;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEnfantId() { return enfantId; }
    public void setEnfantId(int enfantId) { this.enfantId = enfantId; }

    public String getTypeActivite() { return typeActivite; }
    public void setTypeActivite(String typeActivite) { this.typeActivite = typeActivite; }

    public String getDeclencheur1() { return declencheur; }
    public void setDeclencheur(String declencheur) { this.declencheur = declencheur; }

    public int getEtatInitialId() { return etatInitialId; }
    public void setEtatInitialId(int etatInitialId) { this.etatInitialId = etatInitialId; }

    public int getEtatFinalId() { return etatFinalId; }
    public void setEtatFinalId(int etatFinalId) { this.etatFinalId = etatFinalId; }

    public int getDureePrevue() { return dureePrevue; }
    public void setDureePrevue(int dureePrevue) { this.dureePrevue = dureePrevue; }

    public int getDureeReelle() { return dureeReelle; }
    public void setDureeReelle(int dureeReelle) { this.dureeReelle = dureeReelle; }

    public Timestamp getHorodatage() { return horodatage; }
    public void setHorodatage(Timestamp horodatage) { this.horodatage = horodatage; }

    public int getFeedbackEnfant() { return feedbackEnfant; }
    public void setFeedbackEnfant(int feedbackEnfant) { this.feedbackEnfant = feedbackEnfant; }

    public String getNoteParent() { return noteParent; }
    public void setNoteParent(String noteParent) { this.noteParent = noteParent; }

    @Override
    public String toString() {
        return "SessionDeCalme{" + "id=" + id + ", type=" + typeActivite + ", duree=" + dureeReelle + "s, score=" + feedbackEnfant + "/5}";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final SessionDeCalme other = (SessionDeCalme) obj;
        return this.id == other.id;
    }


}

