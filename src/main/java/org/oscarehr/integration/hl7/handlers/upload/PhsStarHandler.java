package org.oscarehr.integration.hl7.handlers.upload;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;

public class PhsStarHandler implements MessageHandler {

	Logger logger = MiscUtils.getLogger();
	
	@Override
	public String parse(String serviceName, String fileName, int fileId) {
		logger.info("received PHS/STAR message");

		org.oscarehr.integration.hl7.handlers.PhsStarHandler handler = new org.oscarehr.integration.hl7.handlers.PhsStarHandler();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line=in.readLine())!=null) {
				sb.append(line);
				sb.append("\n");			
			}
			handler.init(sb.toString());
		
			return ("success");
		}catch(Exception e) {
	        logger.error("Unexpected error.", e);
	        MessageUploader.clean(fileId);
	        throw(new RuntimeException(e));
		}
		
	}

}
