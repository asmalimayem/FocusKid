package com.projet.focuskid.entities;

public class Scenario {
    private int id;
    private String titre;
    private String description;
    private String animationUrl;
    private int emotionId;


    public Scenario() {
    }


    public Scenario(String titre, String description, String animationUrl, int emotionId) {
        this.titre = titre;
        this.description = description;
        this.animationUrl = animationUrl;
        this.emotionId = emotionId;
    }


    public Scenario(int id, String titre, String description, String animationUrl, int emotionId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.animationUrl = animationUrl;
        this.emotionId = emotionId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnimationUrl() {
        return animationUrl;
    }

    public void setAnimationUrl(String animationUrl) {
        this.animationUrl = animationUrl;
    }

    public int getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }


    @Override
    public String toString() {
        return "Scenario{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", emotionId=" + emotionId +
                '}';
    }


    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Scenario other = (Scenario) obj;
        return this.id == other.id;
    }
}
