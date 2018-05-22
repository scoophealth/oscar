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

package oscar.oscarLab.ca.all.upload.handlers;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.upload.RouteReportResults;


public class ExcellerisOntarioHandler implements MessageHandler {

	Logger logger = Logger.getLogger(ExcellerisOntarioHandler.class);

	public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {
		Document doc = null;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(new FileInputStream(fileName));
		} catch (Exception e) {
			logger.error("Could not parse Excelleris ON message", e);
		}

		RouteReportResults routeResults;
		StringBuilder audit = new StringBuilder();
		
		if (doc != null) {
			int i = 0;
			try {
				Node messageSpec = doc.getFirstChild();
				NodeList messages = messageSpec.getChildNodes();
				for (i = 0; i < messages.getLength(); i++) {

					String hl7Body = messages.item(i).getFirstChild().getTextContent();
					MessageUploader.routeReport(loggedInInfo, serviceName, "ExcellerisON", hl7Body, fileId);
				}
			} catch (Exception e) {
				logger.error("Could not upload Excelleris Ontario message", e);
				MiscUtils.getLogger().error("Error", e);
				MessageUploader.clean(fileId);
				return null;
			}
			return ("success");
		} else {
			return null;
		}

	}

}
