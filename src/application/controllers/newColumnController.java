package application.controllers;

import java.sql.Date;
import java.sql.SQLException;

import application.Model;
import application.objects.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class newColumnController {
	
	private Stage stage;
	
	public int userID;
	
	public int projectID;
	
	private Model model = new Model();
	
	@FXML
	private TextField txtFieldColumnName;
	
	@FXML
	private Label lbl_heading;
	
	@FXML
	private TextArea txtAreaDescription;
	
	@FXML
	private DatePicker datePicker;
	
	@FXML
	private Label lbl_notification;
	
	public newColumnController() {
		
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}


	public int getProjectID() {
		return projectID;
	}


	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	

	public void setHeading(String projectName) {
		lbl_heading.setText("Add column for project " + projectName);
	}
	
	
	public void back(ActionEvent event) throws Exception {
		System.out.println("Back to dashboard");
		
		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
		Parent root = dashboardScene.load();
		dashboardController dashboardController = dashboardScene.getController();
		dashboardController.setQuote();
		dashboardController.setUserID(userID);
		User user = model.getUserDAO().getUser(userID);
		
		dashboardController.setWelcomeMessage(user.getFirstName());
		dashboardController.showProjects(userID);
		
		
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//Add project to database
	public void addColumn(ActionEvent event) throws SQLException {
		Date tmpDate = new Date(datePicker.getValue().toEpochDay());
		
		if (txtFieldColumnName.getText() != "") {
			if (model.getProjectDAO().addColumn(projectID, txtFieldColumnName.getText(), tmpDate, txtAreaDescription.getText())) {
				
			}
		}
		
		
//		if (textFieldProjectName.getText() != "") {
//			if (model.getProjectDAO().addProject(userId, textFieldProjectName.getText()) == false) {
//				lbl_notification.setText("Project already exists!");
//				System.out.println(textFieldProjectName.getText() + " already exists");
//			}
//			else {
//				lbl_notification.setText("Project successfully added!");
//				System.out.println(textFieldProjectName.getText() + " added to database under user id: " + userId);
//			}
//		}
//		else {
//			System.out.println("Project name hasn't been added.");
//			lbl_notification.setText("Please enter the project name");
//		}
		
	}
	
}