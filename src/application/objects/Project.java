package application.objects;

import java.util.ArrayList;

public class Project {
	int projectID;
	int userID;
	String projectName;
	boolean isDefault;
	
	public Project() {
		
	}
	
	public int getProjectID() {
		return projectID;
	}
	
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public boolean isDefault() {
		return isDefault;
	}
	
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	
	
	
}