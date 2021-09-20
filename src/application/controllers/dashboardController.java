package application.controllers;

import java.sql.Date;

import application.dao.projectDAO;
import javafx.event.ActionEvent;

public class dashboardController extends projectDAO {
	
	int userId;
	
	
	public void setUserID(int userID) {
		this.userId = userID;
	}
	
	public void goToProfile(ActionEvent event) {
		
	}
	
	public void addProject(ActionEvent event, int userID, String projectName) {
		
	}
	
	public void saveProjectChanges(ActionEvent event, int projectID, String projectName) {
		
	}
	
	public void deleteProject(ActionEvent event, int projectID) {
		
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