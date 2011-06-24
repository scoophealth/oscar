/*
 * HL7Handler
 * Upload handler
 * 
 */
package oscar.oscarLab.ca.all.upload.handlers;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

/**
 * 
 */
public class OLISHL7Handler implements MessageHandler {

	Logger logger = Logger.getLogger(OLISHL7Handler.class);

	public OLISHL7Handler() {
		logger.info("NEW OLISHL7Handler UPLOAD HANDLER instance just instantiated. ");
	}

	public String parse(String serviceName, String fileName, int fileId) {		
		int i = 0;
		try {
			ArrayList<String> messages = Utilities.separateMessages(fileName);
			for (i = 0; i < messages.size(); i++) {
				String msg = messages.get(i);
				MessageUploader.routeReport(serviceName,"OLIS_HL7", msg.replace("\\E\\", "\\SLASHHACK\\").replace("µ", "\\MUHACK\\").replace("\\H\\", "\\.H\\").replace("\\N\\", "\\.N\\"), fileId);
				
			}
			logger.info("Parsed OK");
		} catch (Exception e) {
			MessageUploader.clean(fileId);
			logger.error("Could not upload message", e);
			return null;
		}
		return ("success");
	}
}