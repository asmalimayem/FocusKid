package com.projet.focuskid.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AfficherEmotion {

    @FXML
    private TextField rList;

    @FXML
    private TextField rnom;

    @FXML
    private TextField ricone;

    public void setrList(String rList) {
        this.rList.setText(rList);
    }

    public void setRnom(String rnom) {
        this.rnom.setText(rnom);
    }

    public void setRicone(String ricone) {
        this.ricone.setText(ricone);
    }
}
