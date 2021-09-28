package application.controllers;

import java.io.IOException;

import application.dao.projectDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class newProjectController extends projectDAO {
	
	private Stage stage;
	
	int userId;
	
	@FXML
	private TextField textFieldProjectName;
	
	@FXML
	private Label lbl_notification;
		
	
	public void setUserID(int userID) {
		this.userId = userID;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void back(ActionEvent event) throws IOException {
		System.out.println("Back to dashboard");
		
		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
		Parent root = dashboardScene.load();
		dashboardController dashboardController = dashboardScene.getController();
		dashboardController.setQuote();
		dashboardController.setUserID(userId);
		dashboardController.setWelcomeMessage("First name");
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//Add project to database
	public void addProject(ActionEvent event) {
		
		System.out.println("OIJIIJ " + userId);
		
		if (textFieldProjectName.getText() != "") {
			if (addProject(userId, textFieldProjectName.getText()) == false) {
				lbl_notification.setText("Project already exists!");
				System.out.println(textFieldProjectName.getText() + " already exists");
			}
			else {
				lbl_notification.setText("Project successfully added!");
				System.out.println(textFieldProjectName.getText() + " added to database under user id: " + userId);
			}
		}
		else {
			System.out.println("Project name hasn't been added.");
			lbl_notification.setText("Please enter the project name");
		}
		
	}
	
}