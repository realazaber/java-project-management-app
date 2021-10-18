package application;	

import application.dao.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Main extends Application{
	
	private Model model = new Model();
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		//Load the GUI, if there is an error end the program.
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/views/Home.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			System.out.println("Loaded GUI.");
		} 
		catch(Exception e) {
			System.out.println("GUI error.");
			System.out.println("Error code: " + e);
			System.exit(0);
		}
		//Load the database, if there is an error end the program.
		try {
			model.getBaseDao().connect();
			System.out.println("Connected to database.");
		} catch (Exception e) {
			System.out.println("Database error.");
			System.out.println("Error code: " + e);
			System.exit(0);
		}
	}
	
	//Run the program
	public static void main(String[] args) {
		baseDao mainDao = new baseDao();
		mainDao.connect();	
		launch(args);
	}
}
