/*
 * MDSHandler.java
 *
 * Created on May 23, 2007, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all.upload.handlers;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

public class MDSHandler implements MessageHandler {

	Logger logger = Logger.getLogger(MDSHandler.class);

	public String parse(String serviceName, String fileName, int fileId) {

		int i = 0;
		try {

			StringBuilder audit = new StringBuilder();
			ArrayList<String> messages = Utilities.separateMessages(fileName);
			for (i = 0; i < messages.size(); i++) {

				String msg = messages.get(i);
				String auditLine = MessageUploader.routeReport(serviceName, "MDS", msg, fileId) + "\n";
				audit.append(auditLine);

			}
			logger.info("Parsed OK");

			return (audit.toString());

		} catch (Exception e) {
			MessageUploader.clean(fileId);
			logger.error("Could not parse message", e);
			return null;
		}

	}

}