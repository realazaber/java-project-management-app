package application.dao;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import application.User;

public interface userDAO {
	public User getUser(int userID) throws SQLException; 
	
	public void saveProfileChanges(String firstName, String lastName, File newProfile) throws SQLException;

	boolean userExists(String firstName, String lastName) throws SQLException;

	void addUser(String firstName, String lastName, String username, String password, InputStream profileImage)
			throws SQLException;

	User loginUser(String username, String password) throws SQLException;

}