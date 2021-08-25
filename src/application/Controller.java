package application;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.*;


import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.*;

public class Controller {
	
	
	
	private Stage stage;
	private Scene scene;
	
    @FXML
    private TextField textFieldFName;

    @FXML
    private TextField textFieldLName;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private TextField textFieldConfirmPassword;
    
    @FXML
    private Label notification;
    
    

	public void Register(ActionEvent event) throws Exception {
		
		Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost/java-project-management-db", "root", "");
		
		
		
		if (textFieldPassword.getText().equals(textFieldConfirmPassword.getText()) && !textFieldPassword.getText().equals("")) { //Passwords match.
			
			if(!textFieldFName.getText().equals("") && !textFieldLName.getText().equals("") && !textFieldUsername.getText().equals("")) {
				System.out.println("First name: " + textFieldFName.getText());
				System.out.println("Last name: " + textFieldLName.getText());
				System.out.println("Username: " + textFieldUsername.getText());
				System.out.println("Password: " + textFieldPassword.getText());
				System.out.println("Confirm password: " + textFieldConfirmPassword.getText());
				
				Statement query_Register = myConnection.createStatement();
				query_Register.execute("INSERT INTO `users` (`id`, `first_name`, `last_name`, `username`, `password`, `path_to_profile`) "
						+ "VALUES (NULL, '"+ textFieldFName.getText() +"', '"+ textFieldLName.getText() +"', '"+ textFieldUsername.getText() +"', '"+ textFieldPassword.getText() +"', '');");
				
				notification.setText("Successfully registered!");
			}
			else {
				notification.setText("Please fill in all fields");
			}
			

		}
		else {
			notification.setText("Passwords do not match!");
		}
		
		
		
	}
	

	public void MoveToHome(ActionEvent event) throws IOException {
		System.out.println("Going to Home page.");
		Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		stage.setScene(scene);
		String css = getClass().getResource("application.css").toExternalForm();
		scene.getStylesheets().add(css);
		stage.show();
	}
	
	public void Quit(ActionEvent event) {
		System.out.println("Quiting Application.");
		System.exit(0);
	}
}
