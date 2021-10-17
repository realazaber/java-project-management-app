package application.controllers;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import application.Model;
import application.controllers.add.newColumnController;
import application.objects.User;
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
    
    public void chooseProfile(ActionEvent event) throws Exception {     	
    	
    	System.out.println("Uploading image.");
    	FileChooser fileChooser = new FileChooser();
    	
    	//Add filters so only images can be added.
    	FileChooser.ExtensionFilter png_Filter = new FileChooser.ExtensionFilter("png images (*.png)", "*.png");
    	FileChooser.ExtensionFilter PNG_Filter = new FileChooser.ExtensionFilter("PNG images (*.PNG)", "*.PNG");    	
    	fileChooser.getExtensionFilters().addAll(png_Filter, PNG_Filter);
    	fileChooser.setTitle("Select image");
    	
    	//Let user choose image and notify them when an image was selected.
    	tmpProfile = fileChooser.showOpenDialog(stage);
    	System.out.println("File chosen: " + tmpProfile);

    	try {
    		//Save the image and display it on image view.
    		InputStream fileInputStream = new FileInputStream(tmpProfile);
    		Image selectedImage = new Image(fileInputStream);
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
	public void Register(ActionEvent event) throws Exception {
				
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		BufferedInputStream file_defaultImage = (BufferedInputStream) classLoader.getResourceAsStream("default_profile.png");
		
		Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost/java-project-management-db", "root", "");
			
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
		else {//Set the selected image as profile.
			input = new BufferedInputStream(new FileInputStream(tmpProfile));
		}
		
		if (passField_Password.getText().equals(passField_ConfirmPassword.getText()) && !passField_Password.getText().equals("")) { //Passwords match.
			
			if(!textField_FName.getText().equals("") && !textField_LName.getText().equals("") && !textField_Username.getText().equals("")) {
				System.out.println("First name: " + textField_FName.getText());
				System.out.println("Last name: " + textField_LName.getText());
				System.out.println("Username: " + textField_Username.getText());
				System.out.println("Password: " + passField_Password.getText());
				System.out.println("Confirm password: " + passField_ConfirmPassword.getText());
				
				//Check if user already exists.
				
				User tmpUser = new User();
				tmpUser.setUsername(textField_Username.getText());
				Boolean userExists = model.getUserDAO().userExists(tmpUser);
				
				
		        if (userExists) {
					System.out.println("User already exists.");
					lbl_notification.setText("Account has already been created!");
				}

		        else {
					//
					try {
						
						//
						//If no image is uploaded use the default.
						if (tmpProfile == null) {
							System.out.println("No image has been added.");
							//Upload default
							input = file_defaultImage;
							System.out.println("Default image " + file_defaultImage + " uploading.");						
						}
						else {
							System.out.println("Photo " + tmpProfile + " is being uploaded.");
						}					
						

						
						model.getUserDAO().addUser(textField_FName.getText(), textField_LName.getText(), textField_Username.getText(), passField_Password.getText(), input);
					}
					catch (Exception e) {
						System.out.println("Error: " + e);
					}
					
					if(input != null) {
						textField_FName.setText("");
						textField_LName.setText("");
						textField_Username.setText("");
						passField_Password.setText("");
						passField_ConfirmPassword.setText("");
						lbl_notification.setText("Successfully registered!");
						tmpProfile = null;
					}
		        }
								
			}
			else {
				System.out.println("Some data is not filled in.");
				lbl_notification.setText("Please fill in all fields.");
			}
			
		}
		else {
			System.out.println(passField_Password.getText() + " does not match " + passField_ConfirmPassword.getText());
			lbl_notification.setText("Passwords do not match!");
		}
			
	}
	
	//Check user details and login to dashboard.
	public void Login(ActionEvent event) throws Exception {
		
		
		System.out.println("Checking for user");
		
		User currentUser = model.getUserDAO().loginUser(textField_LoginUsername.getText(), passField_LoginPassword.getText());
		
		
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
        	System.out.println("User does not exist.");
        	lbl_loginStatus.setText("Invalid username or password.");
        }
        System.out.println("PASSWORD FIELD: " + passField_LoginPassword.getText());
	}
	
	//Close application.
	public void Quit(ActionEvent event) {
		System.out.println("Quiting Application.");
		System.exit(0);
	}
}
