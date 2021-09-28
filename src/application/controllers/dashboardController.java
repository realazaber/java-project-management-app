package application.controllers;

import java.io.IOException;
import java.sql.Date;
import java.util.Random;

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
	private TextField textFieldProjectName;
	
	@FXML
	private Button btn_createProject;
	
	@FXML
	private Label lbl_notification;
	
	int userId;
	String username;
	String firstName;
	
	
	
	public void loadProject(int projectId) {
		
		
		String tmpString = Integer.toString(projectId);
		 Tab tab = new Tab();
		 tab.setText(tmpString);
		 tab_projects.getTabs().add(tab);
	}
	
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
	
	
	public void goToProfile(ActionEvent event) {
		
	}
	
	//Open the window for adding the new project.
	public void addProjectWindow(ActionEvent event) throws IOException {
		System.out.println("Opening add project window.");
		
		
		FXMLLoader newProjectScene = new FXMLLoader(getClass().getResource("NewProject.fxml"));
		Parent root = newProjectScene.load();
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//Add project to database
	public void addProject(ActionEvent event) {
		
		if (addProject(userId, textFieldProjectName.getText()) == false) {
			lbl_notification.setText("Project already exists!");
			System.out.println(textFieldProjectName.getText() + " already exists");
		}
		else {
			lbl_notification.setText("Project successfully added!");
			System.out.println(textFieldProjectName.getText() + " added to database.");
		}
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