package application;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;

public class dao {
	
	public void connect() {
		//Try to connect to database, if connection can not be made then 
		//close the program.
		try {
			Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost/java-project-management-db", "root", "");
			System.out.println("Connected to database.");
			
		} 
		catch (Exception e) {
			System.out.println("Can't connect to database.");
			System.out.println("Error code: " + e);
			System.exit(0);
		}
	}

	public boolean userExists(String username, String firstName, String lastName) {
		
		return false;
	}
	
	public void addUser(String firstName, String lastName, String username, String password, File profileImage) {
		
	}
	
	public void addProject(int userID, String projectName) {
		
	}
	
	public void saveProjectChanges(String projectName) {
		
	}
	
	public void deleteProject(int projectID) {
		
	}
	
	public void addTaskColumn(int taskID, int projectID, String taskName, Date dueDate, String description) {
		
	}
	
	public void saveTaskColumnChanges(String taskName, Date dueDate, String description) {
		
	}
	
	public void deleteTaskColumn(int taskID) {
		
	}
	
	public void addTask(int taskID, String description, boolean completed) {
		
	}
	
	public void saveTaskChanges(String description, boolean completed) {
		
	}
	
	public void deleteTask(int taskItemID) {
		
	}
}