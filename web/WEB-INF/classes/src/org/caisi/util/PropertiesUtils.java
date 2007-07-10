package org.caisi.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
	private static Properties properties = null;
	static {
		try {
			properties = getProperties("/caisi.properties");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Properties getProperties() {
		return(properties);
	}

	/**
	 * This will automatically read in the values in the file to this object.
	 */
	private static Properties getProperties(String url) throws IOException {
		Properties p = new Properties();
		readFromFile(url, p);
		return(p);
	}

	/**
	 * This method reads the properties from the url into the object passed in.
	 */
	private static void readFromFile(String url, Properties p) throws IOException {
		InputStream is = PropertiesUtils.class.getResourceAsStream(url);
		if (is == null) is = new FileInputStream(url);
		try {
			p.load(is);
		}
		finally {
			is.close();
		}
	}
}
