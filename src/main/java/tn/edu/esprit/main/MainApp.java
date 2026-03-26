package tn.edu.esprit.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

	 
  

    public static void main(String[] args){
        launch(args);
    }
	@Override
   
	public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminDashboardView.fxml"));
        Scene scene = new Scene(loader.load(), 900,950);
        stage.setTitle("Admin dashboread");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();}
	 /*@Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AjouterCarnet.fxml"));
        Scene scene = new Scene(loader.load(), 900, 1000);
        stage.setTitle("Gestion Carnets Éducatifs");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }*/
   

}