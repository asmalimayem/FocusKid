package tn.edu.esprit.entities;

import java.sql.Date;
import java.sql.Timestamp;

public class Commentaire {

    private int id;
    private int carnetId;
    private Date dateCommentaire;
    private String texteCommentaire;
    private String typeCommentaire;
    private Timestamp createdAt;

    public Commentaire() {}

    public Commentaire(int carnetId, Date dateCommentaire,
                       String texteCommentaire, String typeCommentaire) {
        this.carnetId = carnetId;
        this.dateCommentaire = dateCommentaire;
        this.texteCommentaire = texteCommentaire;
        this.typeCommentaire = typeCommentaire;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCarnetId() { return carnetId; }
    public void setCarnetId(int carnetId) { this.carnetId = carnetId; }

    public Date getDateCommentaire() { return dateCommentaire; }
    public void setDateCommentaire(Date dateCommentaire) { this.dateCommentaire = dateCommentaire; }

    public String getTexteCommentaire() { return texteCommentaire; }
    public void setTexteCommentaire(String texteCommentaire) { this.texteCommentaire = texteCommentaire; }

    public String getTypeCommentaire() { return typeCommentaire; }
    public void setTypeCommentaire(String typeCommentaire) { this.typeCommentaire = typeCommentaire; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return typeCommentaire + " - " + dateCommentaire;
    }
}