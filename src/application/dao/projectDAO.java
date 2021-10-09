package application.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import application.objects.Column;
import application.objects.Project;

public interface projectDAO {
	
	public boolean addProject(int userID, String projectName) throws SQLException;
	public ArrayList<Project> loadProjects(int userID) throws SQLException;
	
	public void saveProjectChanges(String projectName) throws SQLException;
	public void deleteProject(int projectID) throws SQLException;
	
	public boolean addColumn(int projectID, String taskName, Date dueDate, String description) throws SQLException;
	public ArrayList<Column> loadColumns(int projectID) throws SQLException;
	
	public void saveColumnChanges(int taskColumnID, String taskName, Date dueDate, String description) throws SQLException;
	public void deleteColumn(int columnID) throws SQLException;
	
	public boolean addTask(int taskID, String description, boolean completed) throws SQLException;
	
	
	public void saveTaskChanges(int taskID, String description, boolean completed) throws SQLException;
	public void deleteTask(int taskItemID) throws SQLException;
}
