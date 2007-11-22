/*
 * Copyright (c) 2007-2008. MB Software Vancouver, Canada. All Rights Reserved.
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
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * This software was written for 
 * MB Software
 * Vancouver, B.C., Canada 
 */

package org.oscarehr.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Print a brief summary of the LogRecord in a human readable format. The summary will be 1 line.
 */
public class SimpleSingleLineFormatter extends Formatter
{
	private Date date = new Date();
	private SimpleDateFormat isoDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public synchronized String format(LogRecord record)
	{
		StringBuilder sb = new StringBuilder();

		sb.append(record.getLevel().getLocalizedName());
		sb.append(": ");

		date.setTime(record.getMillis());
		sb.append(isoDateFormatter.format(date));
		sb.append(' ');

		sb.append(record.getSourceClassName());
		sb.append('.');
		sb.append(record.getSourceMethodName());
		sb.append(' ');

		sb.append(record.getMessage());

		if (record.getThrown() != null)
		{
			try
			{
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				pw.close();
				sb.append('\n');
				sb.append(sw.toString());
			}
			catch (Exception e)
			{
			    e.printStackTrace();
			}
		}

		sb.append('\n');
		
		return sb.toString();
	}
}
