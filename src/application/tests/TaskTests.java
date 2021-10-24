package application.tests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import application.Model;
import application.domains.Checklist;
import application.domains.Task;

public class TaskTests {
	
	Model model = new Model();

	//Test if task can be added.
	@Test
	public void addTaskNoDup() throws SQLException {		
		boolean output = false;
		
		//Create test task.
		Task testTask = new Task();
		testTask.setColumnID(0);
		testTask.setTaskName("Test name");
		testTask.setDescription("Test description.");
		Date date = new Date(0);
		testTask.setDueDate(date);
		
		//If task does not exist add it.
		if (model.getProjectDAO().addTask(testTask.getColumnID(), testTask.getTaskName(), testTask.getDescription(), testTask.getDueDate(), testTask.isCompleted())) {
			
		}
		
		//Check if task is in the database.
		ArrayList<Task> tasks = model.getProjectDAO().loadTasks(testTask.getColumnID());
		int taskID = 0;
		for (Task task : tasks) {
			
			if (task.getTaskName().equals(testTask.getTaskName())) {
				taskID = task.getTaskID();
				output = true;
			}
		}
		
		assertTrue(output);

		//Delete task.
		model.getProjectDAO().deleteTask(taskID);
		
		//Delete checklist.
		Checklist checklist = model.getProjectDAO().loadChecklist(taskID);
		model.getProjectDAO().deleteCheckList(checklist.getCheckListID());
	}
	
	//Test if duplicate task can be added.
	@Test
	public void addTaskDup() throws SQLException {
		boolean output = false;
		
		//Create test task.
		Task testTask = new Task();
		testTask.setColumnID(0);
		testTask.setTaskName("Test name");
		testTask.setDescription("Test description.");
		Date date = new Date(0);
		testTask.setDueDate(date);
		
		//try to add task twice.
		model.getProjectDAO().addTask(testTask.getColumnID(), testTask.getTaskName(), testTask.getDescription(), testTask.getDueDate(), testTask.isCompleted());
		model.getProjectDAO().addTask(testTask.getColumnID(), testTask.getTaskName(), testTask.getDescription(), testTask.getDueDate(), testTask.isCompleted());
		
		
		//Check if task is in the database.
		ArrayList<Task> tasks = model.getProjectDAO().loadTasks(testTask.getColumnID());
		int nameCounter = 0;
		int taskID = 0;
		for (Task task : tasks) {
			if (task.getTaskName().equals(testTask.getTaskName())) {
				nameCounter++;
				taskID = task.getTaskID();
			}
		}
		
		//Make sure the task only got sent once.
		if (nameCounter == 1) {
			output = true;
		}
		
		assertTrue(output);
		

		//Delete task.
		model.getProjectDAO().deleteTask(taskID);
		

		//Delete checklist.
		Checklist checklist = model.getProjectDAO().loadChecklist(taskID);
		model.getProjectDAO().deleteCheckList(checklist.getCheckListID());
	}
	
	//Test if edit task works.
	@Test
	public void editTask() throws SQLException {
		boolean output = false;
		
		//Create test task.
		Task testTask = new Task();
		testTask.setColumnID(0);
		testTask.setTaskName("Test name");
		testTask.setDescription("Test description.");
		Date date = new Date(0);
		testTask.setDueDate(date);
		
		//try to add task.
		model.getProjectDAO().addTask(testTask.getColumnID(), testTask.getTaskName(), testTask.getDescription(), testTask.getDueDate(), testTask.isCompleted());
		
		//Load task from database.
		ArrayList<Task> tasks = model.getProjectDAO().loadTasks(testTask.getColumnID());
		Task newTask = tasks.get(tasks.size() - 1);
		
		//Change task name.
		model.getProjectDAO().saveTaskChanges(newTask.getTaskID(), "Edited name", newTask.getDescription(), newTask.getDueDate(), newTask.isCompleted());
		
		//Check if the changes were applied.
		tasks = model.getProjectDAO().loadTasks(testTask.getColumnID());
		if (tasks.size() == 1) {
			for (Task task : tasks) {
				if (task.getTaskName().equals("Edited name")) {
					output = true;
				}
			}
		}
		
		assertTrue(output);
		
		//Delete task.
		model.getProjectDAO().deleteTask(newTask.getTaskID());
		

		//Delete checklist.
		Checklist checklist = model.getProjectDAO().loadChecklist(newTask.getTaskID());
		model.getProjectDAO().deleteCheckList(checklist.getCheckListID());
	}
	
	//Test if delete task also deletes it's checklists.
	@Test
	public void deleteTask() throws SQLException {
		boolean output = false;
		
		//Create test task.
		Task testTask = new Task();
		testTask.setColumnID(0);
		testTask.setTaskName("Test name");
		testTask.setDescription("Test description.");
		Date date = new Date(0);
		testTask.setDueDate(date);
		
		//try to add task.
		model.getProjectDAO().addTask(testTask.getColumnID(), testTask.getTaskName(), testTask.getDescription(), testTask.getDueDate(), testTask.isCompleted());
		
		//Load task from database.
		ArrayList<Task> tasks = model.getProjectDAO().loadTasks(testTask.getColumnID());
		Task newTask = tasks.get(tasks.size() - 1);
		
		//Create test checklist.
		Checklist testChecklist = new Checklist();
		testChecklist.setTaskID(newTask.getTaskID());
		model.getProjectDAO().addCheckList(newTask.getTaskID());
		
		//Delete task.
		model.getProjectDAO().deleteTask(newTask.getTaskID());
		
		//Delete checklist.
		Checklist newChecklist = model.getProjectDAO().loadChecklist(newTask.getTaskID());
		model.getProjectDAO().deleteCheckList(newChecklist.getCheckListID());
		
		//Get all tasks from database and their checklist.
		tasks = model.getProjectDAO().loadTasks(testTask.getColumnID());
		
		newChecklist = model.getProjectDAO().loadChecklist(newTask.getTaskID());
		
		if (tasks.size() == 0 && newChecklist.getCheckListID() == 0) {
			output = true;
		}
		
		assertTrue(output);
		
		//Delete task.
		model.getProjectDAO().deleteTask(newTask.getTaskID());
		

		//Delete checklist.
		Checklist checklist = model.getProjectDAO().loadChecklist(newTask.getTaskID());
		model.getProjectDAO().deleteCheckList(checklist.getCheckListID());
	}
}
