package application.dao;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import application.domains.User;

//Interface for userDAOImpl
public interface userDAO {
	public User getUser(int userID) throws SQLException; 
	
	public void saveProfileChanges(int user_id, String firstName, String lastName, InputStream newProfile) throws SQLException;

	public boolean userExists(User user) throws SQLException;

	void addUser(String firstName, String lastName, String username, String password, InputStream profileImage)
			throws SQLException;

	User loginUser(String username, String password) throws SQLException;

}