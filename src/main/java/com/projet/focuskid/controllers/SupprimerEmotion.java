package com.projet.focuskid.controllers;

import com.projet.focuskid.entities.Emotion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.projet.focuskid.services.ServiceEmotion;

import java.sql.SQLException;

public class SupprimerEmotion {

    @FXML
    private TextField txtid;

    @FXML
    void supprimerEmotion(ActionEvent event) {

        int id = Integer.parseInt(txtid.getText());

        Emotion emotion = new Emotion();
        emotion.setId(id);

        ServiceEmotion se = new ServiceEmotion();

        try {
            se.supprimer(emotion);
            System.out.println("Emotion supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}