package application.tests.daos;

import static org.junit.Assert.*;

import org.junit.Test;

import application.dao.baseDao;

public class baseDaoTest {

	@Test
	public void dbconnection() {
		baseDao testDao = new baseDao();
		testDao.connect();
	}

}
