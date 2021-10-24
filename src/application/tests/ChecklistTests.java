package application.tests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import application.Model;
import application.domains.ActionItem;
import application.domains.Checklist;
import application.domains.Task;

public class ChecklistTests {

	
	Model model = new Model();

	//Test if dup checklist can be added.
	
	/*
	 * There is no test if checklist can be
	 * added by itself because that is never
	 * an option in the program.
	 */
	@Test
	public void addChecklistDup() throws SQLException {
		boolean output = true;
		
		//Create task for the checklist,
		Task testTask = new Task();
		testTask.setColumnID(0);
		testTask.setTaskName("Test name");
		testTask.setDescription("Test description.");
		Date date = new Date(0);
		testTask.setDueDate(date);
		model.getProjectDAO().addTask(testTask.getColumnID(), testTask.getTaskName(), testTask.getDescription(), testTask.getDueDate(), testTask.isCompleted());
		
		//Get the newly added task from database.
		ArrayList<Task> tasks = model.getProjectDAO().loadTasks(testTask.getColumnID());
		Task newTask = tasks.get(tasks.size() - 1);
		
		//Create checklist template.
		Checklist testChecklist = new Checklist();
		testChecklist.setCheckListID(1);
		testChecklist.setTaskID(newTask.getTaskID());
		model.getProjectDAO().addCheckList(testChecklist.getTaskID());
		
		//Check if the duplicated checklist was added.
		tasks = model.getProjectDAO().loadTasks(testTask.getColumnID());
		for (Task task : tasks) {
			Checklist tmpChecklist = model.getProjectDAO().loadChecklist(task.getTaskID());
			if (tmpChecklist.getCheckListID() == testChecklist.getCheckListID()) {
				output = false;
			}
		}
		
		assertTrue(output);
		
		
	}
	
	
	//Test if delete checklist also deletes it's action items.
	@Test
	public void deleteChecklist() throws SQLException {
		boolean output = false;
		
		//Create checklist template.
		Checklist testChecklist = new Checklist();
		testChecklist.setCheckListID(1);
		testChecklist.setTaskID(1);
		model.getProjectDAO().addCheckList(testChecklist.getTaskID());
		
		//Get the new checklist.
		Checklist newChecklist = model.getProjectDAO().loadChecklist(testChecklist.getTaskID());
		
		//Create action item template.
		ActionItem actionItem = new ActionItem();
		actionItem.setcheckListID(newChecklist.getCheckListID());
		actionItem.setActionitemID(0);
		actionItem.setName("Test name");
		actionItem.setDescription("Test description");
		actionItem.setCompleted(false);		
		model.getProjectDAO().addActionItem(actionItem.getcheckListID(), actionItem.getName(), actionItem.getDescription(), actionItem.isCompleted());
		
		//Delete checklist.
		model.getProjectDAO().deleteCheckList(newChecklist.getCheckListID());
		
		//Check if action item was deleted.
		ArrayList<ActionItem> actionItems = model.getProjectDAO().loadActionItems(newChecklist.getCheckListID());
		if (actionItems.size() == 0) {
			output = true;
		}
		
		assertTrue(output);
		
	}

}
