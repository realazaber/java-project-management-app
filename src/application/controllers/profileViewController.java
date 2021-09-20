package application.controllers;

import application.dao.userDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class profileViewController extends userDAO {
	
    @FXML
    private TextField firstName;
    
    @FXML
    private TextField lastName;
	
	public void saveProfileChanges(ActionEvent event, String firstName, String lastName) throws Exception {  	
		
	}
}