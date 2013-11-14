/**
 * Copyright (c) 2013. Department of Family Practice, University of British Columbia. All Rights Reserved.
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
 * Department of Family Practice
 * Faculty of Medicine
 * University of British Columbia
 * Vancouver, Canada
 */
package org.oscarehr.common.service;

import java.util.List;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.exports.e2e.E2EPatientExport;
import org.oscarehr.exports.e2e.E2EVelocityTemplate;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

/**
 * An E2E scheduler object for periodically exporting all available patient summaries via HTTP POST
 * This object extends the JDK TimerTask, but applicationContextE2E.xml uses Quartz scheduling instead
 * 
 * @author Marc Dumontier, Jeremy Ho
 */
public class E2ESchedulerJob extends TimerTask {
	private static final Logger logger = MiscUtils.getLogger();
	private String e2eUrl = OscarProperties.getInstance().getProperty("E2E_URL");

	@Override
	public void run() {
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		Integer success = 0;

		try {
			List<Integer> ids = demographicDao.getActiveDemographicIds();

			logger.info("Starting E2E export job\nE2E Target URL: ".concat(e2eUrl).concat("\n").concat(Integer.toString(ids.size())).concat(" records pending"));

			for(Integer id:ids) {
				// Select Template
				E2EVelocityTemplate t = new E2EVelocityTemplate();

				// Create and load Patient data
				logger.info("[Demo: ".concat(id.toString()).concat("]"));
				E2EPatientExport patient = new E2EPatientExport();
				patient.setExAllTrue();

				// Load patient data and merge to template
				String output = "";
				if(patient.loadPatient(id.toString())) {
					if(patient.isActive()) {
						output = t.export(patient);
					} else {
						logger.info("Patient ".concat(id.toString()).concat(" not active - skipping"));
						continue;
					}
				} else {
					logger.error("Failed to load patient ".concat(id.toString()));
					continue;
				}

				// Attempt to perform HTTP POST request
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(e2eUrl);

					// Assemble Multi-part Request
					ByteArrayBody body = new ByteArrayBody(output.getBytes(), "text/xml", "output_".concat(id.toString()).concat(".xml"));
					MultipartEntity reqEntity = new MultipartEntity();
					reqEntity.addPart("content", body);
					httpPost.setEntity(reqEntity);

					// Send HTTP POST request
					HttpResponse response = httpclient.execute(httpPost);
					if(response != null && response.getStatusLine().getStatusCode() == 201) {
						success++;
					} else {
						logger.warn(response.getStatusLine());
					}
				} catch (HttpHostConnectException e) {
					logger.error("Connection to ".concat(e2eUrl).concat(" refused"));
				} catch (Exception e) {
					logger.error("Error", e);
				}
			}

			logger.info("Done E2E export job\n".concat(success.toString()).concat(" records processed"));
		} catch(Throwable e) {
			logger.error("Error", e);
			logger.info("E2E export job aborted\n".concat(success.toString()).concat(" records processed"));
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}
}
