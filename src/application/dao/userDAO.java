package application.dao;

import java.io.File;

public class userDAO extends dao {
	public boolean userExists(String username, String firstName, String lastName) {
		connect();
		return false;
	}
	
	public void addUser(String firstName, String lastName, String username, String password, File profileImage) {
		connect();
	}
}