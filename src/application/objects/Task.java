package application.objects;

public class Task {
	int taskID;
	int columnID;
	String description;
	boolean completed;
	
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
	
	
}