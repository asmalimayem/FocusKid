package tn.edu.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ParentDashboardController {

    @FXML
    private void ouvrirCarnet() {
        ouvrirFenetre("/view/AjouterCarnet.fxml", "Gestion Carnet");
    }

    @FXML
    private void ouvrirCommentaire() {
        ouvrirFenetre("/view/AjouterCommentaire.fxml", "Gestion Commentaires");
    }

    private void ouvrirFenetre(String path, String title) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(title);
            
            // Option 1 : Maximiser la fenêtre (occupe tout l'écran)
            //stage.setMaximized(true);
            
            // Option 2 : Définir une taille fixe (par exemple 900x700)
             stage.setWidth(900);
            stage.setHeight(700);
            
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
}