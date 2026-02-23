import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        ouvrirEnseignant();
        primaryStage.show();
    }

    public static void ouvrirEnseignant() throws Exception {
        // Charger le FXML depuis le dossier fxml sous src
        URL fxmlUrl = Main.class.getResource("/fxml/enseignant.fxml");
        if (fxmlUrl == null) {
            // Essayer un chemin alternatif
            fxmlUrl = Main.class.getResource("fxml/enseignant.fxml");
        }

        if (fxmlUrl == null) {
            System.err.println("ERREUR: fichier fxml/enseignant.fxml introuvable!");
            System.err.println("Chemin recherché: /fxml/enseignant.fxml ou fxml/enseignant.fxml");
            return;
        }

        System.out.println("FXML trouvé: " + fxmlUrl);

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());

        // Charger le CSS depuis le dossier css sous src
        URL cssUrl = Main.class.getResource("/css/tdah-style.css");
        if (cssUrl == null) {
            cssUrl = Main.class.getResource("css/tdah-style.css");
        }

        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            System.out.println("CSS trouvé: " + cssUrl);
        } else {
            System.err.println("CSS non trouvé");
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle("Espace Enseignant - Application TDAH");
        primaryStage.setMaximized(true);
    }

    public static void ouvrirApprenant() throws Exception {
        URL fxmlUrl = Main.class.getResource("/fxml/apprenant.fxml");
        if (fxmlUrl == null) {
            fxmlUrl = Main.class.getResource("fxml/apprenant.fxml");
        }

        if (fxmlUrl == null) {
            System.err.println("ERREUR: fichier fxml/apprenant.fxml introuvable!");
            return;
        }

        System.out.println("FXML trouvé: " + fxmlUrl);

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());

        URL cssUrl = Main.class.getResource("/css/tdah-style.css");
        if (cssUrl == null) {
            cssUrl = Main.class.getResource("css/tdah-style.css");
        }

        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle("Espace Apprenant - Application TDAH");
        primaryStage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}