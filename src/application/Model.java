package application;

import application.dao.*;

public class Model {
	private userDAO userDAO;
	private projectDAO projectDAO;
	
	public Model() {
		userDAO = new userDAOImpl();
	}
	
	public userDAO getUserDAO() {
		return userDAO;
	}
	
	public projectDAO getProjectDAO() {
		return projectDAO;
	}
}