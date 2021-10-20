package application.domains;

public class ActionItem {
	int actionitemID;
	int checkListID;
	String name;
	String description;
	boolean completed;
	
	public ActionItem() {
		
	}

	public int getActionitemID() {
		return actionitemID;
	}

	public void setActionitemID(int actionitemID) {
		this.actionitemID = actionitemID;
	}

	public int getcheckListID() {
		return checkListID;
	}

	public void setcheckListID(int checkListID) {
		this.checkListID = checkListID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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