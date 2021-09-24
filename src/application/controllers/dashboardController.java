package application.controllers;

import java.io.IOException;
import java.sql.Date;

import application.dao.projectDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class dashboardController extends projectDAO {
	
	private Stage stage;
	
	@FXML
	private Label label_fname;
	
	int userId;
	String username;
	String firstName;
	
	
	public void setUserID(int userID) {
		this.userId = userID;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUsername(String username) {
		
	}
	
	
	public void setWelcomeMessage(String firstName) {
		label_fname.setText(firstName);
	}
	
	
	public void goToProfile(ActionEvent event) {
		
	}
	
	public void addProject(ActionEvent event, int userID, String projectName) {
		
	}
	
	public void saveProjectChanges(ActionEvent event, int projectID, String projectName) {
		
	}
	
	public void deleteProject(ActionEvent event, int projectID) {
		
	}
	
	public void logout(ActionEvent event) throws IOException {
		System.out.println("logging out.");
		FXMLLoader homeScene = new FXMLLoader(getClass().getResource("Home.fxml"));
		Parent root = homeScene.load();
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
	
	public void addTaskColumn(ActionEvent event, int taskID, int projectID, String taskName, Date dueDate, String description) {
		
	}
	
	public void saveTaskColumnChanges(ActionEvent event,String taskName, Date dueDate, String description) {
		
	}
	
	public void deleteTaskColumn(ActionEvent event, int taskID) {
		
	}
	
	
	
	public void addTask(ActionEvent event, int taskID, String description, boolean completed) {
		
	}
	
	public void saveTaskChanges(ActionEvent event, String description, boolean completed) {
		
	}
	
	public void deleteTask(ActionEvent event, int taskItemID) {
		
	}
	
}