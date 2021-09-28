package application.dao;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.User;

public class userDAO extends baseDao {
	
	
	public User getUser(int userID) throws SQLException {
		connect();
		User user = new User();
		
		Statement getUserStatement = connect().createStatement();
		ResultSet rs = getUserStatement.executeQuery("SELECT `first_name`, `last_name`, `username`, `password`, `profile` FROM `users` WHERE `id` = '" + userID + "'");
		
		while (rs.next()) {
			user.setFirstName(rs.getString(1));
		}
		
		return user;
	}
	
	public boolean userExists(String username, String firstName, String lastName) {
		connect();
		return false;
	}
	
	public void addUser(String firstName, String lastName, String username, String password, File profileImage) {
		connect();
	}
	
	public void saveProfileChanges(String firstName, String lastName, File newProfile) {
		
	}
}