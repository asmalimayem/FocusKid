package com.projet.focuskid.controllers;

import com.projet.focuskid.entities.Emotion;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import com.projet.focuskid.services.ServiceEmotion;

import java.sql.SQLException;
import java.util.List;

public class ListeEmotion {

    @FXML
    private TextArea textarea;

    @FXML
    public void initialize() {

        ServiceEmotion se = new ServiceEmotion();

        try {
            List<Emotion> emotions = se.recuperer();
            textarea.setText(emotions.toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}