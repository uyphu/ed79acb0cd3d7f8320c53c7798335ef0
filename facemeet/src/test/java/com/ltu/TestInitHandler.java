package com.ltu;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.kins.vs.dao.InitDao;

public class TestInitHandler extends TestCase {

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public TestInitHandler(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TestInitHandler.class);
	}
	
	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		InitDao.initTables();
		assertTrue(true);
	}
}
