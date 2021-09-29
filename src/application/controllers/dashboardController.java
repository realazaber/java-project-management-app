package application.controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import application.Project;
import application.dao.projectDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class dashboardController extends projectDAO {
	
	String[] quotes = {"You are epic smart", "I owe you kfc", "You are a chad"};
	
	private Stage stage;
	
	@FXML
	private TabPane tab_projects;
	
	@FXML
	private Label lbl_fname;
	
	@FXML
	private Label lbl_inspirationalQuote;

	
	@FXML
	private Button btn_createProject;
	

	
	int userId;
	String username;
	String firstName;
	

	
	public void showProjects(int userID) throws Exception {
		ArrayList<Project> userProjects = loadProjects(userID);
		
		
		
		for (Project project : userProjects) {

			
			
			Tab tab = new Tab(project.getProjectName());
			
			tab_projects.getTabs().add(tab);
		}
		
		

	}
	
	//Creates an inspirational quote.
	public void setQuote() {
		int randomInt = new Random().nextInt(quotes.length);
		lbl_inspirationalQuote.setText(quotes[randomInt]);
	}
	
	public void setUserID(int userID) {
		this.userId = userID;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUsername(String username) {
		
	}
	
	
	public void setWelcomeMessage(String firstName) {
		lbl_fname.setText(firstName);
	}
	
	
	//Open the window for adding the new project.
	public void addProjectWindow(ActionEvent event) throws IOException {
		System.out.println("Opening add project window.");
		System.out.println("User ID: " + userId);
		
		FXMLLoader newProjectScene = new FXMLLoader(getClass().getResource("NewProject.fxml"));
		Parent root = newProjectScene.load();
		
		newProjectController newProjectController = newProjectScene.getController();
		newProjectController.setUserID(userId);
		
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

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