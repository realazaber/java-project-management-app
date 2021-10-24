package application.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ActionitemTests.class, ChecklistTests.class, ColumnTests.class, ProjectTests.class, TaskTests.class,
		UserTests.class })
public class TestSuite {

}
