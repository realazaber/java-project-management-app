package application.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import application.Model;
import application.domains.ActionItem;
import application.domains.Checklist;

public class ActionitemTests {

	Model model = new Model();

	//Test if task can be added.
	@Test
	public void addActionItemNoDup() throws SQLException {
		boolean output = false;
		
		//Add checklist
		Checklist testChecklist = new Checklist();
		testChecklist.setTaskID(1);
		model.getProjectDAO().addCheckList(testChecklist.getTaskID());
		
		int actionItemID = 0;
		//Clear action items.
		ArrayList<ActionItem> actionItems = model.getProjectDAO().loadActionItems(testChecklist.getCheckListID());
		if (actionItems.size() == 0) {
			//Create test action item.
			ActionItem testActionItem = new ActionItem();
			testActionItem.setcheckListID(testChecklist.getCheckListID());
			testActionItem.setName("Test name");
			testActionItem.setDescription("Test description");
			testActionItem.setCompleted(false);
			model.getProjectDAO().addActionItem(testActionItem.getcheckListID(), testActionItem.getName(), testActionItem.getDescription(), testActionItem.isCompleted());
			
			//Check if action item is in database.
			actionItems = model.getProjectDAO().loadActionItems(testChecklist.getCheckListID());
			
			for (ActionItem actionItem : actionItems) {
				if (actionItem.getName().equals(testActionItem.getName())) {
					output = true;
					actionItemID = actionItem.getActionitemID();
				}
			}
			
			
		}
		else {
			fail();
		}
		
		assertTrue(output);
		
		Checklist newChecklist = model.getProjectDAO().loadChecklist(testChecklist.getTaskID());
		
		model.getProjectDAO().deleteActionItem(actionItemID);
		model.getProjectDAO().deleteCheckList(newChecklist.getCheckListID());
		
			
	}
	
	//Test if duplicate task can be added.
	@Test
	public void addActionItemDup() throws SQLException {
		boolean output = false;
		
		//Add checklist
		Checklist testChecklist = new Checklist();
		testChecklist.setTaskID(2);
		model.getProjectDAO().addCheckList(testChecklist.getTaskID());
		
		int actionitemID = 0;
		
		//Clear action items.
		ArrayList<ActionItem> actionItems = model.getProjectDAO().loadActionItems(testChecklist.getCheckListID());
		if (actionItems.size() == 0) {
			//Create test action item.
			ActionItem testActionItem = new ActionItem();
			testActionItem.setcheckListID(testChecklist.getCheckListID());
			testActionItem.setName("Test name");
			testActionItem.setDescription("Test description");
			testActionItem.setCompleted(false);
			
			//Try adding action item twice.
			model.getProjectDAO().addActionItem(testActionItem.getcheckListID(), testActionItem.getName(), testActionItem.getDescription(), testActionItem.isCompleted());
			model.getProjectDAO().addActionItem(testActionItem.getcheckListID(), testActionItem.getName(), testActionItem.getDescription(), testActionItem.isCompleted());
			
			//Check if action item is in database twice.
			actionItems = model.getProjectDAO().loadActionItems(testChecklist.getCheckListID());
			int nameCounter = 0;
			
			
			for (ActionItem actionItem : actionItems) {
				if (actionItem.getName().equals(testActionItem.getName())) {
					nameCounter++;
					actionitemID = actionItem.getActionitemID();
				}
			}
			
			if (nameCounter == 1) {
				output = true;
			}
			
			
		}
		else {
			fail();
		}
		
		assertTrue(output);
		Checklist newChecklist = model.getProjectDAO().loadChecklist(testChecklist.getTaskID());
		model.getProjectDAO().deleteActionItem(actionitemID);
		model.getProjectDAO().deleteCheckList(newChecklist.getCheckListID());
		
	}

	//Test if edit action item works.
	@Test
	public void editActionItem() throws SQLException {
		boolean output = false;
		
		//Add checklist
		Checklist testChecklist = new Checklist();
		testChecklist.setTaskID(3);
		model.getProjectDAO().addCheckList(testChecklist.getTaskID());
		
		int actionItemID = 0;
		
			Checklist newChecklist = model.getProjectDAO().loadChecklist(testChecklist.getTaskID());
		
		
			//Create test action item.
			ActionItem testActionItem = new ActionItem();
			testActionItem.setcheckListID(newChecklist.getCheckListID());
			testActionItem.setName("Test name");
			testActionItem.setDescription("Test description");
			testActionItem.setCompleted(false);
			model.getProjectDAO().addActionItem(newChecklist.getCheckListID(), testActionItem.getName(), testActionItem.getDescription(), testActionItem.isCompleted());
			
			ArrayList<ActionItem> actionItems = model.getProjectDAO().loadActionItems(newChecklist.getCheckListID());
			ActionItem newActionItem = actionItems.get(actionItems.size() - 1);
			
			
			//Edit action item.
			model.getProjectDAO().changeActionItem(newActionItem.getActionitemID(), "Edited name", testActionItem.getDescription(), testActionItem.isCompleted());
			
			
			newChecklist = model.getProjectDAO().loadChecklist(testChecklist.getTaskID());
			
			actionItems = model.getProjectDAO().loadActionItems(newChecklist.getCheckListID());
			if (actionItems.size() == 1) {
				for (ActionItem actionItem : actionItems) {
					if (actionItem.getName().equals("Edited name")) {
						
						actionItemID = actionItem.getActionitemID();
						
						output = true;
					}
				}
			}
			
			
		
		else {
			fail();
		}
		
		assertTrue(output);
		
		model.getProjectDAO().deleteActionItem(actionItemID);
		model.getProjectDAO().deleteCheckList(newChecklist.getCheckListID());
		
	}
	
	//Test if delete action item works.
	@Test
	public void deleteActionItem() throws SQLException {
		//Create test action item.
		ActionItem testActionItem = new ActionItem();
		testActionItem.setcheckListID(5);
		testActionItem.setName("Test name");
		testActionItem.setDescription("Test description");
		testActionItem.setCompleted(false);
		model.getProjectDAO().addActionItem(testActionItem.getcheckListID(), testActionItem.getName(), testActionItem.getDescription(), testActionItem.isCompleted());

		ArrayList<ActionItem> actionItems = model.getProjectDAO().loadActionItems(testActionItem.getcheckListID());
		ActionItem newActionItem = actionItems.get(actionItems.size() - 1);
		
		model.getProjectDAO().deleteActionItem(newActionItem.getActionitemID());
		
		
	}

}
