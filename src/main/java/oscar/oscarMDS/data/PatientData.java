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

package oscar.oscarMDS.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.oscarehr.common.dao.MdsPIDDao;
import org.oscarehr.common.model.MdsPID;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class PatientData {

	public Patient getPatient(String demographicNo) {
		MdsPIDDao dao = SpringUtils.getBean(MdsPIDDao.class);
		MdsPID pid = dao.find(ConversionUtils.fromIntString(demographicNo));

		if (pid == null) {
			return null;
		}
		return new Patient(pid.getPatientName(), pid.getDob(), pid.getHealthNumber(), pid.getSex(), pid.getHomePhone(), pid.getAlternatePatientId());
	}

	public class Patient {
		String patientName;
		String dOB;
		String sex;
		String age;
		String healthNumber;
		String homePhone;
		String workPhone;
		String patientLocation;

		public Patient(String patientName, String dOB, String healthNumber, String sex, String homePhone, String patientLocation) {
			int patientAge;
			GregorianCalendar cal = new GregorianCalendar(Locale.ENGLISH);
			GregorianCalendar now = new GregorianCalendar(Locale.ENGLISH);
			java.util.Date curTime = new java.util.Date();
			now.setTime(curTime);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

			if (dOB.length() >= 8) {
				// boneheaded calendar numbers months from 0
				cal.set(Integer.parseInt(dOB.substring(0, 4)), Integer.parseInt(dOB.substring(4, 6)) - 1, Integer.parseInt(dOB.substring(6, 8)));

				this.dOB = dateFormat.format(cal.getTime());
			} else {
				this.dOB = "";
			}

			try {
				this.patientName = patientName.substring(0, patientName.indexOf("^")) + ", " + patientName.substring(patientName.indexOf("^") + 1).replace('^', ' ');
			} catch (IndexOutOfBoundsException e) {
				this.patientName = patientName.replace('^', ' ');
			}
			this.sex = sex;
			this.homePhone = homePhone;
			this.healthNumber = healthNumber;
			// this.patientLocation = patientLocation;
			this.patientLocation = "";
			if (dOB.length() > 0) {
				patientAge = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
				if (now.get(Calendar.MONTH) < cal.get(Calendar.MONTH)) {
					patientAge--;
				}
				if (now.get(Calendar.MONTH) == cal.get(Calendar.MONTH) && now.get(Calendar.DAY_OF_MONTH) < cal.get(Calendar.DAY_OF_MONTH)) {
					patientAge--;
				}
				this.age = String.valueOf(patientAge);
			} else {
				this.age = "N/A";
			}
			this.workPhone = "";
		}

		public String getPatientName() {
			return this.patientName;
		}

		public String getSex() {
			return this.sex;
		}

		public String getDOB() {
			return this.dOB;
		}

		public String getAge() {
			return this.age;
		}

		public String getHomePhone() {
			return this.homePhone;
		}

		public String getWorkPhone() {
			return this.workPhone;
		}

		public String getPatientLocation() {
			return this.patientLocation;
		}

		public String getHealthNumber() {
			return this.healthNumber;
		}

	}

}
