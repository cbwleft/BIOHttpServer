package com.cbwleft;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MimeTypes {

	public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
	private static String mapFile = "mime.types";
	private static Map<String, String> mine_types = new HashMap<>();
	
	public static void load(){
		try (InputStream is = MimeTypes.class.getClassLoader().getResourceAsStream(mapFile)) {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				String[] pair = line.split("\\s+");
				mine_types.put(pair[0], pair[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getContentType(File file) {
		String fileName = file.getName();
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		String contentType = mine_types.get(extension);
		return contentType == null ? DEFAULT_CONTENT_TYPE : contentType;

	}

}
