package application.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import application.objects.Column;
import application.objects.Project;
import application.objects.Task;

public interface projectDAO {
	
	public boolean addProject(int userID, String projectName, boolean isDefault) throws SQLException;
	public ArrayList<Project> loadProjects(int userID) throws SQLException;
	
	public void saveProjectChanges(int projectID, int userID, String projectName, boolean isDefault) throws SQLException;
	public void deleteProject(int projectID) throws SQLException;
	
	public boolean addColumn(int projectID, String taskName, Date dueDate, String description) throws SQLException;
	public ArrayList<Column> loadColumns(int projectID) throws SQLException;
	
	public void saveColumnChanges(int project_ID, int ColumnID, String columnName, Date dueDate, String description) throws SQLException;
	public void deleteColumn(int columnID) throws SQLException;
	
	public boolean addTask(int columnID, String taskName, String description, Date dueDate, boolean completed) throws SQLException;
	public ArrayList<Task> loadTasks(int columnID) throws SQLException;
	public boolean tasksCompleted(int columnID) throws SQLException;
	
	public void saveTaskChanges(int taskID, String taskName, String description, Date dueDate, boolean completed) throws SQLException;
	public void deleteTask(int taskItemID) throws SQLException;
}
