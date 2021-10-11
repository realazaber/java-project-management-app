package application.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import application.objects.Column;
import application.objects.Project;
import application.objects.Task;
import application.controllers.newProjectController;

public class projectDAOImpl implements projectDAO {
	
	baseDao baseDao = new baseDao();
	
	public projectDAOImpl() {
		
	}
	
	public boolean addProject(int userID, String projectName) throws SQLException {
		try {
			Connection connection = baseDao.connect();
			Statement checkProjects = connection.createStatement();
			ResultSet rs = checkProjects.executeQuery("SELECT * FROM `projects` WHERE `project_name` = '" + projectName + "' && `user_id` = '" + userID + "'");
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
			ResultSet rs = checkColumns.executeQuery("SELECT * FROM `columns` WHERE `column_name` = '" + columnName + "' && `project_id` = '" + projectID + "'");
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
		String query = "SELECT `column_id`, `column_name`, `due_date`, `description` FROM `columns` WHERE `project_id` = '" + projectID + "'";
		ResultSet rs = loadColumnsStatement.executeQuery(query);
		
		while (rs.next()) {
			Column tmpColumn = new Column();
			tmpColumn.setProjectID(projectID);
			tmpColumn.setColumnID(rs.getInt(1));
			tmpColumn.setColumn_name(rs.getString(2));
			tmpColumn.setDue_date(rs.getDate(3));
			tmpColumn.setDescription(rs.getString(4));
			System.out.println("Column name: " + tmpColumn.getColumn_name());
			System.out.println("Due date: " + tmpColumn.getDue_date());
			System.out.println("Description: " + tmpColumn.getDescription());
			columns.add(tmpColumn);
			
			System.out.println("=============================");

		}
		
		return columns;
	}
	
	public void saveColumnChanges(int project_ID, int ColumnID, String columnName, Date dueDate, String description) throws SQLException {
		PreparedStatement ps_saveColumnChanges = baseDao.connect().prepareStatement("UPDATE `columns` SET `column_name` = ?, `due_date` = ?, `description` = ? WHERE `column_id` = ? AND `project_id` = ?");
		ps_saveColumnChanges.setString(1, columnName);
		ps_saveColumnChanges.setDate(2, dueDate);
		ps_saveColumnChanges.setString(3, description);
		ps_saveColumnChanges.setInt(4, ColumnID);
		ps_saveColumnChanges.setInt(5, project_ID);
		ps_saveColumnChanges.execute();
	}
	
	public void deleteColumn(int columnID) throws SQLException {
		try {
			//Connect to database
			Connection connection = baseDao.connect();
			
			//Delete column
			String queryDeleteColumn =  "DELETE FROM `columns` WHERE `column_id` = ?";
			PreparedStatement statementDeleteColumn = connection.prepareStatement(queryDeleteColumn);
			statementDeleteColumn.setInt(1, columnID);
			statementDeleteColumn.execute();
			//Delete tasks
			String queryDeleteTasks = "DELETE FROM `tasks` WHERE `column_id` = ?";
			PreparedStatement statementDeleteTasks = connection.prepareStatement(queryDeleteTasks);
			statementDeleteTasks.setInt(1, columnID);
			statementDeleteTasks.execute();
			
			
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	public boolean addTask(int columnID, int taskID, String description, boolean completed) throws SQLException {
		return false;
	}
	
	public ArrayList<Task> loadTasks(int columnID) throws SQLException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		Statement loadTasksStatement = baseDao.connect().createStatement();
		String query = "SELECT `task_id`, `column_id`, `task_name`, `description`, `due_date`, `completed` FROM `tasks` WHERE `column_id` = '" + columnID + "'";
		ResultSet rs = loadTasksStatement.executeQuery(query);
		
		while (rs.next()) {
			Task tmpTask = new Task();
			tmpTask.setTaskID(rs.getInt(1));
			tmpTask.setColumnID(rs.getInt(2));
			tmpTask.setTaskName(rs.getString(3));
			tmpTask.setDescription(rs.getString(4));
			tmpTask.setDueDate(rs.getDate(5));
			tmpTask.setCompleted(rs.getBoolean(6));
			
			tasks.add(tmpTask);
			
			System.out.println("=============================");

		}
		
		return tasks;
		
	}
	
	public void saveTaskChanges(int taskID, String description, boolean completed) throws SQLException {
		
	}
	
	public void deleteTask(int taskItemID) throws SQLException {
		
	}
}