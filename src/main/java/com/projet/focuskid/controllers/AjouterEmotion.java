package com.projet.focuskid.controllers;

import com.projet.focuskid.entities.Emotion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import com.projet.focuskid.services.ServiceEmotion;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterEmotion {

    @FXML
    private TextField txtnom;

    @FXML
    private TextField txticone;

    @FXML
    void addEmotion(ActionEvent event) {

        String nom = txtnom.getText();
        String icone = txticone.getText();

        ServiceEmotion se = new ServiceEmotion();
        Emotion emotion = new Emotion(nom, icone);

        try {
            se.ajouter(emotion);

            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/projet/focuskid/AfficherEmotion.fxml"));
            Parent root = loader.load();

            AfficherEmotion ae = loader.getController();
            ae.setRnom(nom);
            ae.setRicone(icone);
            ae.setrList(se.recuperer().toString());

            txtnom.getScene().setRoot(root);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}