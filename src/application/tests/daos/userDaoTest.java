///////////////////////////////////
//Make sure the database is empty//
//     before running tests      //
///////////////////////////////////


package application.tests.daos;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.junit.Test;

import application.Model;
import application.objects.User;

public class userDaoTest {

	Model model = new Model();
	
	@Test
	public void userDoesExist() throws SQLException {
		
		User testUser = new User();
		testUser.setFirstName("First name");
		testUser.setLastName("Last name");
		testUser.setUsername("Username");
		testUser.setPassword("Password");
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		BufferedInputStream file_defaultImage = (BufferedInputStream) classLoader.getResourceAsStream("default_profile.png");
		
		testUser.setProfilePicture(file_defaultImage);
		model.getUserDAO().addUser(testUser.getFirstName(), testUser.getLastName(), testUser.getUsername(), testUser.getPassword(), testUser.getProfilePicture());
		
		assertTrue(model.getUserDAO().userExists(testUser));
	}
	
	@Test
	public void userDoesNotExist() throws SQLException {
		User testUser = new User();
		assertTrue(!model.getUserDAO().userExists(testUser));
	}

}
