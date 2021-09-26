package application.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class baseDao {
	
	public Connection connect() {
		//Try to connect to database, if connection can not be made then 
		//close the program.
		try {
			Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost/java-project-management-db", "root", "");
			System.out.println("Connected to database.");
			return myConnection;
			
		} 
		catch (Exception e) {
			System.out.println("Can't connect to database.");
			System.out.println("Error code: " + e);
			System.exit(0);
		}
		return null;
	}


	

}