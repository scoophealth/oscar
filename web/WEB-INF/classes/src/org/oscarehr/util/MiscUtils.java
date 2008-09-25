/*
 * Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
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
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

public class MiscUtils {

	public static byte[] propertiesToXmlByteArray(Properties p) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		p.storeToXML(os, null);
		return (os.toByteArray());
	}

	public static Properties xmlByteArrayToProperties(byte[] b) throws IOException {
		Properties p = new Properties();

		ByteArrayInputStream is = new ByteArrayInputStream(b);
		p.loadFromXML(is);

		return (p);
	}
	
	/**
	 * This method will set the calendar to the beginning of the month, i.e.
	 * day=1, hour=0, minute=0, sec=0, ms=0. It will return the same instance passed in (not a clone of it).
	 */
	public static Calendar setToBeginningOfMonth(Calendar cal)
	{
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		// force calculation / materialisation of actual time.
		cal.getTimeInMillis();
		
		return(cal);
	}
}
