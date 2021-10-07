package application.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.objects.Column;
import application.objects.Project;
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
			//Connect to database
			Connection connection = baseDao.connect();
			
			//Delete project
			String queryDeleteProject =  "DELETE FROM `projects` WHERE `projects`.`project_id` = ?";
			PreparedStatement statementDeleteProject = connection.prepareStatement(queryDeleteProject);
			statementDeleteProject.setInt(1, projectID);
			statementDeleteProject.execute();
			
			//Delete columns
			String queryDeleteColumns = "DELETE FROM `columns` WHERE `project_id` = ?";
			PreparedStatement statementDeleteColumns = connection.prepareStatement(queryDeleteColumns);
			statementDeleteColumns.setInt(1, projectID);
			statementDeleteColumns.execute();
			
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	public boolean addColumn(int projectID, String columnName, Date dueDate, String description) throws SQLException {

		
		try {
			Connection connection = baseDao.connect();
			Statement checkColumns = connection.createStatement();
			ResultSet rs = checkColumns.executeQuery("SELECT * FROM `columns` WHERE `column_name` = '" + columnName + "'");
			if (!rs.next()) {
				String query = "INSERT INTO `columns` (`column_id`, `project_id`, `column_name`, `due_date`, `description`) VALUES (null, ?, ?, ?, ?)";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, projectID);
				statement.setString(2, columnName);
				statement.setDate(3, dueDate);
				statement.setString(4, description);
				statement.execute();
				return true;
			}
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return false;
	}
	
	public ArrayList<Column> loadColumns(int projectID) throws SQLException {
		System.out.println("Loading columns for project " + projectID);
		ArrayList<Column> columns = new ArrayList<Column>();
		
		Statement loadColumnsStatement = baseDao.connect().createStatement();
		String query = "SELECT `column_name`, `due_date`, `description` FROM `columns` WHERE `project_id` = '" + projectID + "'";
		ResultSet rs = loadColumnsStatement.executeQuery(query);
		
		while (rs.next()) {
			Column tmpColumn = new Column();
			tmpColumn.setColumn_name(rs.getString(1));
			tmpColumn.setDue_date(rs.getDate(2));
			tmpColumn.setDescription(rs.getString(3));
			System.out.println("Column name: " + tmpColumn.getColumn_name());
			System.out.println("Due date: " + tmpColumn.getDue_date());
			System.out.println("Description: " + tmpColumn.getDescription());
			columns.add(tmpColumn);
			
			System.out.println("=============================");

		}
		
		return columns;
	}
	
	public void saveColumnChanges(int taskColumnID, String taskName, Date dueDate, String description) throws SQLException {
		
	}
	
	public void deleteColumn(int taskID) throws SQLException {
		
	}
	
	public boolean addTask(int taskID, String description, boolean completed) throws SQLException {
		return false;
	}
	
	public void saveTaskChanges(int taskID, String description, boolean completed) throws SQLException {
		
	}
	
	public void deleteTask(int taskItemID) throws SQLException {
		
	}
}