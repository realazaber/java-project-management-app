package application.objects;

import java.sql.Date;

public class Task {
	int taskID;
	int columnID;
	String name;
	String description;
	boolean completed;
	
	Date dueDate;
	
	public Task() {
		
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public int getColumnID() {
		return columnID;
	}
	
	public void setTaskName(String name) {
		this.name = name;
	}
	
	public String getTaskName() {
		return name;
	}

	public void setColumnID(int columnID) {
		this.columnID = columnID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	
}