package application;

import application.dao.*;

public class Model {
	private baseDao baseDao;
	private userDAO userDAO;
	private projectDAO projectDAO;
	
	
	public Model() {
		baseDao = new baseDao();
		userDAO = new userDAOImpl();
		projectDAO = new projectDAOImpl();
	}
	
	public baseDao getBaseDao() {
		return baseDao;
	}
	
	public userDAO getUserDAO() {
		return userDAO;
	}
	
	public projectDAO getProjectDAO() {
		return projectDAO;
	}
}