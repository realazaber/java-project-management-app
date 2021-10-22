package application.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.validator.PublicClassValidator;

import application.controllers.add.newProjectController;
import application.domains.*;

public class projectDAOImpl implements projectDAO {
	
	baseDao baseDao = new baseDao();
	
	Connection connection = baseDao.connect();
	
	public projectDAOImpl() {
		
	}
	
	public boolean addProject(int userID, String projectName, boolean isDefault) throws SQLException {
		try {
			Statement checkProjects = connection.createStatement();
			ResultSet rs = checkProjects.executeQuery("SELECT * FROM `projects` WHERE `project_name` = '" + projectName + "' && `user_id` = '" + userID + "'");
			if (!rs.next()) {
				
				if (isDefault) {
					//Change others to non default
					PreparedStatement ps_checkForDefault = connection.prepareStatement("UPDATE `projects` SET `is_default` = 0 WHERE `user_id` = '" + userID + "'");
					ps_checkForDefault.execute();
				}
				
				
				//Add project
				String queryAddProject = "INSERT INTO `projects` (`project_id`, `user_id`, `project_name`, `is_default`) VALUES (null, ?, ?, ?)";
				PreparedStatement ps_addProject = connection.prepareStatement(queryAddProject);
				ps_addProject.setInt(1, userID);
				ps_addProject.setString(2, projectName);
				ps_addProject.setBoolean(3, isDefault);
				ps_addProject.execute();
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
		
		Statement loadProjectStatement = connection.createStatement();
		String query = "SELECT `project_id`, `user_id`, `project_name`, `is_default` FROM `projects` WHERE `user_id` = '" + userID + "'";
		ResultSet rs = loadProjectStatement.executeQuery(query);
		
		while (rs.next()) {
			Project tmpProject = new Project();
			tmpProject.setProjectID(rs.getInt(1));
			tmpProject.setUserID(rs.getInt(2));
			tmpProject.setProjectName(rs.getString(3));
			tmpProject.setDefault(rs.getBoolean(4));
			projects.add(tmpProject);
			
			System.out.println("=============================");
			System.out.println("Project ID: " + tmpProject.getProjectID());
			System.out.println("Project Name: " + tmpProject.getProjectName());
			System.out.println("Project user ID: " + tmpProject.getUserID());
		}
		
		
		return projects;
		
	}
	
	public void saveProjectChanges(int projectID, int userID, String projectName, boolean isDefault) throws SQLException {
		
		Statement checkProjects = connection.createStatement();
		
		if (isDefault) {

			ResultSet rs_checkProjects = checkProjects.executeQuery("SELECT * FROM `projects` WHERE `project_name` = '" + projectName + "' && `user_id` = '" + userID + "' && `is_default` = true");
			if (!rs_checkProjects.next()) {
				//Change others to non default
				PreparedStatement ps_checkForDefault = connection.prepareStatement("UPDATE `projects` SET `is_default` = 0 WHERE `user_id` = '" + userID + "'");
				ps_checkForDefault.execute();
			}
		}
		
		
		
		PreparedStatement ps_saveProjectChanges = connection.prepareStatement("UPDATE `projects` SET `project_name` = ?, `is_default` = ? WHERE `project_id` = ?");
		ps_saveProjectChanges.setString(1, projectName);
		ps_saveProjectChanges.setBoolean(2, isDefault);
		ps_saveProjectChanges.setInt(3, projectID);
		ps_saveProjectChanges.execute();
	}
	
	public void deleteProject(int projectID) throws SQLException {
		try {
			
			//Delete project
			String queryDeleteProject =  "DELETE FROM `projects` WHERE `projects`.`project_id` = ?";
			PreparedStatement statementDeleteProject = connection.prepareStatement(queryDeleteProject);
			statementDeleteProject.setInt(1, projectID);
			statementDeleteProject.execute();
			
			//Delete columns and tasks that are connected to the project.
			ArrayList<Column> columns = loadColumns(projectID);
			
			for (Column column : columns) {
				deleteColumn(column.getColumnID());
				ArrayList<Task> tasks = loadTasks(column.getColumnID());
				for (Task task : tasks) {				
					deleteTask(task.getTaskID());
				}
			}
			
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	public boolean addColumn(int projectID, String columnName, Date dueDate, String description) throws SQLException {
		
		try {
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
		} 
		catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return false;
	}
	
	public ArrayList<Column> loadColumns(int projectID) throws SQLException {
		System.out.println("Loading columns for project " + projectID);
		ArrayList<Column> columns = new ArrayList<Column>();
		
		Statement loadColumnsStatement = connection.createStatement();
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
		PreparedStatement ps_saveColumnChanges = connection.prepareStatement("UPDATE `columns` SET `column_name` = ?, `due_date` = ?, `description` = ? WHERE `column_id` = ? AND `project_id` = ?");
		ps_saveColumnChanges.setString(1, columnName);
		ps_saveColumnChanges.setDate(2, dueDate);
		ps_saveColumnChanges.setString(3, description);
		ps_saveColumnChanges.setInt(4, ColumnID);
		ps_saveColumnChanges.setInt(5, project_ID);
		ps_saveColumnChanges.execute();
	}
	
	public Column searchColumn(int columnID) throws SQLException {
		Column column = new Column();
		
		Statement findColumnStatement = connection.createStatement();
		String query = "SELECT `column_id`, `project_id`, `column_name`, `due_date`, `description` FROM `columns` WHERE `column_id` = '" + columnID + "'";
		ResultSet rs = findColumnStatement.executeQuery(query);
		
		while (rs.next()) {

			column.setColumnID(rs.getInt(1));
			column.setProjectID(rs.getInt(2));
			column.setColumn_name(rs.getString(3));
			column.setDue_date(rs.getDate(4));
			column.setDescription(rs.getString(5));


		}
		
		return column;
	}
	
	
	public void deleteColumn(int columnID) throws SQLException {
		try {
			
			//Delete column
			String queryDeleteColumn =  "DELETE FROM `columns` WHERE `column_id` = ?";
			PreparedStatement statementDeleteColumn = connection.prepareStatement(queryDeleteColumn);
			statementDeleteColumn.setInt(1, columnID);
			statementDeleteColumn.execute();
			//Delete tasks
			
			ArrayList<Task> tasks = loadTasks(columnID);
			for (Task task : tasks) {
				deleteTask(task.getTaskID());
				Checklist tmpChecklist = loadChecklist(task.getTaskID());
				deleteCheckList(tmpChecklist.getCheckListID());
			}
			
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	public boolean addTask(int columnID, String taskName, String description, Date dueDate, boolean completed) throws SQLException {
		try {
			Statement checkTasks = connection.createStatement();
			ResultSet rs_checkTasks = checkTasks.executeQuery("SELECT * FROM `tasks` WHERE `task_name` = '" + taskName + "' && `column_id` = '" + columnID + "'");
			if (!rs_checkTasks.next()) {
				String query = "INSERT INTO `tasks` (`task_id`, `column_id`, `task_name`, `description`, `due_date`, `completed`) VALUES (null, ?, ?, ?, ?, ?)";
				PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, columnID);
				statement.setString(2, taskName);
				statement.setString(3, description);
				statement.setDate(4, dueDate);
				statement.setBoolean(5, completed);				
				statement.execute();
				
				ResultSet rs_newTaskID = statement.getGeneratedKeys();
				int newTaskID = 0;
				if (rs_newTaskID.next()) {
					newTaskID = rs_newTaskID.getInt(1);
				}
				
				addCheckList(newTaskID);
				
				return true;			
			}
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return false;
	}
	
	public ArrayList<Task> loadTasks(int columnID) throws SQLException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		Statement loadTasksStatement = connection.createStatement();
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

		}
		
		return tasks;
		
	}
	
	public boolean tasksCompleted(int columnID) throws SQLException {
		ArrayList<Task> tasks = loadTasks(columnID);
		boolean allComplete = true;
		for (Task task : tasks) {
			if (task.isCompleted() == false) {
				return false;
			}
			allComplete = false;
			Checklist tmpChecklist = loadChecklist(task.getTaskID());
			ArrayList<ActionItem> tmpActionItems = loadActionItems(tmpChecklist.getCheckListID());
			for (ActionItem actionItem : tmpActionItems) {
				if (actionItem.isCompleted() == false) {
					return false;
				}
			}
		}
				
		return true;
	}
	
	public void saveTaskChanges(int taskID, String taskName, String description, Date dueDate, boolean completed) throws SQLException {
		PreparedStatement ps_saveTaskChanges = connection.prepareStatement("UPDATE `tasks` SET `task_name` = ?, `description` = ?, `due_date` = ?, `completed` = ? WHERE `task_id` = ?");
		ps_saveTaskChanges.setString(1, taskName);
		ps_saveTaskChanges.setString(2, description);
		ps_saveTaskChanges.setDate(3, dueDate);
		ps_saveTaskChanges.setBoolean(4, completed);
		ps_saveTaskChanges.setInt(5, taskID);
		ps_saveTaskChanges.execute();
	}
	

	public void changeTaskColumn(int taskID, int columnID) throws SQLException {
		PreparedStatement ps_changeTaskColumn = connection.prepareStatement("UPDATE `tasks` SET `column_id` = ? WHERE `task_id` = ?");
		ps_changeTaskColumn.setInt(1, columnID);
		ps_changeTaskColumn.setInt(2, taskID);
		ps_changeTaskColumn.execute();
	}
	

	
	public void deleteTask(int taskID) throws SQLException {
		try {
			//Delete columns
			String queryDeleteTasks = "DELETE FROM `tasks` WHERE `task_id` = ?";
			PreparedStatement statementDeleteTasks = connection.prepareStatement(queryDeleteTasks);
			statementDeleteTasks.setInt(1, taskID);
			statementDeleteTasks.execute();
			
			deleteCheckList(taskID);
			
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	public boolean addCheckList(int taskID) {
		
		try {
			Statement checkTasks = connection.createStatement();
			ResultSet rs_checkTasks = checkTasks.executeQuery("SELECT * FROM `checklists` WHERE `task_id` = '" + taskID + "'");
			if (!rs_checkTasks.next()) {
				String query = "INSERT INTO `checklists` (`checklist_id`, `task_id`) VALUES (null, ?)";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, taskID);
				statement.execute();
				return true;
				
			}
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		
		
		
		return false;
	}
	
	public Checklist loadChecklist(int taskID) throws SQLException {
		
		Checklist checklist = new Checklist();
		
		Statement loadChecklistStatement = connection.createStatement();
		String query = "SELECT `checklist_id`, `task_id` FROM `checklists` WHERE `task_id` = '" + taskID + "'";
		ResultSet rs_loadChecklist = loadChecklistStatement.executeQuery(query);
		
		while (rs_loadChecklist.next()) {
			checklist.setCheckListID(rs_loadChecklist.getInt(1));
			checklist.setTaskID(rs_loadChecklist.getInt(2));			
		}
		
		
		return checklist;
	}
	
	public void deleteCheckList(int checkListID) {
		try {
			//Delete checklist
			String queryDeleteChecklist = "DELETE FROM `checklists` WHERE `checklist_id` = ?";
			PreparedStatement statementDeleteChecklist = connection.prepareStatement(queryDeleteChecklist);
			statementDeleteChecklist.setInt(1, checkListID);
			statementDeleteChecklist.execute();
			
			//Delete action items
			ArrayList<ActionItem> actionItems = loadActionItems(checkListID);
			for (ActionItem actionItem : actionItems) {
				deleteActionItem(actionItem.getActionitemID());
			}
			
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	public boolean addActionItem(int checkListID, String name, String description, boolean completed) {
		try {
			Statement checkTasks = connection.createStatement();
			ResultSet rs_checkTasks = checkTasks.executeQuery("SELECT * FROM `action_items` WHERE `checklist_id` = '" + checkListID + "' AND `name` = '" + name + "'");
			if (!rs_checkTasks.next()) {
				String query = "INSERT INTO `action_items` (`actionitem_id`, `checklist_id`, `name`, `description`, `completed`) VALUES (null, ?, ?, ?, ?)";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, checkListID);
				statement.setString(2, name);
				statement.setString(3, description);
				statement.setBoolean(4, completed);
				statement.execute();
				return true;
				
			}
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
				
		return false;
	}
	
	public ArrayList<ActionItem> loadActionItems(int checkListID) throws SQLException {
		
		ArrayList<ActionItem> actionItems = new ArrayList<ActionItem>();
		
		Statement loadActionItemsStatement = connection.createStatement();
		String query = "SELECT `actionitem_id`, `checklist_id`, `name`, `description`, `completed` FROM `action_items` WHERE `checklist_id` = '" + checkListID + "'";
		ResultSet rs_actionItems = loadActionItemsStatement.executeQuery(query);
		
		while (rs_actionItems.next()) {
			
			ActionItem tmpActionItem = new ActionItem();
			tmpActionItem.setActionitemID(rs_actionItems.getInt(1));
			tmpActionItem.setcheckListID(rs_actionItems.getInt(2));
			tmpActionItem.setName(rs_actionItems.getString(3));
			tmpActionItem.setDescription(rs_actionItems.getString(4));
			tmpActionItem.setCompleted(rs_actionItems.getBoolean(5));
			
			actionItems.add(tmpActionItem);
			
		}			
		
		return actionItems;
	}
	
	public void changeActionItem(int actionItemID, String name, String description, boolean completed) throws SQLException {
		PreparedStatement ps_saveActionItemChanges = connection.prepareStatement("UPDATE `action_items` SET `name` = ?, `description` = ?, `completed` = ? WHERE `actionitem_id` = ?");
		ps_saveActionItemChanges.setString(1, name);
		ps_saveActionItemChanges.setString(2, description);
		ps_saveActionItemChanges.setBoolean(3, completed);
		ps_saveActionItemChanges.setInt(4, actionItemID);

		ps_saveActionItemChanges.execute();
	}
	
	public void deleteActionItem(int actionItemID) {
		try {
			//Delete action item.
			String queryDeleteActionItem = "DELETE FROM `action_items` WHERE `actionitem_id` = ?";
			PreparedStatement statementDeleteActionItem = connection.prepareStatement(queryDeleteActionItem);
			statementDeleteActionItem.setInt(1, actionItemID);
			statementDeleteActionItem.execute();			
		} 
		catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
}