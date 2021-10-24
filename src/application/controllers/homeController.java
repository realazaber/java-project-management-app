package application.controllers;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.io.*;

import application.Model;
import application.controllers.add.newColumnController;
import application.domains.User;
import application.dao.userDAO;

public class homeController {
	
	private Stage stage;
	
    @FXML
    private TextField textField_FName;

    @FXML
    private TextField textField_LName;

    @FXML
    private TextField textField_Username;

    @FXML    
    private PasswordField passField_Password;
    
    @FXML
    private PasswordField passField_ConfirmPassword;
    
    @FXML
    private Label lbl_notification;
    
    @FXML 
    private Button btn_imageUpload;
    
    @FXML 
    public ImageView imageView = new ImageView();

    @FXML
    private TextField textField_LoginUsername;
    
    @FXML
    private PasswordField passField_LoginPassword;
    
    @FXML
    private Label lbl_loginStatus;
    
    @FXML
    private File tmpProfile;
    
    private Model model = new Model();
    
    //Open file explorer and let user choose profile image.
    @FXML
    public void chooseProfile(ActionEvent event) throws Exception {     	
    	
    	System.out.println("Uploading image.");
    	FileChooser fileChooser = new FileChooser();
    	
    	//Add filters so only images can be added.
    	fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
    	fileChooser.setTitle("Select image");
    	
    	//Let user choose image and notify them when an image was selected.
    	tmpProfile = fileChooser.showOpenDialog(stage);
    	System.out.println("File chosen: " + tmpProfile);

    	try {
    		//Save the image and display it on image view.
    		FileInputStream fileInputStream = new FileInputStream(tmpProfile);
    		
    		byte[] newProfileBytes = fileInputStream.readAllBytes();    		
    		Image selectedImage = new Image(new ByteArrayInputStream(newProfileBytes));
    		System.out.println("Chosen image " + selectedImage);
    		
    		imageView.setImage(selectedImage);
    		
    		System.out.println("Set " + tmpProfile + " as preview.");
		} catch (Exception e) {
			//If there is an error, notify the user.
			System.out.println("Error uploading custom profile: " + e);
			System.out.println("Image url: " + tmpProfile);
			
		}
    	

    }

    //If user is not in database yet then upload new user.
    @FXML
	public void Register(ActionEvent event) throws Exception {
				
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		BufferedInputStream file_defaultImage = (BufferedInputStream) classLoader.getResourceAsStream("default_profile.png");
			
		BufferedInputStream input = null;
		
		if (tmpProfile == null) {//Set default profile if user has not selected an image.
			try {
				//Set the image to upload as the default image.
				input = file_defaultImage;
				System.out.println("Using default profile.");
			} catch (Exception e) {
				System.out.println("Default image not found: " + e);
			}
		}
		else {//If user has chosen an image, set it as profile.
			input = new BufferedInputStream(new FileInputStream(tmpProfile));
		}
		
		//Passwords match.
		if (passField_Password.getText().equals(passField_ConfirmPassword.getText()) && !passField_Password.getText().equals("")) { 
			
			//All fields are filled in.
			if(!textField_FName.getText().equals("") && !textField_LName.getText().equals("") && !textField_Username.getText().equals("")) {
				
				//Show the user details in console for debugging.
				System.out.println("First name: " + textField_FName.getText());
				System.out.println("Last name: " + textField_LName.getText());
				System.out.println("Username: " + textField_Username.getText());
				System.out.println("Password: " + passField_Password.getText());
				System.out.println("Confirm password: " + passField_ConfirmPassword.getText());
				
				//Create user.
				User tmpUser = new User();
				tmpUser.setUsername(textField_Username.getText());
				
				//Check if user already exists.				
				Boolean userExists = model.getUserDAO().userExists(tmpUser);
				
				
		        if (userExists) { //User account already exists.
					System.out.println("User already exists.");
					lbl_notification.setTextFill(Color.ORANGE);
					lbl_notification.setText("Account has already been created!");
				}
		        else { //If user account doesn't exist then add user to database.					
					try {																				
						//Add user to database.
						model.getUserDAO().addUser(textField_FName.getText(), textField_LName.getText(), textField_Username.getText(), passField_Password.getText(), input);
						//Let the user know that the account has been created.
					}
					catch (Exception e) {
						System.out.println("Error: " + e);
					}
					
					if(input != null) {
						//Let the user know that the account has been created.						
						textField_FName.clear();
						textField_LName.clear();
						textField_Username.clear();
						passField_Password.clear();
						passField_ConfirmPassword.clear();
						lbl_notification.setTextFill(Color.GREEN);
						lbl_notification.setText("Successfully registered!");
						
					}
		        }								
			}
			else {
				//Prompt the user to fill in all fields.
				System.out.println("Some data is not filled in.");
				lbl_notification.setTextFill(Color.RED);
				lbl_notification.setText("Please fill in all fields.");
			}
			
		}
		else {
			//Notify the user that password and confirm password does not match.
			System.out.println(passField_Password.getText() + " does not match " + passField_ConfirmPassword.getText());
			lbl_notification.setTextFill(Color.RED);
			lbl_notification.setText("Passwords do not match!");
		}
			
	}
	
	//Check user details and login to dashboard.
	public void Login(ActionEvent event) throws Exception {
		System.out.println("Checking for user");
		
		//Check if a user with the same username and password exists.
		User currentUser = model.getUserDAO().loginUser(textField_LoginUsername.getText(), passField_LoginPassword.getText());
		
		//If the user exists then login.
		if (currentUser != null) {	
			
        	FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("/application/views/Dashboard.fxml"));
        	Parent root = dashboardScene.load();      	
        	        	
        	dashboardController dashboardController = dashboardScene.getController();
        	dashboardController.setUserID(currentUser.getUserID());
        	dashboardController.loadUser(currentUser);
        	dashboardController.setWelcomeMessage(currentUser.getFirstName());
        	dashboardController.setQuote();
        	dashboardController.showProjects(currentUser.getUserID());

        	System.out.println("Logging in...");
        	System.out.println("User id: " + dashboardController.getUserId());        	
			
        	System.out.println("User exists.");
			lbl_loginStatus.setText("Logging in!");
						
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
        else {
        	//If the user has not been created or the login details are wrong let the user know.
        	System.out.println("User does not exist.");
        	lbl_loginStatus.setTextFill(Color.RED);
        	lbl_loginStatus.setText("Invalid login details or user does not exist.");
        }      
	}
	
	//Close application.
	public void Quit(ActionEvent event) {
		System.out.println("Quiting Application.");
		System.exit(0);
	}
}
