package application;

import java.io.File;
import java.io.InputStream;

public class User {
	int userId;
	String firstName;
	String lastName;
	String username;
	String password;
	InputStream profilePicture;
	
	public User() {
		
	}
	
	public int getUserID() {
		return userId;
	}
	
	public void setUserID(int userID) {
		this.userId = userID;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setUsername(String username) {
		username = this.username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public InputStream getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(InputStream inputStream) {
		this.profilePicture = inputStream;
	}
}