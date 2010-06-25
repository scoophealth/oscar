/*
 * CMLHandler.java
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

public class CMLHandler implements MessageHandler {

	Logger logger = Logger.getLogger(CMLHandler.class);

	public String parse(String serviceName, String fileName, int fileId) {

		int i = 0;
		try {
			ArrayList<String> messages = Utilities.separateMessages(fileName);
			for (i = 0; i < messages.size(); i++) {

				String msg = messages.get(i);
				MessageUploader.routeReport(serviceName, "CML", msg, fileId);

			}
		} catch (Exception e) {
			MessageUploader.clean(fileId);
			logger.error("Could not upload message: ", e);
			e.printStackTrace();
			return null;
		}
		return ("success");

	}

}