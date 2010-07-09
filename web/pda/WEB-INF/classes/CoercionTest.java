/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

package bean;

import java.lang.Class;
import java.lang.ClassNotFoundException;
import java.lang.Object;
import java.lang.String;
import java.lang.System;
import java.lang.Character;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import java.io.InputStream;
import java.io.IOException;

import org.oscarehr.util.MiscUtils;

public class CoercionTest {
	private static Logger logger = MiscUtils.getLogger();

	public static String showAsciiStream(InputStream in, int show) {

		if (in == null) return "null";

		StringBuffer sb = new StringBuffer("");

		int i = 0;
		int c = 0;

		try {
			while ((i++ < show) && ((c = in.read()) != -1)) {

				if (Character.isISOControl((char) c)) {
					if ((new Character((char) c).charValue() == 10) || (new Character((char) c).charValue() == 13)) {

						sb.append("<br>");
					} else {

						sb.append("");

					}
				} else {
					sb.append(new Character((char) c).toString());
				}
			}
		} catch (IOException ioe) {
			logger.error("Error", e);
		}

		return sb.toString();
	}

}
