package com.ltu;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.amazonaws.services.lambda.runtime.Context;

public class TestUserPointHandler extends TestCase {

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public TestUserPointHandler(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TestUserPointHandler.class);
	}
	
	/**
	 * Creates the context.
	 * 
	 * @return the context
	 */
	private Context createContext() {
		TestContext ctx = new TestContext();

		ctx.setFunctionName("Your Function Name");

		return ctx;
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		
		Context context = createContext();
		TestUtils.callAPI(context, "userpoint.input.json", "userpoint.output.json");
		
		assertTrue(true);
	}
}