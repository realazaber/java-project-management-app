package application.controllers;

import java.io.File;
import java.io.IOException;

import application.dao.userDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class profileViewController extends userDAO {
	
    @FXML
    private TextField textFieldFName;
    
    @FXML
    private TextField textFieldLName;
    
    @FXML
    private TextField txtFieldusername;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private File newProfile;
	
    //Get the input fields and update the user details with them.
	public void saveProfileChanges(ActionEvent event, String firstName, String lastName, String username, File newProfile) throws Exception {  	
		
	}
	
	//Navigate back to dashboard.
	public void goToDashboard(ActionEvent event) throws IOException {
		
	}
}