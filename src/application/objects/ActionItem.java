package application.objects;

public class ActionItem {
	int actionitemID;
	int checkListID;
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

	public String getDescripion() {
		return descripion;
	}

	public void setDescripion(String descripion) {
		this.descripion = descripion;
	}
	
}