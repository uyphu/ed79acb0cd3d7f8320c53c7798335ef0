package com.ltu;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.amazonaws.services.lambda.runtime.Context;

public class TestDeviceHandler extends TestCase {

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public TestDeviceHandler(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TestDeviceHandler.class);
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
		TestUtils.callAPI(context, "device.input.json", "device.output.json");
		assertTrue(true);
	}
	
}
