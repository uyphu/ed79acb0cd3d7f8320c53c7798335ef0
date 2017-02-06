package com.ltu;

import java.io.InputStream;

public class TestUtil {

	public static void main(String[] args) {
		System.out.println("Testing...");
		InputStream input = TestUtil.class.getResourceAsStream("user.input.json");
		System.out.println(input.toString());
	}
	
}
