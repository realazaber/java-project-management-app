package application;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.mysql.cj.x.protobuf.MysqlxCursor.Open;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.*;


import javafx.scene.control.*;
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
    
    @FXML 
    private Button btn_imageUpload;
    
    @FXML 
    public static ImageView imageView;

    @FXML
    private TextField LoginUsername;
    
    @FXML 
    private TextField LoginPassword;
    
    @FXML
    private Label loginStatus;
    
    public void uploadImage(ActionEvent event) throws Exception {
    	
    	System.out.println("Uploading image.");
    	

    	
    	
    }

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
				System.out.println("Some data is not filled in.");
				notification.setText("Please fill in all fields.");
			}
			

		}
		else {
			System.out.println(textFieldPassword.getText() + " does not match " + textFieldConfirmPassword.getText());
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
	
	public void Login(ActionEvent event) throws IOException, SQLException {
		System.out.println("Checking for user");
		Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost/java-project-management-db", "root", "");
		Statement query_Login = myConnection.createStatement();
		ResultSet rs = query_Login.executeQuery("SELECT * FROM `users` WHERE `username` = '" + LoginUsername.getText() + "' AND `password` = '" + LoginPassword.getText() + "'");
        if (rs.next()) {
			System.out.println("User exists.");
			loginStatus.setText("Logging in!");
		}
        else {
        	System.out.println("User does not exist.");
        	loginStatus.setText("Invalid username or password.");
        }
	}
	
	public void Quit(ActionEvent event) {
		System.out.println("Quiting Application.");
		System.exit(0);
	}
}
