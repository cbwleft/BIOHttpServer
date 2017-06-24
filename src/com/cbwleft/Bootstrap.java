package com.cbwleft;

import java.io.IOException;

public class Bootstrap {

	
	public static void main(String[] args) {
		try {
			new HttpServer().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
