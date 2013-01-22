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

package oscar.oscarReport.oscarMeasurements.data;

import java.util.ArrayList;

import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;


/**
 * This classes main function ConsultReportGenerate collects a group of patients with consults in the last specified date
 */
public class RptMeasurementsData {

	/**
 	 * Gets the number of Patient seen during aspecific time period
	 *
	 * @return 
	 * 		number or Patients seen in Integer
	 */
	public int getNbPatientSeen(String startDateA, String endDateA) {
		int nbPatient = 0;
		MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
		for (Object o : dao.findByCreateDate(ConversionUtils.fromDateString(startDateA), ConversionUtils.fromDateString(endDateA))) {
			nbPatient = (Integer) o;
		}
		return nbPatient;
	}

	/**
	 * get the number of patients during a specific time period
	 *
	 * @return 
	 * 		ArrayList which contain the result in String format
	 */
	public ArrayList getPatientsSeen(String startDate, String endDate) {
		ArrayList patients = new ArrayList();
		MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
		for (Object[] o : dao.findByCreateDate(ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate))) {
			Integer i = (Integer) o[0];
			patients.add("" + i);
		}
		return patients;
	}
}
