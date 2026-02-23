package model;

import java.time.LocalDateTime;

public class ReponseUserExercice {
    private int idRepUser;
    private int idUser;
    private int idExercice;
    private Integer idQuestionExercice;
    private String reponse;
    private boolean estCorrecte;
    private int tempsReponse; // en secondes
    private LocalDateTime dateReponse;

    public ReponseUserExercice() {}

    public ReponseUserExercice(int idUser, int idExercice, Integer idQuestionExercice,
                               String reponse, int tempsReponse) {
        this.idUser = idUser;
        this.idExercice = idExercice;
        this.idQuestionExercice = idQuestionExercice;
        this.reponse = reponse;
        this.tempsReponse = tempsReponse;
        this.dateReponse = LocalDateTime.now();
    }

    // Getters et Setters
    public int getIdRepUser() { return idRepUser; }
    public void setIdRepUser(int idRepUser) { this.idRepUser = idRepUser; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public int getIdExercice() { return idExercice; }
    public void setIdExercice(int idExercice) { this.idExercice = idExercice; }

    public Integer getIdQuestionExercice() { return idQuestionExercice; }
    public void setIdQuestionExercice(Integer idQuestionExercice) { this.idQuestionExercice = idQuestionExercice; }

    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }

    public boolean isEstCorrecte() { return estCorrecte; }
    public void setEstCorrecte(boolean estCorrecte) { this.estCorrecte = estCorrecte; }

    public int getTempsReponse() { return tempsReponse; }
    public void setTempsReponse(int tempsReponse) { this.tempsReponse = tempsReponse; }

    public LocalDateTime getDateReponse() { return dateReponse; }
    public void setDateReponse(LocalDateTime dateReponse) { this.dateReponse = dateReponse; }
}