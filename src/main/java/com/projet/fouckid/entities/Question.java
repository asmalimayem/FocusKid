package com.projet.fouckid.entities;

public class Question {

    private int id;
    private int jeuId;  // Correspond à jeu_id dans la table
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String bonneReponse;  // Correspond à bonne_reponse (CHAR(1))

    public Question() {
    }

    public Question(int jeuId, String questionText, String optionA,
                    String optionB, String optionC, String bonneReponse) {
        this.jeuId = jeuId;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.bonneReponse = bonneReponse;
    }

    public Question(int id, int jeuId, String questionText, String optionA,
                    String optionB, String optionC, String bonneReponse) {
        this.id = id;
        this.jeuId = jeuId;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.bonneReponse = bonneReponse;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJeuId() { return jeuId; }
    public void setJeuId(int jeuId) { this.jeuId = jeuId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }

    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }

    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }

    public String getBonneReponse() { return bonneReponse; }
    public void setBonneReponse(String bonneReponse) { this.bonneReponse = bonneReponse; }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", jeuId=" + jeuId +
                ", questionText='" + questionText + '\'' +
                ", bonneReponse='" + bonneReponse + '\'' +
                '}';
    }
}