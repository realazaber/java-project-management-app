package application;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import java.sql.*;

import javax.imageio.ImageIO;

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
    public static ImageView imageView = new ImageView();


    @FXML
    private TextField LoginUsername;
    
    @FXML 
    private TextField LoginPassword;
    
    @FXML
    private Label loginStatus;
    
    private static File tmpProfile = null;
    

    
    public void uploadImage(ActionEvent event) throws Exception {  	
    	
    	System.out.println("Uploading image.");
    	FileChooser fileChooser = new FileChooser();
    	
    	//Add filters so only images can be added.
        FileChooser.ExtensionFilter JPG_Filter = new FileChooser.ExtensionFilter("JPG images (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter jpg_Filter = new FileChooser.ExtensionFilter("jpg images (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter jpeg_Filter = new FileChooser.ExtensionFilter("jpeg images (*.jpeg)", "*.jpeg");
        FileChooser.ExtensionFilter PNG_Filter = new FileChooser.ExtensionFilter("PNG images (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter png_Filter = new FileChooser.ExtensionFilter("png images (*.png)", "*.png");
        
    	
    	fileChooser.getExtensionFilters().addAll(JPG_Filter, jpg_Filter, jpeg_Filter, PNG_Filter, png_Filter);
    	fileChooser.setTitle("Select image");
    	tmpProfile = fileChooser.showOpenDialog(stage);
    	System.out.println("File chosen: " + tmpProfile);

    	try {
    		BufferedImage bufferedImage = ImageIO.read(tmpProfile);
        	Image selectedImage = SwingFXUtils.toFXImage(bufferedImage, null);
        	System.out.println("Set " + tmpProfile + " as preview.");
        	imageView.setImage(selectedImage);
		} catch (Exception e) {
			System.out.println("Error uploading custom profile: " + e);
			System.out.println("Image url: " + tmpProfile);
		}

    }

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
		
		if (textFieldPassword.getText().equals(textFieldConfirmPassword.getText()) && !textFieldPassword.getText().equals("")) { //Passwords match.
			
			if(!textFieldFName.getText().equals("") && !textFieldLName.getText().equals("") && !textFieldUsername.getText().equals("")) {
				System.out.println("First name: " + textFieldFName.getText());
				System.out.println("Last name: " + textFieldLName.getText());
				System.out.println("Username: " + textFieldUsername.getText());
				System.out.println("Password: " + textFieldPassword.getText());
				System.out.println("Confirm password: " + textFieldConfirmPassword.getText());
				
				//Check if user already exists.
				PreparedStatement ps_checkUsers = myConnection.prepareStatement("SELECT * FROM `users` WHERE `first_name` = ? AND `last_name` = ?");
				ps_checkUsers.setString(1, textFieldFName.getText());
				ps_checkUsers.setString(2, textFieldLName.getText());
				
				ResultSet rs_checkUsers = ps_checkUsers.executeQuery();
		        if (rs_checkUsers.next()) {
					System.out.println("User already exists.");
					notification.setText("Account has already been created!");
				}
		        else {
					//
					
					try {
						
						PreparedStatement ps = myConnection.prepareStatement("INSERT INTO `users` (`id`, `first_name`, `last_name`, `username`, `password`, `profile`) VALUES (null,?,?,?,?,?)");
						
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
						
						ps.setString(1, textFieldFName.getText());
						ps.setString(2, textFieldLName.getText());
						ps.setString(3, textFieldUsername.getText());
						ps.setString(4, textFieldPassword.getText());															
						ps.setBinaryStream(5, input);
						ps.execute();
					}
					catch (Exception e) {
						System.out.println("Error: " + e);
					}
					
					if(input != null) {
						textFieldFName.setText("");
						textFieldLName.setText("");
						textFieldUsername.setText("");
						textFieldPassword.setText("");
						textFieldConfirmPassword.setText("");
						notification.setText("Successfully registered!");
					}
		        }
								
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
