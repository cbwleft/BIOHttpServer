package com.cbwleft;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpProcesser implements Runnable{

	private Socket socket;
	private HttpRequest request;
	private HttpResponse response;
	
	public HttpProcesser(Socket socket) {
		this.socket=socket;
	}
	
	@Override
	public void run() {
		try {
			InputStream inputStream=socket.getInputStream();
			request=new HttpRequest(inputStream);
			request.parseRequest();
			System.out.print(request.getHeader());
			if(request.getEntity()!=null){
				System.out.println(request.getEntity());
			}
			OutputStream outputStream=socket.getOutputStream();
			response=new HttpResponse(outputStream);
			response.setRequest(request);
			response.sendStaticResource();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
