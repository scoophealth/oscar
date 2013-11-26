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

import java.util.Calendar;
import java.util.GregorianCalendar;
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
import oscar.util.StringUtils;

/**
 * An E2E scheduler object for periodically exporting all available patient summaries via HTTP POST
 * This object extends the JDK TimerTask, but applicationContextE2E.xml uses Quartz scheduling instead
 * 
 * @author Marc Dumontier, Jeremy Ho
 */
public class E2ESchedulerJob extends TimerTask {
	private static final Logger logger = MiscUtils.getLogger();
	private String e2eUrl = OscarProperties.getInstance().getProperty("E2E_URL");
	private String e2eDiff = OscarProperties.getInstance().getProperty("E2E_DIFF");
	private String e2eDiffDays = OscarProperties.getInstance().getProperty("E2E_DIFF_DAYS");

	@Override
	public void run() {
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		StringBuilder sb = new StringBuilder(255);
		Integer success = 0;
		Integer failure = 0;
		Integer diffDays = 14;
		boolean diffMode = e2eDiff != null && e2eDiff.toLowerCase().equals("on");

		try {
			List<Integer> ids = null;
			if(diffMode) {
				if(e2eDiffDays != null && StringUtils.isNumeric(e2eDiffDays)) {
					diffDays = Integer.parseInt(e2eDiffDays);
				}

				Calendar cal = GregorianCalendar.getInstance();
				cal.add(Calendar.DAY_OF_YEAR, -diffDays);
				ids = DemographicDao.getDemographicIdsOpenedSinceTime(cal.getTime());
			} else {
				ids = demographicDao.getActiveDemographicIds();
			}

			StringBuilder sbStart = reuseStringBuilder(sb);
			sbStart.append("Starting E2E export job\nE2E Target URL: ").append(e2eUrl);
			if(diffMode) {
				sbStart.append("\nExport Mode: Differential - Days: ").append(diffDays);
			} else {
				sbStart.append("\nExport Mode: Full");
			}
			logger.info(sbStart.toString());
			logger.info((Integer.toString(ids.size()).concat(" records pending")));

			for(Integer id:ids) {
				// Select Template
				E2EVelocityTemplate t = new E2EVelocityTemplate();

				// Create and load Patient data
				long startLoad = System.currentTimeMillis();
				E2EPatientExport patient = new E2EPatientExport();
				patient.setExAllTrue();
				long endLoad = startLoad;

				long startTemplate = 0;
				long endTemplate = startTemplate;
				// Load patient data and merge to template
				String output = "";
				if(patient.loadPatient(id.toString())) {
					endLoad = System.currentTimeMillis();
					if(patient.isActive()) {
						startTemplate = System.currentTimeMillis();
						output = t.export(patient);
						endTemplate = System.currentTimeMillis();
					} else {
						logger.info("Patient ".concat(id.toString()).concat(" not active - skipping"));
						continue;
					}
				} else {
					endLoad = System.currentTimeMillis();
					logger.error("Failed to load patient ".concat(id.toString()));
					failure++;
					continue;
				}

				long startPost = System.currentTimeMillis();
				long endPost = startPost;

				// Attempt to perform HTTP POST request
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(e2eUrl);

					// Assemble Multi-part Request
					StringBuilder sbFile = reuseStringBuilder(sb);
					sbFile.append("output_").append(id).append(".xml");
					ByteArrayBody body = new ByteArrayBody(output.getBytes(), "text/xml", sbFile.toString());
					MultipartEntity reqEntity = new MultipartEntity();
					reqEntity.addPart("content", body);
					httpPost.setEntity(reqEntity);

					// Send HTTP POST request
					HttpResponse response = httpclient.execute(httpPost);
					if(response != null && response.getStatusLine().getStatusCode() == 201) {
						success++;
					} else {
						logger.warn(response.getStatusLine());
						failure++;
					}
				} catch (HttpHostConnectException e) {
					logger.error("Connection to ".concat(e2eUrl).concat(" refused"));
					failure++;
				} catch (Exception e) {
					logger.error("Error", e);
					failure++;
				} finally {
					endPost = System.currentTimeMillis();
				}

				StringBuilder sbTimer = reuseStringBuilder(sb);
				sbTimer.append("[Demo: ").append(id);
				sbTimer.append("] L:").append( (endLoad - startLoad)/1000.0 );
				sbTimer.append(" T:").append( (endTemplate - startTemplate)/1000.0 );
				sbTimer.append(" P:").append( (endPost - startPost)/1000.0 );
				logger.info(sbTimer.toString());
			}

			logger.info("Done E2E export job");
		} catch(Throwable e) {
			logger.error("Error", e);
			logger.info("E2E export job aborted");
		} finally {
			sb = reuseStringBuilder(sb);
			sb.append(success).append(" records processed\n");
			sb.append(failure).append(" records failed");
			logger.info(sb.toString());
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}

	/**
	 * Helper function for reusing a StringBuilder object for efficiency
	 * Blanks out existing sb contents and returns sb
	 * 
	 * @param sb
	 */
	private StringBuilder reuseStringBuilder(final StringBuilder sb) {
		sb.delete(0, sb.length());
		return sb;
	}
}
