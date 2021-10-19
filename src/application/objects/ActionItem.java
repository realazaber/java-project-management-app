package application.objects;

public class ActionItem {
	int actionitemID;
	int taskID;
	String name;
	String descripion;
	
	public ActionItem() {
		
	}

	public int getActionitemID() {
		return actionitemID;
	}

	public void setActionitemID(int actionitemID) {
		this.actionitemID = actionitemID;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescripion() {
		return descripion;
	}

	public void setDescripion(String descripion) {
		this.descripion = descripion;
	}
	
}