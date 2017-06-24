package com.cbwleft;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HttpServer {
	
	public int port=8080;
	public int maxThreads=20;
	public int timeout=10*1000;
	public static final String WEB_ROOT =
		    System.getProperty("user.dir") + File.separator  + "webroot";
	
	public void start() throws IOException{
		
		ServerSocket ss=new ServerSocket(port);
		ExecutorService executorService=Executors.newFixedThreadPool(maxThreads);
		System.out.println("Web root is "+WEB_ROOT);
		System.out.println("Http server lisen on "+port);
		MimeTypes.load();
		
		while(true){
			Socket socket=ss.accept();
			socket.setSoTimeout(timeout);
			executorService.submit(new HttpProcesser(socket));
		}
		
	}
	
}
