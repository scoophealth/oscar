/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */

package oscar.ocan.task;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.ocan.OcanXmlAdapterFactory;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.ShutdownException;

import oscar.ocan.service.OcanDataProcessor;
import oscar.ocan.service.OcanDataProcessor.OcanProcess;

public class OcanSubmissionTask extends TimerTask {

	private static final Log log = LogFactory.getLog(OcanSubmissionTask.class);

	private GenericIntakeManager genericIntakeManager;
	private OcanDataProcessor ocanDataProcessor;
	private OcanXmlAdapterFactory adapterFactory = new OcanXmlAdapterFactory();

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
		LoggedInInfo.setLoggedInInfoToCurrentClassAndMethod();
		try {
			log.info("start ocan submission task");

			List<Map<String, Intake>> intakes = genericIntakeManager.getIntakeListforOcan();

			if (intakes == null || intakes.size() == 0) {
				log.warn("getIntakeListforOcan() returned null or empty list");
				return;
			}

			OcanProcess process = ocanDataProcessor.createOcanProcess();

			for (Map<String, Intake> intakeMap : intakes) {
				MiscUtils.checkShutdownSignaled();

				try {
					if (useTestData) {
						ocanDataProcessor.createOcanRecord(process,
								new FileInputStream(testDataPath + "/client.xml"),
								new FileInputStream(testDataPath + "/staff.xml"),
								Integer.MAX_VALUE);
					} else {
						ocanDataProcessor.createOcanRecord(process,
								new ByteArrayInputStream(toXml(intakeMap, "client").getBytes()),
								new ByteArrayInputStream(toXml(intakeMap, "staff").getBytes()),
								intakeMap.get("client").getClientId());
					}

				} catch (Exception e) {
					log.error("createOcanRecord() thrown an exception, skipping the record.", e);
					continue;
				}
			}

			ocanDataProcessor.finishOcanProcess(process);

			log.info("finish ocan submission task");
		} catch (ShutdownException e) {
			log.debug("OcanSubmissionTask noticed shutdown signaled.");
		} catch (FileNotFoundException e) {
			log.error("finishOcanProcess() thrown an exception, terminating the submission.", e);
		} catch (JAXBException e) {
			log.error("finishOcanProcess() thrown an exception, terminating the submission.", e);
		} finally {
			LoggedInInfo.loggedInInfo.remove();
			DbConnectionFilter.releaseThreadLocalDbConnection();
		}
	}

	private String toXml(Map<String, Intake> intakeMap, String type) {
		StringBuilder xml = new StringBuilder();
		Intake intake = intakeMap.get(type);
		if ("client".equals(type)) {
			toClientXml(xml, intake, intake.getNode());
		} else {
			toStaffXml(xml, intake, intake.getNode());
		}
		return xml.toString();
	}

	private void toClientXml(StringBuilder builder, Intake intake, IntakeNode node) {
		if (node == null)
			return;
		IntakeNodeHtmlAdapter htmlAdapter = adapterFactory.getOcanClientXmlAdapter(0, node, intake);

		builder.append(htmlAdapter.getPreBuilder());

		for (IntakeNode child : node.getChildren()) {
			toClientXml(builder, intake, child);
		}

		builder.append(htmlAdapter.getPostBuilder());
	}

	private void toStaffXml(StringBuilder builder, Intake intake, IntakeNode node) {
		if (node == null)
			return;
		IntakeNodeHtmlAdapter htmlAdapter = adapterFactory.getOcanStaffXmlAdapter(0, node, intake);

		builder.append(htmlAdapter.getPreBuilder());

		for (IntakeNode child : node.getChildren()) {
			toStaffXml(builder, intake, child);
		}

		builder.append(htmlAdapter.getPostBuilder());
	}

	public void setUseTestData(boolean useTestData) {
		this.useTestData = useTestData;
	}

	public void setTestDataPath(String testDataPath) {
		this.testDataPath = testDataPath;
	}
}