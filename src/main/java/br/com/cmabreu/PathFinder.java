package br.com.cmabreu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class PathFinder {
	private static PathFinder instance;

	public static PathFinder getInstance() {
		if ( instance == null ) {
			instance = new PathFinder();
		}
		return instance;
	}
	
	
	public static String readFile(String file) throws Exception {
		Charset cs = Charset.forName("UTF-8");
	    FileInputStream stream = new FileInputStream(file);
	    try {
	        Reader reader = new BufferedReader(new InputStreamReader(stream, cs));
	        StringBuilder builder = new StringBuilder();
	        char[] buffer = new char[8192];
	        int read;
	        while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
	            builder.append(buffer, 0, read);
	        }
	        return builder.toString();
	    } finally {
	        stream.close();
	    }        
	}
	
	private PathFinder() {
		// Use getInstance() 
	}
	
	public String getPath() throws UnsupportedEncodingException {
		String path = this.getClass().getClassLoader().getResource("").getPath();
		String fullPath = URLDecoder.decode(path, "UTF-8");
		String pathArr[] = fullPath.split("/WEB-INF/classes/");
		fullPath = pathArr[0];
		String reponsePath = "";
		reponsePath = new File(fullPath).getPath();
		return reponsePath;
	}			
	
}
