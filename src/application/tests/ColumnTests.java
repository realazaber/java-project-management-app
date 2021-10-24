package application.tests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import application.Model;
import application.domains.Column;
import application.domains.Project;
import application.domains.Task;
import application.domains.User;

public class ColumnTests {

	Model model = new Model();
	
	User user = new User();
	
	
	//Test if column can be added.
	@Test
	public void addColumnNoDup() throws SQLException {		
		boolean output = false;
		
		//Create test column.
		Column testColumn = new Column();
		testColumn.setProjectID(1);
		testColumn.setColumn_name("Test name");
		testColumn.setDescription("Test description");
		Date date = new Date(0);
		testColumn.setDue_date(date);
						
		//If column does not exist add it.
		if (model.getProjectDAO().addColumn(testColumn.getProjectID(), testColumn.getColumn_name(), testColumn.getDue_date(), testColumn.getDescription())) {
			
		}
		
		//Check if the column is in the database.
		ArrayList<Column> columns = model.getProjectDAO().loadColumns(testColumn.getProjectID());
		int columnID = 0;
		for (Column column : columns) {
			if (column.getColumn_name().equals(testColumn.getColumn_name())) {
				columnID = column.getColumnID();
				output = true;
			}
		}
		
		assertTrue(output);
		
		//Delete the added column.
		model.getProjectDAO().deleteColumn(columnID);
	}
	
	//Test if duplicate column can be added.
	@Test
	public void addColumnDup() throws SQLException {
		boolean output = false;
		
		//Create test column.
		Column testColumn = new Column();
		testColumn.setProjectID(1);
		testColumn.setColumn_name("Test name");
		testColumn.setDescription("Test description");
		Date date = new Date(0);
		testColumn.setDue_date(date);
		
		//Try to add the column twice.
		model.getProjectDAO().addColumn(testColumn.getProjectID(), testColumn.getColumn_name(), testColumn.getDue_date(), testColumn.getDescription());
		model.getProjectDAO().addColumn(testColumn.getProjectID(), testColumn.getColumn_name(), testColumn.getDue_date(), testColumn.getDescription());
		
		
		//Check if the column is twice in the database.
		ArrayList<Column> columns = model.getProjectDAO().loadColumns(testColumn.getProjectID());
		int nameCounter = 0;
		int columnID = 0;
		for (Column column : columns) {
			if (column.getColumn_name().equals(testColumn.getColumn_name())) {
				nameCounter++;
				columnID = column.getColumnID();
			}
		}
		
		//Make sure the column only got sent once.
		if (nameCounter == 1) {
			output = true;
		}
		
		assertTrue(output);
		
		//Delete the added column.
		model.getProjectDAO().deleteColumn(columnID);
	}
	
	//Test if column can be edited.
	@Test
	public void editColumn() throws SQLException {
		boolean output = false;
		
		//Add test column.
		Column testColumn = new Column();
		testColumn.setProjectID(0);
		testColumn.setColumn_name("Test name");
		testColumn.setDescription("Test description");
		Date date = new Date(0);
		testColumn.setDue_date(date);
		
		
		//Try to add the column.
		model.getProjectDAO().addColumn(testColumn.getProjectID(), testColumn.getColumn_name(), testColumn.getDue_date(), testColumn.getDescription());
				
		//Load column from database.
		ArrayList<Column> columns = model.getProjectDAO().loadColumns(testColumn.getProjectID());
		Column newColumn = columns.get(columns.size() - 1);
		
		//Change column name.
		model.getProjectDAO().saveColumnChanges(newColumn.getProjectID(), newColumn.getColumnID(), "Edited name", newColumn.getDue_date(), newColumn.getDescription());
		
		//Check if the changes were applied.
		columns = model.getProjectDAO().loadColumns(testColumn.getProjectID());
		if (columns.size() == 1) {
			for (Column column : columns) {
				if (column.getColumn_name().equals("Edited name")) {
					output = true;
				}
			}
		}
		assertTrue(output);
		model.getProjectDAO().deleteColumn(newColumn.getColumnID());
		
		
	}
	
	
	//Test if delete column also deletes it's tasks.
	@Test
	public void deleteColumn() throws SQLException {
		boolean output = true;
		
		//Add test column.
		Column testColumn = new Column();
		testColumn.setProjectID(0);
		testColumn.setColumn_name("Test name");
		testColumn.setDescription("Test description");
		Date date = new Date(0);
		testColumn.setDue_date(date);
		
		//Try to add the column.
		model.getProjectDAO().addColumn(testColumn.getProjectID(), testColumn.getColumn_name(), testColumn.getDue_date(), testColumn.getDescription());
		
		//Get the columns ID from database.
		ArrayList<Column> columns = model.getProjectDAO().loadColumns(testColumn.getProjectID());
		int columnID = 0;
		
		//Save its new column id.
		if (columns.size() == 1) {
			for (Column column : columns) {
				columnID = column.getColumnID();								
			}
		}
		else {
			fail();
		}

		
		//Create task and add it to column
		Task testTask = new Task();
		testTask.setColumnID(columnID);
		testTask.setTaskName("Test name");
		testTask.setDescription("Test description");
		testTask.setDueDate(date);
		testTask.setCompleted(false);
		
		model.getProjectDAO().addTask(testTask.getColumnID(), testTask.getTaskName(), testTask.getDescription(), testTask.getDueDate(), testTask.isCompleted());
		
		//Delete column.
		model.getProjectDAO().deleteColumn(testTask.getColumnID());
		
		//Get all column from database and their tasks.
		columns = model.getProjectDAO().loadColumns(testColumn.getProjectID());
		ArrayList<Task> tasks = model.getProjectDAO().loadTasks(testColumn.getColumnID());

		
		//If there are no columns and tasks left then the test is a success.
		if (columns.size() == 0 && tasks.size() == 1) {
			output = true;
		}
		else {
			output = false;
		}

		
		assertTrue(output);
		
	}
}
