package application.dao;

import java.io.File;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.domains.User;

public class userDAOImpl implements userDAO {
	
	baseDao baseDao = new baseDao();
	
	public userDAOImpl() {
		
	}
	
	@Override
	public User getUser(int userID) throws SQLException {
		User user = new User();
		
		String query = "SELECT `user_id`, `first_name`, `last_name`, `username`, `password`, `profile` FROM `users` WHERE `user_id` = '" + userID + "'";
		PreparedStatement getUserStatement = baseDao.connect().prepareStatement(query);		
		ResultSet rs = getUserStatement.executeQuery(query);
		
		while (rs.next()) {
			user.setUserID(rs.getInt(1));
			user.setFirstName(rs.getString(2));
			user.setLastName(rs.getString(3));
			user.setUsername(rs.getString(4));
			user.setPassword(rs.getString(5));
			user.setProfilePicture(rs.getBinaryStream(6));
		}
		
		return user;
	}
	
	@Override
	public boolean userExists(User user) throws SQLException {
		
		String query = "SELECT * FROM `users` WHERE username = ?";
		PreparedStatement ps_userExists = baseDao.connect().prepareStatement(query);
		ps_userExists.setString(1, user.getUsername());
		
		ResultSet rs_userExists = ps_userExists.executeQuery();
		if (rs_userExists.next()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public User loginUser(String username, String password) throws SQLException {
		User currentUser = new User();
		String query = "SELECT * FROM `users` WHERE `username` = '" + username + "' AND `password` = '" + password + "'";
		PreparedStatement loginStatement = baseDao.connect().prepareStatement(query);
		ResultSet rs_login = loginStatement.executeQuery(query);
		
		if (rs_login.next()) {
			currentUser.setUserID(rs_login.getInt(1));
			currentUser.setFirstName(rs_login.getString(2));
			currentUser.setLastName(rs_login.getString(3));
			currentUser.setUsername(username);
			currentUser.setPassword(password);
			currentUser.setProfilePicture(rs_login.getBinaryStream(6));
			return currentUser;
		}
		else {
			return null;
		}
	}
	
	@Override
	public void addUser(String firstName, String lastName, String username, String password, InputStream profileImage) throws SQLException {
		String query = "INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `username`, `password`, `profile`) VALUES (null,?,?,?,?,?)";
		PreparedStatement ps_addUser = baseDao.connect().prepareStatement(query);
	
		if (profileImage == null) {
			System.out.println("Using default image");
		}
		else {
			System.out.println("Uploading profile: " + profileImage);
		}
		ps_addUser.setString(1, firstName);
		ps_addUser.setString(2, lastName);
		ps_addUser.setString(3, username);
		ps_addUser.setString(4, password);
		ps_addUser.setBinaryStream(5, profileImage);
		ps_addUser.execute();
		
	}
	
	@Override
	public void saveProfileChanges(int user_id, String firstName, String lastName, InputStream newProfile) throws SQLException {
		String query = "UPDATE `users` SET `first_name` = ?, `last_name` = ?, `profile` = ? WHERE `user_id` = ?";
		PreparedStatement ps_saveProfileChanges = baseDao.connect().prepareStatement(query);
		ps_saveProfileChanges.setString(1, firstName);
		ps_saveProfileChanges.setString(2, lastName);
		ps_saveProfileChanges.setBinaryStream(3, newProfile);
		ps_saveProfileChanges.setInt(4, user_id);
		ps_saveProfileChanges.execute();
		
		
	}


}