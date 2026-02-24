package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AfficherScenario {

    @FXML
    private TextField rList;

    @FXML
    private TextField rtitre;

    @FXML
    private TextField rdescription;

    @FXML
    private TextField ranimationUrl;

    @FXML
    private TextField remotionId;

    public void setrList(String rList) {
        this.rList.setText(rList);
    }

    public void setRtitre(String rtitre) {
        this.rtitre.setText(rtitre);
    }

    public void setRdescription(String rdescription) {
        this.rdescription.setText(rdescription);
    }

    public void setRanimationUrl(String ranimationUrl) {
        this.ranimationUrl.setText(ranimationUrl);
    }

    public void setRemotionId(String remotionId) {
        this.remotionId.setText(remotionId);
    }
}