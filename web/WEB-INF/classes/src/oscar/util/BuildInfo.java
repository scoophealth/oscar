// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public final class BuildInfo {
    private static Logger logger=MiscUtils.getLogger(); 

	private static final String BUILD_PROPERTIES="/build.properties";
	
	private static final Properties properties = new Properties();
	static {
		try {
			InputStream is = BuildInfo.class.getResourceAsStream(BUILD_PROPERTIES);
			properties.load(is);
			is.close();
		} catch (Exception e) {
			logger.error("Error loading "+BUILD_PROPERTIES, e);
		}
	}

	public static String getBuildDate() {
		return properties.getProperty("builddate", "unknown");
	}

	public static String getBuildTag() {
		return properties.getProperty("buildtag", "unknown");
	}
}