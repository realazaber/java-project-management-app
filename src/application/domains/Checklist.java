package application.domains;

public class Checklist {
	int checkListID;
	int taskID;
	
	public Checklist() {
		
	}
	
	public int getCheckListID() {
		return checkListID;
	}
	public void setCheckListID(int checkListID) {
		this.checkListID = checkListID;
	}
	public int getTaskID() {
		return taskID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	
}