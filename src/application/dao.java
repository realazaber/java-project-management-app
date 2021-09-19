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
	
	public boolean userExists() {
		connect();
		return false;
	}
	
	public void addUser(String firstName, String lastName, String username, String password, File profileImage) {
		connect();
	}
	
	public void addProject(int user_id, String projectName) {
		connect();
	}
	
	public void addTask(int taskID, int projectID, String taskName, Date dueDate, String description) {
		connect();
	}
	
	public void addTaskItem() {
		connect();
	}
}