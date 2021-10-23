package application.dao;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import application.domains.*;

public class projectDAOImpl implements projectDAO {
	
	baseDao baseDao = new baseDao();
	
	Connection connection = baseDao.connect();
	
	public projectDAOImpl() {
		
	}
	
	public boolean addProject(int userID, String projectName, boolean isDefault) throws SQLException {
		System.out.println("Adding project to user " + userID);
		try {
			String query = "SELECT * FROM `projects` WHERE `project_name` = '" + projectName + "' && `user_id` = '" + userID + "'";				
			PreparedStatement checkProjects = connection.prepareStatement(query);
			ResultSet rs = checkProjects.executeQuery();
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
				ps_addProject.executeUpdate();
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
		try {
			ArrayList<Project> projects = new ArrayList<Project>();
			
			String query = "SELECT `project_id`, `user_id`, `project_name`, `is_default` FROM `projects` WHERE `user_id` = '" + userID + "'";
			PreparedStatement loadProjectStatement = connection.prepareStatement(query);
			
			ResultSet rs = loadProjectStatement.executeQuery();
			
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
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return null;
	}
	
	public void saveProjectChanges(int projectID, int userID, String projectName, boolean isDefault) throws SQLException {
		System.out.println("Saving project changes for project " + projectID);
		
		try {
			String query = "SELECT * FROM `projects` WHERE `project_name` = '" + projectName + "' && `user_id` = '" + userID + "' && `is_default` = true";
			PreparedStatement checkProjects = connection.prepareStatement(query);
			
			if (isDefault) {

				ResultSet rs_checkProjects = checkProjects.executeQuery();
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
		catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		

	}
	
	public void deleteProject(int projectID) throws SQLException {
		System.out.println("Deleting project " + projectID);
		
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
		} 
		catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	//Add column to project.
	public boolean addColumn(int projectID, String columnName, Date dueDate, String description) throws SQLException {
		
		try {
			
			//Check if column already exists.
			String query = "SELECT * FROM `columns` WHERE `column_name` = '" + columnName + "' && `project_id` = '" + projectID + "'";
			PreparedStatement checkColumns = connection.prepareStatement(query);
			ResultSet rs = checkColumns.executeQuery();
			
			//If the column does not exist then add it to the project.
			if (!rs.next()) {
				String queryInsertColumn = "INSERT INTO `columns` (`column_id`, `project_id`, `column_name`, `due_date`, `description`) VALUES (null, ?, ?, ?, ?)";
				PreparedStatement statement = connection.prepareStatement(queryInsertColumn);
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
	
	//Load columns for a project.
	public ArrayList<Column> loadColumns(int projectID) throws SQLException {
		System.out.println("Loading columns for project " + projectID);
		
		try {
			//Make a list for the columns.
			ArrayList<Column> columns = new ArrayList<Column>();
			
			//Prepare query to load teh columns.
			String query = "SELECT `column_id`, `column_name`, `due_date`, `description` FROM `columns` WHERE `project_id` = '" + projectID + "'";
			PreparedStatement loadColumnsStatement = connection.prepareStatement(query);
			
			//For each column found add them to the list.
			ResultSet rs_loadColumns = loadColumnsStatement.executeQuery();
			while (rs_loadColumns.next()) {
				Column tmpColumn = new Column();
				tmpColumn.setProjectID(projectID);
				tmpColumn.setColumnID(rs_loadColumns.getInt(1));
				tmpColumn.setColumn_name(rs_loadColumns.getString(2));
				tmpColumn.setDue_date(rs_loadColumns.getDate(3));
				tmpColumn.setDescription(rs_loadColumns.getString(4));
				System.out.println("Column name: " + tmpColumn.getColumn_name());
				System.out.println("Due date: " + tmpColumn.getDue_date());
				System.out.println("Description: " + tmpColumn.getDescription());
				columns.add(tmpColumn);
			}
			
			return columns;
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return null;
	}
	
	//Save changes to column.
	public void saveColumnChanges(int project_ID, int columnID, String columnName, Date dueDate, String description) throws SQLException {
		
		System.out.println("Searching for column " + columnID + ": " + columnName);
		
		//Apply changes to column.
		try {
			PreparedStatement ps_saveColumnChanges = connection.prepareStatement("UPDATE `columns` SET `column_name` = ?, `due_date` = ?, `description` = ? WHERE `column_id` = ? AND `project_id` = ?");
			ps_saveColumnChanges.setString(1, columnName);
			ps_saveColumnChanges.setDate(2, dueDate);
			ps_saveColumnChanges.setString(3, description);
			ps_saveColumnChanges.setInt(4, columnID);
			ps_saveColumnChanges.setInt(5, project_ID);
			ps_saveColumnChanges.execute();
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	//Search for column by id.
	public Column searchColumn(int columnID) throws SQLException {
		System.out.println("Searching for column " + columnID);
		try {
			//Make template for column.
			Column column = new Column();
			
			//Prepare query to find column.
			Statement findColumnStatement = connection.createStatement();
			String query = "SELECT `column_id`, `project_id`, `column_name`, `due_date`, `description` FROM `columns` WHERE `column_id` = '" + columnID + "'";
			ResultSet rs = findColumnStatement.executeQuery(query);
			
			//Save the found column details to column template.
			while (rs.next()) {
				column.setColumnID(rs.getInt(1));
				column.setProjectID(rs.getInt(2));
				column.setColumn_name(rs.getString(3));
				column.setDue_date(rs.getDate(4));
				column.setDescription(rs.getString(5));
			}
			
			return column;
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return null;
	}
	
	//Delete column
	public void deleteColumn(int columnID) throws SQLException {
		System.out.println("Deleting column " + columnID);
		try {			
			//Delete column
			String queryDeleteColumn = "DELETE FROM `columns` WHERE `column_id` = ?";
			PreparedStatement statementDeleteColumn = connection.prepareStatement(queryDeleteColumn);
			statementDeleteColumn.setInt(1, columnID);
			statementDeleteColumn.execute();
			
			//Delete tasks linked to column			
			ArrayList<Task> tasks = loadTasks(columnID);
			for (Task task : tasks) {
				deleteTask(task.getTaskID());
				
				//Delete checklist linked to task.
				Checklist tmpChecklist = loadChecklist(task.getTaskID());
				deleteCheckList(tmpChecklist.getCheckListID());
			}
			
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	//Add task to column
	public boolean addTask(int columnID, String taskName, String description, Date dueDate, boolean completed) throws SQLException {
		System.out.println("Adding task " + taskName + "to column " + columnID);
		
		try {
			//Check if task already exists.
			String query = "SELECT * FROM `tasks` WHERE `task_name` = '" + taskName + "' && `column_id` = '" + columnID + "'";
			PreparedStatement checkTasks = connection.prepareStatement(query);		
			ResultSet rs_checkTasks = checkTasks.executeQuery();
			
			//If task doesn't already exist then add it to the column.
			if (!rs_checkTasks.next()) {
				String queryNewTask = "INSERT INTO `tasks` (`task_id`, `column_id`, `task_name`, `description`, `due_date`, `completed`) VALUES (null, ?, ?, ?, ?, ?)";
				PreparedStatement statement = connection.prepareStatement(queryNewTask, Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, columnID);
				statement.setString(2, taskName);
				statement.setString(3, description);
				statement.setDate(4, dueDate);
				statement.setBoolean(5, completed);				
				statement.execute();
				
				//Add checklist to the new task.
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
	
	//Load tasks linked to a column.
	public ArrayList<Task> loadTasks(int columnID) throws SQLException {
		
		System.out.println("Loading tasks for column " + columnID);
		try {
			//Create list for the tasks
			ArrayList<Task> tasks = new ArrayList<Task>();
			
			//Prepare query to retrieve the tasks
			String query = "SELECT `task_id`, `column_id`, `task_name`, `description`, `due_date`, `completed` FROM `tasks` WHERE `column_id` = '" + columnID + "'";
			PreparedStatement loadTasksStatement = connection.prepareStatement(query);
			
			//Get the tasks and add them to the task list.
			ResultSet rs_loadTasks = loadTasksStatement.executeQuery();			
			while (rs_loadTasks.next()) {
				Task tmpTask = new Task();
				tmpTask.setTaskID(rs_loadTasks.getInt(1));
				tmpTask.setColumnID(rs_loadTasks.getInt(2));
				tmpTask.setTaskName(rs_loadTasks.getString(3));
				tmpTask.setDescription(rs_loadTasks.getString(4));
				tmpTask.setDueDate(rs_loadTasks.getDate(5));
				tmpTask.setCompleted(rs_loadTasks.getBoolean(6));
				
				tasks.add(tmpTask);
			}
			
			return tasks;
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}	
		return null;
	}
	
	//Check if all tasks are completed in a column.
	public boolean tasksCompleted(int columnID) throws SQLException {
		System.out.println("Checking if tasks are completed for column " + columnID);
		
		try {
			//Load all the tasks in the column.
			ArrayList<Task> tasks = loadTasks(columnID);
			boolean allComplete = true;
			
			//Check each task if they are complete.
			for (Task task : tasks) {
				//If one task is not task is not complete then the column is incomplete.
				if (task.isCompleted() == false) {
					return false;
				}
				
				//Check if all action items are complete.
				Checklist tmpChecklist = loadChecklist(task.getTaskID());
				ArrayList<ActionItem> tmpActionItems = loadActionItems(tmpChecklist.getCheckListID());
				/*
				 * If there are action items 
				 * and they are all complete 
				 * then that task is complete.
				 */
				if (tmpActionItems.size() > 0) {
					for (ActionItem actionItem : tmpActionItems) {
						if (actionItem.isCompleted() == false) {
							return false;
						}
					}
				}
				/*
				 * If there are no action items 
				 * then the task is complete.
				 */
				else {
					allComplete = true;
				}
			}
					
			return true;
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return false;
	}
	
	//Save task changes.
	public void saveTaskChanges(int taskID, String taskName, String description, Date dueDate, boolean completed) throws SQLException {
		System.out.println("Saving task changes to " + taskID);
		
		try {
			//Apply the changes to the task in the database.
			PreparedStatement ps_saveTaskChanges = connection.prepareStatement("UPDATE `tasks` SET `task_name` = ?, `description` = ?, `due_date` = ?, `completed` = ? WHERE `task_id` = ?");
			ps_saveTaskChanges.setString(1, taskName);
			ps_saveTaskChanges.setString(2, description);
			ps_saveTaskChanges.setDate(3, dueDate);
			ps_saveTaskChanges.setBoolean(4, completed);
			ps_saveTaskChanges.setInt(5, taskID);
			ps_saveTaskChanges.execute();
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	//Change the column the task is in
	public void changeTaskColumn(int taskID, int columnID) throws SQLException {
		System.out.println("Movving task " + taskID + " to column " + columnID);
		
		try {
			//Move the task to another column by changing its column id.
			PreparedStatement ps_changeTaskColumn = connection.prepareStatement("UPDATE `tasks` SET `column_id` = ? WHERE `task_id` = ?");
			ps_changeTaskColumn.setInt(1, columnID);
			ps_changeTaskColumn.setInt(2, taskID);
			ps_changeTaskColumn.execute();
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}	
	}
	
	//Delete task
	public void deleteTask(int taskID) throws SQLException {
		System.out.println("Deleting task " + taskID);
		
		try {
			//Delete task
			String queryDeleteTasks = "DELETE FROM `tasks` WHERE `task_id` = ?";
			PreparedStatement statementDeleteTasks = connection.prepareStatement(queryDeleteTasks);
			statementDeleteTasks.setInt(1, taskID);
			statementDeleteTasks.execute();
			
			//Delete checklist linked to this task.
			deleteCheckList(taskID);
			
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	//Add checklist to database.
	public boolean addCheckList(int taskID) throws SQLException {
		System.out.println("Adding checklist to task " + taskID);
		
		try {
			//Check if checklist already exists.
			String query = "SELECT * FROM `checklists` WHERE `task_id` = '" + taskID + "'";
			PreparedStatement checkTasks = connection.prepareStatement(query);
	
			//If checklist does not already exist then add it to the database.
			ResultSet rs_checkTasks = checkTasks.executeQuery();
			if (!rs_checkTasks.next()) {
				String queryNewChecklist = "INSERT INTO `checklists` (`checklist_id`, `task_id`) VALUES (null, ?)";
				PreparedStatement statement = connection.prepareStatement(queryNewChecklist);
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
	
	//Get the checklist.
	public Checklist loadChecklist(int taskID) throws SQLException {
		System.out.println("Load checklist for task " + taskID);
		
		try {
			//Create a template for the chekclist.
			Checklist checklist = new Checklist();
			
			//Prepare query to find the check list in the database.
			String query = "SELECT `checklist_id`, `task_id` FROM `checklists` WHERE `task_id` = '" + taskID + "'";
			PreparedStatement loadChecklistStatement = connection.prepareStatement(query);
			
			//Find the check list in the database.
			ResultSet rs_loadChecklist = loadChecklistStatement.executeQuery();			
			while (rs_loadChecklist.next()) {
				/*
				 * Assign the values found to the checklist found and
				 * return it.
				 */
				checklist.setCheckListID(rs_loadChecklist.getInt(1));
				checklist.setTaskID(rs_loadChecklist.getInt(2));			
			}
						
			return checklist;
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return null;
	}
	
	//Delete checklist.
	public void deleteCheckList(int checkListID) throws SQLException {
		System.out.println("Delete checklist " + checkListID);
		
		try {
			//Delete checklist.
			String queryDeleteChecklist = "DELETE FROM `checklists` WHERE `checklist_id` = ?";
			PreparedStatement statementDeleteChecklist = connection.prepareStatement(queryDeleteChecklist);
			statementDeleteChecklist.setInt(1, checkListID);
			statementDeleteChecklist.execute();
			
			//Delete action items in the checklist.
			ArrayList<ActionItem> actionItems = loadActionItems(checkListID);
			for (ActionItem actionItem : actionItems) {
				deleteActionItem(actionItem.getActionitemID());
			}
			
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
	
	//Create new action item.
	public boolean addActionItem(int checkListID, String name, String description, boolean completed) throws SQLException {
		System.out.println("Adding action item");
		
		try {
			//First check if the action item already exists.
			String query = "SELECT * FROM `action_items` WHERE `checklist_id` = '" + checkListID + "' AND `name` = '" + name + "'";
			PreparedStatement checkTasks = connection.prepareStatement(query);
			ResultSet rs_checkTasks = checkTasks.executeQuery();
			
			//if the action item doesn't exist then add it to the database.
			if (!rs_checkTasks.next()) {
				String queryNewActionItem = "INSERT INTO `action_items` (`actionitem_id`, `checklist_id`, `name`, `description`, `completed`) VALUES (null, ?, ?, ?, ?)";
				PreparedStatement statement = connection.prepareStatement(queryNewActionItem);
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
	
	//Load action items.
	public ArrayList<ActionItem> loadActionItems(int checkListID) throws SQLException {
		System.out.println("Loading action items for checklist " + checkListID);
		
		try {
			//Prepare the arraylist for the action items.
			ArrayList<ActionItem> actionItems = new ArrayList<ActionItem>();
			
			String query = "SELECT `actionitem_id`, `checklist_id`, `name`, `description`, `completed`h FROM `action_items` WHERE `checklist_id` = '" + checkListID + "'";
			PreparedStatement loadActionItemsStatement = connection.prepareStatement(query);
			
			ResultSet rs_actionItems = loadActionItemsStatement.executeQuery();
			
			//For each action item found add it to the array list.
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
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return null;

	}
	
	//Save changes to action item.
	public void changeActionItem(int actionItemID, String name, String description, boolean completed) throws SQLException {
		System.out.println("Saving changes to action item " + actionItemID);
		
		//Save changes to the action item.
		try {
			String query = "UPDATE `action_items` SET `name` = ?, `description` = ?, `completed` = ? WHERE `actionitem_id` = ?";
			PreparedStatement ps_saveActionItemChanges = connection.prepareStatement(query);
			ps_saveActionItemChanges.setString(1, name);
			ps_saveActionItemChanges.setString(2, description);
			ps_saveActionItemChanges.setBoolean(3, completed);
			ps_saveActionItemChanges.setInt(4, actionItemID);

			ps_saveActionItemChanges.execute();
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		
	}
	
	//Delete action item
	public void deleteActionItem(int actionItemID) throws SQLException {
		System.out.println("Deleting action item " + actionItemID);
		
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