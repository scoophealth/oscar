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


package oscar.oscarReport.data;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandler;

/**
 *This classes main function ObecGenerate collects a group of patients with health insurance number for OHIP validation in the last specified date
 */
public class ObecData {
	private static Logger logger = MiscUtils.getLogger();

	//public ArrayList demoList = null;
	public String sql = "";
	public String results = null;
	public String text = null;
	public String connect = null;
	DBPreparedHandler accessDB = null;

	public ObecData() {
	}

	public String generateOBEC(String sDate, String eDate, Properties pp) {
		int count = 0;
		String retval = "";
		String filename = "";
		if (sDate == null || sDate.compareTo("") == 0) {
			sDate = "9999/00/00";
		}
		if (eDate == null || eDate.compareTo("") == 0) {
			eDate = "9999/12/31";
		}
		try {

			String sql = "select d.demographic_no, d.last_name, d.first_name, LEFT(d.address, 32) as address, LEFT(d.city, 30) as city, d.postal, d.hin, d.ver, d.province from appointment a, demographic d where a.demographic_no=d.demographic_no and d.hin <> '' and a.appointment_date>= '" + sDate + "' and appointment_date<='" + eDate + "' and (d.hc_type='Ontario' or d.hc_type='ON' or d.hc_type='ONTARIO') group by d.demographic_no order by d.last_name";
			ResultSet rs = DBHandler.GetSQL(sql);
			while (rs.next()) {
				count = count + 1;
				if (count == 1) {
					retval = retval + "OBEC01" + space(oscar.Misc.getString(rs, "hin"), 10) + space(oscar.Misc.getString(rs, "ver"), 2) + "\r";
				} else {
					retval = retval + "\n" + "OBEC01" + space(oscar.Misc.getString(rs, "hin"), 10) + space(oscar.Misc.getString(rs, "ver"), 2) + "\r";
				}
			}
			rs.close();
			if (retval.compareTo("") == 0) {
				filename = "0";
			} else {
				filename = writeFile(retval, pp);
			}
		} catch (SQLException e) {
			MiscUtils.getLogger().debug("There has been an error while retrieving a Obec");
			MiscUtils.getLogger().error("Error", e);
		}

		return filename;
	}

	public static String space(String oldString, int leng) {

		String outputString = "";
		int i;
		for (i = oldString.length(); i < leng; i++) {
			outputString = outputString + " ";
		}
		outputString = oldString + outputString;
		return outputString;
	}

	public static String zero(String oldString, int leng) {

		String outputString = "";
		int i;
		for (i = oldString.length(); i < leng; i++) {
			outputString = outputString + "0";
		}
		outputString = oldString + outputString;
		return outputString;
	}

	public String writeFile(String value1, Properties pp) {

		String obecFilename = "";

		try {


			String oscar_home = pp.getProperty("DOCUMENT_DIR");
			Calendar calendar = new GregorianCalendar();
			String randomDate = String.valueOf(calendar.get(Calendar.SECOND)) + String.valueOf(calendar.get(Calendar.MILLISECOND));
			if (randomDate.length() > 3) {
				randomDate = randomDate.substring(0, 3);
			}
			if (randomDate.length() < 3) {
				randomDate = zero(randomDate, 3);
			}
			obecFilename = "OBECE" + randomDate + ".TXT";
			FileOutputStream out;
			out = new FileOutputStream(oscar_home + obecFilename);
			PrintStream p;
			p = new PrintStream(out);

			p.println(value1);

			p.close();
		} catch (Exception e) {
			logger.error("", e);
		}
		return obecFilename;
	}
};
