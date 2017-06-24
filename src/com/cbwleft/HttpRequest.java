package com.cbwleft;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest implements HttpMessage{
	
	
	private InputStream inputStream;
	private byte[] buffer=new byte[2048];
	private String requestLine;
	private Map<String,String> headersMap=new HashMap<>();
	private int contentLength;
	private boolean headerEnd;
	private StringBuffer header=new StringBuffer();
	private StringBuffer entity;
	
	public HttpRequest(InputStream inputStream) {
		this.inputStream=inputStream;
	}
	
	public void parseRequest() throws IOException{
		int bufferSize=inputStream.read(buffer);
		for(int index=0;index<bufferSize;index++){
			char c=(char) buffer[index];
			if(c==LF){
				int length=header.length();
				if(header.charAt(length-1)==CR){
					if(requestLine==null){//第一个CRLF是请求行
						requestLine=header.toString();
					}else{
						int crlf=header.lastIndexOf(CRLF);
						String headerLine=header.substring(crlf+2,length-1);
						if(!"".equals(headerLine)){//请求头
							putHeader(headerLine);
						}else{//以CRLF结尾的空行标识请求头的结束
							headerEnd=true;
							if(headersMap.get(CONTENT_LENGTH)!=null){
								contentLength=Integer.parseInt(headersMap.get(CONTENT_LENGTH));
							}
						}
					}
				}
			}
			if(contentLength!=0){
				if(entity==null){//请求头的最后一个LF，不放入实体部分
					header.append(c);
					entity=new StringBuffer(contentLength);
				}else{
					entity.append(c);
				}
			}else{
				header.append(c);
			}
		}
		if(headerEnd){//如果请求头读取完毕
			while(contentLength>0&&contentLength>entity.length()){
				bufferSize=inputStream.read(buffer);
				entity.append(new String(buffer,0,bufferSize));
			}
		}else{
			parseRequest();
		}
	}
	
	private void putHeader(String header){
		int index=header.indexOf(":");
		String key=header.substring(0,index);
		String value=header.charAt(index+1)==' '?header.substring(index+2):header.substring(index+1);
		headersMap.put(key, value);
	}
	
	public String getHeader() {
		return header.toString();
	}
	
	public String getEntity() {
		return entity==null?null:entity.toString();
	}
	
	public String getUri(){
		return requestLine.split(" ")[1];
	}
}
