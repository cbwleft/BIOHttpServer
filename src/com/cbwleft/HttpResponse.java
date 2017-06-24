package com.cbwleft;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse implements HttpMessage {

	private byte[] buffer = new byte[2048];
	HttpRequest request;
	OutputStream outputStream;

	public HttpResponse(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	public void sendStaticResource() throws IOException {
		FileInputStream fis = null;
		try {
			File file = new File(HttpServer.WEB_ROOT, request.getUri());
			if (file.isFile()) {
				fis = new FileInputStream(file);
				int bufferSize = fis.read(buffer);
				StringBuffer header=new StringBuffer();
				header.append("HTTP/1.1 200 OK").append(CRLF)
					.append("Connection:close").append(CRLF)
					.append("Content-Type:").append(MimeTypes.getContentType(file)).append(CRLF)
					.append(CRLF);
				outputStream.write(header.toString().getBytes());
				while (bufferSize != -1) {
					outputStream.write(buffer, 0, bufferSize);
					bufferSize = fis.read(buffer);
				}
			} else {
				String errorMessage = "HTTP/1.1 404 Not Found\r\n" + "Content-Type: text/html\r\n"
						+ "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";
				outputStream.write(errorMessage.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				fis.close();
		}
	}
}
