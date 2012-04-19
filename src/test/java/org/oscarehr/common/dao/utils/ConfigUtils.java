/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
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
			String overrideProperties=System.getProperty("oscar_override_properties");
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
