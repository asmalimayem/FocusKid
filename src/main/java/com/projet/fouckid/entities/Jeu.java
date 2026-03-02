package com.projet.fouckid.entities;


public class Jeu {

    private int id;
    private String titre;
    private String type; // QUIZ ou EXERCICE
    private String niveau; // FACILE, MOYEN, DIFFICILE

    public Jeu() {
    }

    public Jeu(String titre, String type, String niveau) {
        this.titre = titre;
        this.type = type;
        this.niveau = niveau;
    }

    public Jeu(int id, String titre, String type, String niveau) {
        this.id = id;
        this.titre = titre;
        this.type = type;
        this.niveau = niveau;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }

    @Override
    public String toString() {
        return "Jeu{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", type='" + type + '\'' +
                ", niveau='" + niveau + '\'' +
                '}';
    }
}