/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.ocan.task;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.MiscUtilsOld;
import org.oscarehr.util.ShutdownException;

import oscar.ocan.service.OcanDataProcessor;
import oscar.ocan.service.OcanDataProcessor.OcanProcess;

public class OcanSubmissionTask extends TimerTask {

	private static final Logger logger = MiscUtils.getLogger();

	private GenericIntakeManager genericIntakeManager;
	private OcanDataProcessor ocanDataProcessor;

	private boolean useTestData;
	private String testDataPath;

	public void setGenericIntakeManager(GenericIntakeManager genericIntakeManager) {
		this.genericIntakeManager = genericIntakeManager;
	}

	public void setOcanDataProcessor(OcanDataProcessor ocanDataProcessor) {
		this.ocanDataProcessor = ocanDataProcessor;
	}

	@Override
	public void run() {
		try {
			logger.info("start ocan submission task");

			OcanProcess process = ocanDataProcessor.createOcanProcess();

			if (useTestData) {

				ocanDataProcessor.createOcanRecord(process,
						new FileInputStream(testDataPath + "/client.xml"),
						new FileInputStream(testDataPath + "/staff.xml"),
						"1001");
			} else {

				Calendar after  = new GregorianCalendar();
				after.roll(Calendar.MONTH, -1);

				List<Map<String, String>> intakes = genericIntakeManager.getIntakeListforOcan(after);

				if (intakes == null || intakes.size() == 0) {
					logger.warn("getIntakeListforOcan() returned null or empty list - no data for submission.");
					return;
				}

				for (Map<String, String> intakeMap : intakes) {
					MiscUtilsOld.checkShutdownSignaled();

					try {
						ocanDataProcessor.createOcanRecord(process,
								new ByteArrayInputStream(intakeMap.get("client").getBytes()),
								new ByteArrayInputStream(intakeMap.get("staff").getBytes()),
								intakeMap.get("clientId"));

					} catch (Exception e) {
						logger.error("createOcanRecord() thrown an exception, skipping the record.", e);
						continue;
					}
				}
			}

			ocanDataProcessor.finishOcanProcess(process);

			logger.info("finish ocan submission task");

		} catch (ShutdownException e) {
			logger.debug("OcanSubmissionTask noticed shutdown signaled.");
		} catch (FileNotFoundException e) {
			logger.error("finishOcanProcess() thrown an exception, terminating the submission.", e);
		} catch (JAXBException e) {
			logger.error("finishOcanProcess() thrown an exception, terminating the submission.", e);
		} catch (NumberFormatException e) {
			logger.error("finishOcanProcess() thrown an exception, terminating the submission.", e);
		} catch (ParseException e) {
			logger.error("finishOcanProcess() thrown an exception, terminating the submission.", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}

	public void setUseTestData(boolean useTestData) {
		this.useTestData = useTestData;
	}

	public void setTestDataPath(String testDataPath) {
		this.testDataPath = testDataPath;
	}
}
