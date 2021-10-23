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
	
	
	//Retrieve a user from the database.
	@Override
	public User getUser(int userID) throws SQLException {
		System.out.println("Retrieving user " + userID);
		
		try {
			//Make user template. 
			User user = new User();
			
			//Get the user.
			String query = "SELECT `user_id`, `first_name`, `last_name`, `username`, `password`, `profile` FROM `users` WHERE `user_id` = '" + userID + "'";
			PreparedStatement getUserStatement = baseDao.connect().prepareStatement(query);		
			ResultSet rs = getUserStatement.executeQuery(query);
			
			while (rs.next()) {
				//Assign values to the template user.
				user.setUserID(rs.getInt(1));
				user.setFirstName(rs.getString(2));
				user.setLastName(rs.getString(3));
				user.setUsername(rs.getString(4));
				user.setPassword(rs.getString(5));
				user.setProfilePicture(rs.getBinaryStream(6));
			}
			
			return user;
		
			//If there is a database error let the user know and close the program.
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return null;
	}
	
	
	//Check if a user is in the database.
	@Override
	public boolean userExists(User user) throws SQLException {
		System.out.println("Check if " + user.getUsername() + " exists");
		
		try {
			//Try to get a user with matching details.
			String query = "SELECT * FROM `users` WHERE username = ?";
			PreparedStatement ps_userExists = baseDao.connect().prepareStatement(query);
			ps_userExists.setString(1, user.getUsername());
			
			//If the user does exist return true.
			ResultSet rs_userExists = ps_userExists.executeQuery();
			if (rs_userExists.next()) {
				return true;
			}
			
			return false;
			
			//If there is a database error let the user know and close the program.
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return false;
	}
	
	//Login the user.
	@Override
	public User loginUser(String username, String password) throws SQLException {
		System.out.println("Login user " + username);
		try {
			//Create user template.
			User currentUser = new User();
			
			//Get user details from database.
			String query = "SELECT * FROM `users` WHERE `username` = '" + username + "' AND `password` = '" + password + "'";
			PreparedStatement loginStatement = baseDao.connect().prepareStatement(query);
			ResultSet rs_login = loginStatement.executeQuery(query);
			
			//If the user exists then assign the values to the user.
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
			
			//If there is a database error let the user know and close the program.
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		return null;
	}

	//Add user to database.
	@Override
	public void addUser(String firstName, String lastName, String username, String password, InputStream profileImage) throws SQLException {
		System.out.println("Adding user " + username + " to database");
		
		try {
			//Prepare query to add the template user to database.
			String query = "INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `username`, `password`, `profile`) VALUES (null,?,?,?,?,?)";
			PreparedStatement ps_addUser = baseDao.connect().prepareStatement(query);
		
			/*
			 * If a profile image has
			 * been selected then upload
			 * it to database. If no profile
			 * was selected then just use the
			 * default profile picture.
			 */
			if (profileImage == null) {
				System.out.println("Using default image");
			}
			else {
				System.out.println("Uploading profile: " + profileImage);
			}
			
			//Assign the values to template user.
			ps_addUser.setString(1, firstName);
			ps_addUser.setString(2, lastName);
			ps_addUser.setString(3, username);
			ps_addUser.setString(4, password);
			ps_addUser.setBinaryStream(5, profileImage);
			ps_addUser.execute();
			
			//If there is a database error let the user know and close the program.
		} catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
		
	}

	//Save changes to user.
	@Override
	public void saveProfileChanges(int userID, String firstName, String lastName, InputStream newProfile) throws SQLException {
		System.out.println("Saving changes to user " + userID);
		
		try {
			//Apply changes to user.
			String query = "UPDATE `users` SET `first_name` = ?, `last_name` = ?, `profile` = ? WHERE `user_id` = ?";
			PreparedStatement ps_saveProfileChanges = baseDao.connect().prepareStatement(query);
			ps_saveProfileChanges.setString(1, firstName);
			ps_saveProfileChanges.setString(2, lastName);
			ps_saveProfileChanges.setBinaryStream(3, newProfile);
			ps_saveProfileChanges.setInt(4, userID);
			ps_saveProfileChanges.execute();
			
		} 
		
		//If there is a database error let the user know and close the program.
		catch (Exception e) {
			System.out.println("Error connecting to database." + e);
			System.exit(0);
		}
	}
}