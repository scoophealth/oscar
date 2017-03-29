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

import java.net.NoRouteToHostException;
import java.util.Calendar;
import java.util.Collections;
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
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.e2e.director.E2ECreator;
import org.oscarehr.e2e.util.EverestUtils;
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
	private static final String e2eUrl = OscarProperties.getInstance().getProperty("E2E_URL");
	private static final String e2eDiff = OscarProperties.getInstance().getProperty("E2E_DIFF");
	private static final String e2eDiffDays = OscarProperties.getInstance().getProperty("E2E_DIFF_DAYS");
	private static final Boolean diffMode = e2eDiff != null && e2eDiff.toLowerCase().equals("on");

	@Override
	public void run() {
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		OscarLogDao oscarLogDao = SpringUtils.getBean(OscarLogDao.class);
		StringBuilder sb = new StringBuilder(255);
		Integer success = 0;
		Integer failure = 0;
		Integer skipped = 0;
		Integer diffDays = 14;
		List<Integer> ids = null;

		try {
			// Gather demographic numbers for specified mode of operation
			if(diffMode) {
				if(e2eDiffDays != null && StringUtils.isNumeric(e2eDiffDays)) {
					diffDays = Integer.parseInt(e2eDiffDays);
				}

				Calendar cal = GregorianCalendar.getInstance();
				cal.add(Calendar.DAY_OF_YEAR, -diffDays);
				ids = oscarLogDao.getDemographicIdsOpenedSinceTime(cal.getTime());
			} else {
				ids = demographicDao.getActiveDemographicIds();
			}
			if(ids != null) Collections.sort(ids);

			// Log Start Header
			StringBuilder sbStart = reuseStringBuilder(sb);
			sbStart.append("Starting E2E export job\nE2E Target URL: ").append(e2eUrl);
			if(diffMode) {
				sbStart.append("\nExport Mode: Differential - Days: ").append(diffDays);
			} else {
				sbStart.append("\nExport Mode: Full");
			}
			logger.info(sbStart.toString());
			StringBuilder sbStartRec = reuseStringBuilder(sb);
			sbStartRec.append(ids.size()).append(" records pending");
			if(ids.size() > 0) {
				sbStartRec.append("\nRange: ").append(ids.get(0)).append(" - ").append(ids.get(ids.size()-1));
				sbStartRec.append(", Median: ").append(ids.get((ids.size()-1)/2));
			}
			logger.info(sbStartRec.toString());

			Long startJob = System.currentTimeMillis();
			Long endJob = startJob;

			for(Integer id:ids) {
				Long startLoad = System.currentTimeMillis();
				// Populate Clinical Document
				ClinicalDocument clinicalDocument = E2ECreator.createEmrConversionDocument(id);
				if(clinicalDocument == null) {
					logger.info("[Demo ".concat(id.toString()).concat("] Not active or failed to populate"));
				}
				Long endLoad = System.currentTimeMillis();

				Long startGenerate = System.currentTimeMillis();
				// Output Clinical Document as String
				String output = EverestUtils.generateDocumentToString(clinicalDocument, true);
				Long endGenerate = System.currentTimeMillis();

				Long startPost = System.currentTimeMillis();
				Long endPost = startPost;

				// Attempt to perform HTTP POST request
				try {
					if(output != null) {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost(e2eUrl);

						// Assemble Multi-part Request
						StringBuilder sbFile = reuseStringBuilder(sb);
						sbFile.append("Record_").append(id).append(".xml");
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
					}
				} catch (HttpHostConnectException e) {
					logger.error("Connection to ".concat(e2eUrl).concat(" refused"));
					failure++;
				} catch (NoRouteToHostException e) {
					logger.error("Can't resolve route to ".concat(e2eUrl));
					failure++;
				} catch (Exception e) {
					logger.error("Error", e);
					failure++;
				} finally {
					endPost = System.currentTimeMillis();
				}

				// Log Record completion + benchmarks
				StringBuilder sbTimer = reuseStringBuilder(sb);
				sbTimer.append("[Demo: ").append(id);
				sbTimer.append("] L:").append( (endLoad - startLoad)/1000.0 );
				sbTimer.append(" G:").append( (endGenerate - startGenerate)/1000.0 );
				sbTimer.append(" P:").append( (endPost - startPost)/1000.0 );
				logger.info(sbTimer.toString());
			}

			endJob = System.currentTimeMillis();
			logger.info("Done E2E export job (" + convertTime(endJob - startJob) + ")");
		} catch(Exception e) {
			logger.error("Error", e);
			logger.info("E2E export job aborted");
		} finally {
			// Log final record counts
			Integer unaccounted = ids.size() - success - failure - skipped;
			sb = reuseStringBuilder(sb);
			sb.append(success).append(" records processed");
			if(failure > 0) sb.append("\n").append(failure).append(" records failed");
			if(skipped > 0) sb.append("\n").append(skipped).append(" records skipped");
			if(unaccounted > 0) sb.append("\n").append(unaccounted).append(" records unaccounted");
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

	/**
	 * Helper function to display time in a nicer readable format
	 * 
	 * @param time
	 * @return string
	 */
	private String convertTime(long time) {
		Long ms = time;
		Long seconds = time / 1000L;
		Long minutes = time / 60000L;
		Long hours = time / 3600000L;
		return String.format("%02d:%02d:%02d.%03d", hours%24, minutes%60, seconds%60, ms%1000);
	}
}
