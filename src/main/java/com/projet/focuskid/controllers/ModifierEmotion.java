package com.projet.focuskid.controllers;

import com.projet.focuskid.entities.Emotion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.projet.focuskid.services.ServiceEmotion;

import java.sql.SQLException;

public class ModifierEmotion {

    @FXML
    private TextField txtid;

    @FXML
    private TextField txtnom;

    @FXML
    private TextField txticone;

    @FXML
    void modifierEmotion(ActionEvent event) {

        int id = Integer.parseInt(txtid.getText());
        String nom = txtnom.getText();
        String icone = txticone.getText();

        Emotion emotion = new Emotion(id, nom, icone);
        ServiceEmotion se = new ServiceEmotion();

        try {
            se.modifier(emotion);
            System.out.println("Emotion modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}