package com.projet.focuskid.entities;

public class Emotion {
    private int id;
    private String nom;
    private String icone;

    public Emotion(){

    }

    public Emotion(String nom, String icone) {
        this.nom = nom;
        this.icone = icone;
    }

    public Emotion(int id, String nom, String icone) {
        this.id = id;
        this.nom = nom;
        this.icone = icone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    @Override
    public String toString() {
        return "Personne{" + "nom=" + nom + ", icone=" + icone + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Emotion other = (Emotion) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public void setIcone1(String string) {
        // TODO Auto-generated method stub

    }
}
