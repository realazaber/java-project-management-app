package application.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class projectDAO extends baseDao {
	
	public boolean addProject(int userID, String projectName) {
		try {
			Connection connection = connect();
			Statement checkProjects = connection.createStatement();
			ResultSet rs = checkProjects.executeQuery("SELECT * FROM `projects` WHERE `project_name` = '" + projectName + "'");
			if (!rs.next()) {
				String query = "INSERT INTO `projects` (`project_id`, `user_id`, `project_name`) VALUES (null, ?, ?)";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, userID);
				statement.setString(2, projectName);
				statement.execute();
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return false;
	}
	
	public void saveProjectChanges(String projectName) {
		
	}
	
	public void deleteProject(int projectID) {
		try {
			Connection connection = connect();
			String query =  "DELETE FROM `projects` WHERE `projects`.`project_id` = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, projectID);
			statement.execute();
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	public void addTaskColumn(int taskColumnID, int projectID, String taskName, Date dueDate, String description) {
		
	}
	
	public void saveTaskColumnChanges(int taskColumnID, String taskName, Date dueDate, String description) {
		
	}
	
	public void deleteTaskColumn(int taskID) {
		
	}
	
	public void addTask(int taskID, String description, boolean completed) {
		
	}
	
	public void saveTaskChanges(int taskID, String description, boolean completed) {
		
	}
	
	public void deleteTask(int taskItemID) {
		
	}
}