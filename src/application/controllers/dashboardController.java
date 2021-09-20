package application.controllers;

import java.io.IOException;
import java.sql.Date;

import application.dao.projectDAO;
import javafx.event.ActionEvent;

public class dashboardController extends projectDAO {
	
	int userId;
	
	public dashboardController(int userID) {
		this.userId = userID;
	}
	
	public void addProject(ActionEvent event, int userID, String projectName) {
		
	}
	
	public void saveProjectChanges(ActionEvent event,String projectName) {
		
	}
	
	public void deleteProject(ActionEvent event,int projectID) {
		
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