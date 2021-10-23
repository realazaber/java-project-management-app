package application.controllers.add;

import java.io.IOException;


import java.sql.SQLException;

import application.Model;
import application.controllers.dashboardController;
import application.domains.User;
import application.dao.projectDAO;
import application.dao.userDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class newProjectController {
	
	private Stage stage;
	
	int userID;

	private Model model = new Model();
	
	@FXML
	private TextField textFieldProjectName;
	
	@FXML
	private CheckBox checkBox_default;
	
	@FXML
	private Label lbl_notification;
		
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public int getUserId() {
		return userID;
	}
	
	//Add project to database
	public void addProject(ActionEvent event) throws SQLException {
		/*
		 * If all required fields are filled in then 
		 * upload project to database.
		 */
		if (textFieldProjectName.getText() != "") {
			
			//Save whether this project is default or not.
			boolean isDefault = checkBox_default.isSelected();
			
			//If project already exists let the user know and do not add it to database.
			if (!model.getProjectDAO().addProject(userID, textFieldProjectName.getText(), isDefault)) {
				lbl_notification.setTextFill(Color.RED);
				lbl_notification.setText("Project already exists!");
				System.out.println(textFieldProjectName.getText() + " already exists");
			}
			//Add project to database and let the user know it was successful,
			else {
				lbl_notification.setTextFill(Color.GREEN);
				lbl_notification.setText("Project successfully added!");
				System.out.println(textFieldProjectName.getText() + " added to database under user id: " + userID);
			}
		}
		//Required fields have no been filled in.
		else {
			System.out.println("Project name hasn't been added.");
			lbl_notification.setText("Please enter the project name");
		}
		
		//Reset input fields.
		textFieldProjectName.clear();
		checkBox_default.setSelected(false);
	}
	
	//Go back to dashboard.
	@FXML
	public void back(ActionEvent event) throws Exception {
		System.out.println("Back to dashboard");
		
		//Load the dashboard and set the neccessary parameters.
		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("/application/views/Dashboard.fxml"));
		Parent root = dashboardScene.load();
		dashboardController dashboardController = dashboardScene.getController();
		dashboardController.setQuote();
		dashboardController.setUserID(userID);
		User user = model.getUserDAO().getUser(userID);		
		dashboardController.setWelcomeMessage(user.getFirstName());
		dashboardController.showProjects(userID);
		dashboardController.tabpane_mainTab.getSelectionModel().select(1);
		dashboardController.loadUser(user);
		
		//Go to dashboard.
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}