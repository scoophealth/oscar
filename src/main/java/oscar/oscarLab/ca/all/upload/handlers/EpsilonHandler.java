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

package oscar.oscarLab.ca.all.upload.handlers;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

public class EpsilonHandler implements MessageHandler {

	private static Logger logger = MiscUtils.getLogger();
	
	/*
	@SuppressWarnings("unchecked")	
	public String parse(String fileName, int fileId) {

		Utilities u = new Utilities();
		MessageUploader uploader = new MessageUploader();
		int i = 0;
		try {
			ArrayList messages = u.separateMessages(fileName);
			for (i = 0; i < messages.size(); i++) {

				String msg = (String) messages.get(i);
				uploader.routeReport("Epsilon", msg, fileId);

			}
		} catch (Exception e) {
			uploader.clean(fileId);
			logger.error("Could not upload message: ", e);			
			return null;
		}
		return ("success");

	}
	*/
	@Override
    public String parse(LoggedInInfo loggedInInfo,String serviceName, String fileName, int fileId, String ipAddr) {
		int i = 0;
		try {

			StringBuilder audit = new StringBuilder();
			ArrayList<String> messages = Utilities.separateMessages(fileName);
			for (i = 0; i < messages.size(); i++) {

				String msg = messages.get(i);
				String auditLine = MessageUploader.routeReport(loggedInInfo, serviceName, "EPSILON", msg, fileId) + "\n";
				audit.append(auditLine);

			}
			logger.debug("Parsed OK");

			return (audit.toString());

		} catch (Exception e) {
			MessageUploader.clean(fileId);
			logger.error("Could not parse message", e);
			return null;
		}
    }
}
