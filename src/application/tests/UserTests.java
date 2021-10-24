package application.tests;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import application.Model;
import application.domains.User;

public class UserTests {
	
	Model model = new Model();
	
	//Clear the users table
	public void clearUsers() {
		
	}

	/*
	 * Test if you can add a user
	 * if there are no duplicates. 
	 */

	@Test
	public void addUserNoDup() throws SQLException, FileNotFoundException {
		boolean output = false;
		
		//Prepare test user details.
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream testInputStream = (BufferedInputStream) classLoader.getResourceAsStream("default_profile.png");
		User tmpUser = new User();
		tmpUser.setUserID(1);
		tmpUser.setFirstName("fname");
		tmpUser.setLastName("lname");
		tmpUser.setUsername("username");
		tmpUser.setPassword("password");
		
		//Make sure user does not already exist.
		if (!model.getUserDAO().userExists(tmpUser)) {
			model.getUserDAO().addUser(tmpUser.getFirstName(), tmpUser.getLastName(), tmpUser.getUsername(), tmpUser.getPassword(), testInputStream);
		}
		
		//Check if the added user exists.
		if (model.getUserDAO().userExists(tmpUser)) {
			output = true;
		}
		
		
		assertTrue(output);
		
		//Delete user from database.
		model.getUserDAO().deleteUser(tmpUser.getUserID());
	}
	
	//Test if you can edit user.
	@Test
	public void saveUserChanges() throws SQLException {
		boolean output = false;
		
		//Prepare test user details.
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream testInputStream = (BufferedInputStream) classLoader.getResourceAsStream("default_profile.png");
		User tmpUser = new User();
		tmpUser.setFirstName("fname");
		tmpUser.setLastName("lname");
		tmpUser.setUsername("username");
		tmpUser.setPassword("password");
		
		//Make sure user does not already exist.
		if (!model.getUserDAO().userExists(tmpUser)) {
			model.getUserDAO().addUser(tmpUser.getFirstName(), tmpUser.getLastName(), tmpUser.getUsername(), tmpUser.getPassword(), testInputStream);
		}
		
		//Get the added user.
		ArrayList<User> users = model.getUserDAO().loadUsers();
		User newUser = users.get(users.size() -1);
		
		
		//Save changes to user.
		model.getUserDAO().saveProfileChanges(newUser.getUserID(), "Edited name", tmpUser.getLastName(), testInputStream);
		
		users = model.getUserDAO().loadUsers();
		for (User user : users) {
			if (user.getFirstName().equals("Edited name")) {
				output = true;
			}
		}
		
		assertTrue(output);
		
		//Delete user from database.
		model.getUserDAO().deleteUser(tmpUser.getUserID());
		model.getUserDAO().deleteUser(newUser.getUserID());
	}
	

}
