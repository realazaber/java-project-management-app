package application.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import application.dao.baseDao;

public class test {

	@Test
	public void dbconnection() {
		baseDao testDao = new baseDao();
	}

}
