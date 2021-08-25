package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.*;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		try {
			Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost/java-project-management-db", "root", "");
			
		} 
		catch (Exception e) {
			System.out.println("Can't connect to database.");
			System.out.println("Error code: " + e);
			System.exit(0);
		}
		System.out.println("Connected to database.");
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/Home.fxml"));
			Scene scene = new Scene(root,800,600);
			
			String css = getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			System.out.println("GUI error.");
			System.out.println("Error code: " + e);
			System.exit(0);
		}
		System.out.println("Loaded GUI.");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
