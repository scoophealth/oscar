package org.oscarehr.common.dao.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class ConfigUtils
{
	private static final Logger logger=MiscUtils.getLogger();

	private static Properties properties=null;
	static
	{
		try
        {
			String overrideProperties=System.getProperty("oscar_properties");
			logger.info("loading "+overrideProperties);
	        properties=getProperties(overrideProperties, "/over_ride_config.properties");
        }
        catch (IOException e)
        {
        	logger.error("unexpected error", e);
        }
	}

	public static String getProperty(String key)
	{
		return(properties.getProperty(key));
	}

	public static String getProperty(Class<?> c, String key)
	{
		return(getProperty(properties, c, key));
	}

	protected static String getProperty(Properties p, Class<?> c, String key)
	{
		return(p.getProperty(c.getName()+'.'+key));
	}

	/**
	 * This will automatically read in the values in the file to this object.
	 */
	protected static Properties getProperties(String propertiesUrl, String defaultPropertiesUrl) throws IOException
	{
		Properties p=new Properties();
		readFromFile(defaultPropertiesUrl, p);

		if (propertiesUrl!=null)
		{
			p=new Properties(p);
			readFromFile(propertiesUrl, p);
		}

		return(p);
	}

	protected static Properties getProperties()
	{
		return(properties);
	}

	/**
	 * This method reads the properties from the url into the object passed in.
	 */
	private static void readFromFile(String url, Properties p) throws IOException
	{
		logger.info("Reading properties : "+url);

		InputStream is=ConfigUtils.class.getResourceAsStream(url);
		if (is==null) is=new FileInputStream(url);

		try
		{
			p.load(is);
		}
		finally
		{
			is.close();
		}
	}
}