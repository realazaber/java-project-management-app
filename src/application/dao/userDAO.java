package application.dao;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import application.objects.User;

public interface userDAO {
	public User getUser(int userID) throws SQLException; 
	
	public void saveProfileChanges(int user_id, String firstName, String lastName, String password, InputStream newProfile) throws SQLException;

	boolean userExists(String firstName, String lastName) throws SQLException;

	void addUser(String firstName, String lastName, String username, String password, InputStream profileImage)
			throws SQLException;

	User loginUser(String username, String password) throws SQLException;

}