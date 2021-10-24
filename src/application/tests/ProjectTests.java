package application.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import application.Model;
import application.domains.Project;

public class ProjectTests {

	Model model = new Model();
	
	
	
	//Clear the projects table.
	void clearProjects(int userID) throws SQLException {
		ArrayList<Project> projects = model.getProjectDAO().loadProjects(userID);
		for (Project project : projects) {
			model.getProjectDAO().deleteProject(project.getProjectID());
		}
	}
	
	//Test if program will not add project if there is a duplicate.
	@Test
	public void addProjectDuplicateDetails() throws SQLException {
		
		//Create template project.
		Project tmpProject = new Project();
		
		tmpProject.setUserID(1);
		tmpProject.setProjectName("Test");
		tmpProject.setDefault(false);
		
		clearProjects(tmpProject.getUserID());
		
		try {
			//Add template to database.
			model.getProjectDAO().addProject(tmpProject.getUserID(), tmpProject.getProjectName(), tmpProject.isDefault());
		} catch (Exception e) {
			System.out.println("Database error: " + e);
		}
		
		//Detect template that was already added and do not add the new project.
		boolean output = false;
		if (!model.getProjectDAO().addProject(tmpProject.getUserID(), tmpProject.getProjectName(), tmpProject.isDefault())) {
			output = true;
		}
		else {
			output = false;
		}
		
		assertTrue(output);
		
		clearProjects(tmpProject.getUserID());
		
		
	}
	
	//Test if project will be added if there are no duplicates.
	@Test
	public void addProjectNoDup() throws SQLException {
		
		boolean output = false;
		
		//Create template project.
		Project tmpProject = new Project();
		
		tmpProject.setUserID(1);
		tmpProject.setProjectName("Test");
		tmpProject.setDefault(false);
		
		//Make sure the projects table is empty. 
		clearProjects(tmpProject.getUserID());
		
		//Add project to database.
		model.getProjectDAO().addProject(tmpProject.getUserID(), tmpProject.getProjectName(), tmpProject.isDefault());
		
		//Load project from database.
		ArrayList<Project> projects = model.getProjectDAO().loadProjects(tmpProject.getUserID());
		
		//Make sure projects size is check if only one project was added.
		if (projects.size() == 1) {
			//See if a project has the same name.
			for (Project project : projects) {
				if (project.getProjectName().equals(tmpProject.getProjectName())) {
					output = true;
				}
			}
		}
		else {
			output = false;
		}
		
		assertTrue(output);
		
		clearProjects(tmpProject.getUserID());		
	}
	
	
	//Test if project can be edited.
	@Test
	public void editProject() throws SQLException {
		boolean output = false;
		
		//Create template project.
		Project tmpProject = new Project();
		
		tmpProject.setUserID(1);
		tmpProject.setProjectName("Test");
		tmpProject.setDefault(false);
		
		//Make sure the projects table is empty. 
		clearProjects(tmpProject.getUserID());
		
		//Add project to database.
		model.getProjectDAO().addProject(tmpProject.getUserID(), tmpProject.getProjectName(), tmpProject.isDefault());
		
		//Load project from database.
		ArrayList<Project> projects = model.getProjectDAO().loadProjects(tmpProject.getUserID());
		
		//Make sure projects size is check if only one project was added.
		if (projects.size() == 1) {
			//See if a project has the same name. 
			for (Project project : projects) {
				//Change project name.
				if (project.getProjectName().equals(tmpProject.getProjectName())) {
					model.getProjectDAO().saveProjectChanges(project.getProjectID(), project.getUserID(), "Edited name", project.isDefault());
				}
			}
		}
		else {
			output = false;
		}
		
		projects = model.getProjectDAO().loadProjects(tmpProject.getUserID());
		
		//Make sure there is only one project in the database.
		if (projects.size() == 1) {
			for (Project project : projects) {
				//Check if the project name matches the edited name.
				if (project.getProjectName().equals("Edited name")) {
					output = true;
				}
			}
		}
		
		assertTrue(output);
		
		clearProjects(tmpProject.getUserID());	
	}
	
	//Test if project can be deleted.
	@Test
	public void deleteProject() throws SQLException {
		boolean output = false;
		
		//Create template project.
		Project tmpProject = new Project();
		
		tmpProject.setUserID(1);
		tmpProject.setProjectName("Test");
		tmpProject.setDefault(false);
		
		//Make sure the projects table is empty. 
		clearProjects(tmpProject.getUserID());
		
		//Add project to database.
		model.getProjectDAO().addProject(tmpProject.getUserID(), tmpProject.getProjectName(), tmpProject.isDefault());
		
		//Load project from database.
		ArrayList<Project> projects = model.getProjectDAO().loadProjects(tmpProject.getUserID());
		
		//Make sure projects size is check if only one project was added.
		if (projects.size() == 1) {
			//See if a project has the same name.
			for (Project project : projects) {
				//If project is in database delete it.
				if (project.getProjectName().equals(tmpProject.getProjectName())) {
					model.getProjectDAO().deleteProject(project.getProjectID());
				}
			}
		}
		else {
			output = false;
		}
		
		
		//Check if the project is still in the database.
		projects = model.getProjectDAO().loadProjects(tmpProject.getUserID());
		if (projects.size() == 0) {
			output = true;
		}
		
		assertTrue(output);
		
		clearProjects(tmpProject.getUserID());
		
	}

}
