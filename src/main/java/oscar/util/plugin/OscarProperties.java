package oscar.util.plugin;

import java.util.Properties;

public class OscarProperties extends Properties {
	private static Properties properties;
	
	private OscarProperties() {}
	
	public OscarProperties getInstance() {
		return (OscarProperties)properties;
	}

	/**
	 * @return Returns the properties.
	 */
	public static Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties The properties to set.
	 */
	public static void setProperties(Properties properties) {
		OscarProperties.properties = properties;
	}
}
