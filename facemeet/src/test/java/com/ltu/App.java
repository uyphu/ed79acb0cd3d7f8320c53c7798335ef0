package com.ltu;

import java.io.InputStream;

public class App {

	public static void main(String[] args) {
		System.out.println("Testing...");
		InputStream input = App.class.getResourceAsStream("user.input.json");
		System.out.println(input.toString());
	}
}
