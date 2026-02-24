package tn.edu.esprit.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

	  /* @Override
	    public void start(Stage stage) throws Exception {

	        FXMLLoader loader = new FXMLLoader(
	                getClass().getResource("/view/AjouterCommentaire.fxml")
	        );

	        Scene scene = new Scene(loader.load(), 600, 500);

	        stage.setTitle("Commentaire");
	        stage.setScene(scene);
	        stage.show();
	    }*/
	@Override
    
		public void start(Stage stage) throws Exception{
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ParentDashboard.fxml"));
	        Scene scene = new Scene(loader.load(), 900, 1000);
	        stage.setTitle("Gestion Carnets Éducatifs");
	        stage.setMaximized(true);
	        stage.setScene(scene);
	        stage.show();
       
    }
	/*@Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AjouterCarnet.fxml"));
        Scene scene = new Scene(loader.load(), 900, 1000);
        stage.setTitle("Gestion Carnets Éducatifs");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }*/

    public static void main(String[] args){
        launch(args);
    }
}