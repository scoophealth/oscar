package oscar.oscarLab.ca.all.upload.handlers;

import java.util.ArrayList;

import org.apache.log4j.Logger;
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
    public String parse(String serviceName, String fileName, int fileId) {
		int i = 0;
		try {

			StringBuilder audit = new StringBuilder();
			ArrayList<String> messages = Utilities.separateMessages(fileName);
			for (i = 0; i < messages.size(); i++) {

				String msg = messages.get(i);
				String auditLine = MessageUploader.routeReport(serviceName, "EPSILON", msg, fileId) + "\n";
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
