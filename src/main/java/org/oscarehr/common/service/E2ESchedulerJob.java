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
package org.oscarehr.common.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.exports.e2e.E2EVelocityTemplate;
import org.oscarehr.exports.e2e.PatientExport;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class E2ESchedulerJob extends TimerTask {
	private static final Logger logger = MiscUtils.getLogger();
	String tmpDir = OscarProperties.getInstance().getProperty("TMP_DIR");

	@Override
	public void run() {
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);

		try {
			logger.info("starting E2E export job");

			List<Integer> ids = demographicDao.getActiveDemographicIds();
			ArrayList<File> files = new ArrayList<File>();

			for(Integer id:ids) {
				// Select Template
				E2EVelocityTemplate t = new E2EVelocityTemplate();

				// Create and load Patient data
				PatientExport patient = new PatientExport();
				patient.setExAllTrue();

				// Load patient data and merge to template
				String output = "";
				if(patient.loadPatient(id.toString())) {
					output = t.export(patient);
				} else {
					logger.error("Failed to load patient " + id.toString());
					continue;
				}

				//export file to temp directory
				try{
					File directory = new File(tmpDir);
					if(!directory.exists()){
						throw new Exception("Temporary Export Directory does not exist!");
					}

					//Standard format for xml exported file : PatientFN_PatientLN_PatientUniqueID_DOB (DOB: ddmmyyyy)
					String expFile = patient.getDemographic().getFirstName()+"_"+patient.getDemographic().getLastName();
					expFile += "_"+id;
					expFile += "_"+patient.getDemographic().getDateOfBirth()+patient.getDemographic().getMonthOfBirth()+patient.getDemographic().getYearOfBirth();
					files.add(new File(directory, expFile+".xml"));
				}catch(Exception e){
					logger.error("Error", e);
				}
				BufferedWriter out  = null;
				try {
					out = new BufferedWriter(new FileWriter(files.get(files.size()-1)));
					out.write(output);

				} catch (IOException ex) {
					logger.error("Error", ex);
					throw new Exception("Cannot write .xml file(s) to export directory.\n Please check directory permissions.");
				} finally {
					try {
						out.close();
					} catch(Exception e) {
						//ignore
					}
				}
			}

			logger.info("done E2E export job");

		} catch(Throwable e ) {
			logger.error("Error",e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}

}
