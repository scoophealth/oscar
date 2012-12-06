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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBPreparedHandler;
import oscar.util.ConversionUtils;

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
			sDate = null;
		}
		if (eDate == null || eDate.compareTo("") == 0) {
			eDate = null;
		}
		
		OscarAppointmentDao dao = SpringUtils.getBean(OscarAppointmentDao.class);
		for(Object[] o : dao.findAppointments(ConversionUtils.fromDateString(sDate), ConversionUtils.fromDateString(eDate))) {
			Appointment a = (Appointment) o[0];
			Demographic d = (Demographic) o[1];
			
			count = count + 1;
			if (count == 1) {
				retval = retval + "OBEC01" + space(d.getHin(), 10) + space(d.getVer(), 2) + "\r";
			} else {
				retval = retval + "\n" + "OBEC01" + space(d.getHin(), 10) + space(d.getVer(), 2) + "\r";
			}
		}
		
		if (retval.compareTo("") == 0) {
			filename = "0";
		} else {
			filename = writeFile(retval, pp);
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
