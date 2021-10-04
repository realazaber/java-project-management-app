package application.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.Project;
import application.controllers.newProjectController;

public class projectDAOImpl implements projectDAO {
	
	baseDao baseDao = new baseDao();
	
	public projectDAOImpl() {
		
	}
	
	public boolean addProject(int userID, String projectName) throws SQLException {
		try {
			Connection connection = baseDao.connect();
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
	
	public ArrayList<Project> loadProjects(int userID) throws SQLException {
		System.out.println("Loading projects for user: " + userID);
		ArrayList<Project> projects = new ArrayList<Project>();
		
		Statement loadProjectStatement = baseDao.connect().createStatement();
		String query = "SELECT `project_id`, `user_id`, `project_name` FROM `projects` WHERE `user_id` = '" + userID + "'";
		ResultSet rs = loadProjectStatement.executeQuery(query);
		
		while (rs.next()) {
			Project tmpProject = new Project();
			tmpProject.setProjectID(rs.getInt(1));
			tmpProject.setUserID(rs.getInt(2));
			tmpProject.setProjectName(rs.getString(3));
			projects.add(tmpProject);
			
			System.out.println("=============================");
			System.out.println("Project ID: " + tmpProject.getProjectID());
			System.out.println("Project Name: " + tmpProject.getProjectName());
			System.out.println("Project user ID: " + tmpProject.getUserID());
		}
		
		
		return projects;
		
	}
	
	public void saveProjectChanges(String projectName) throws SQLException {
		
	}
	
	public void deleteProject(int projectID) throws SQLException {
		try {
			Connection connection = baseDao.connect();
			String query =  "DELETE FROM `projects` WHERE `projects`.`project_id` = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, projectID);
			statement.execute();
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	public void addColumn(int taskColumnID, int projectID, String taskName, Date dueDate, String description) throws SQLException {
		
	}
	
	public void saveColumnChanges(int taskColumnID, String taskName, Date dueDate, String description) throws SQLException {
		
	}
	
	public void deleteColumn(int taskID) throws SQLException {
		
	}
	
	public void addTask(int taskID, String description, boolean completed) throws SQLException {
		
	}
	
	public void saveTaskChanges(int taskID, String description, boolean completed) throws SQLException {
		
	}
	
	public void deleteTask(int taskItemID) throws SQLException {
		
	}
}