package com.ltu;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.amazonaws.services.lambda.runtime.Context;

public class TestUserHandler extends TestCase {

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public TestUserHandler(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TestUserHandler.class);
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
		TestUtils.callAPI(context, "user.input.json", "user.output.json");
		
		assertTrue(true);
	}
}
