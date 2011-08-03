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
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

public class MDSHandler implements MessageHandler {

	Logger logger = Logger.getLogger(MDSHandler.class);
	Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao)SpringUtils.getBean("hl7TextInfoDao");
	

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

	private boolean isDuplicate(String msg) {
		//OLIS requirements - need to see if this is a duplicate
		oscar.oscarLab.ca.all.parsers.MessageHandler h = Factory.getHandler("MDS", msg);
		//if final		
		if(h.getOrderStatus().equals("F")) {
			String fullAcc = h.getAccessionNum();
			
			//do we have this?
			List<Hl7TextInfo> dupResults = hl7TextInfoDao.searchByAccessionNumber(fullAcc);
			for(Hl7TextInfo dupResult:dupResults) {
				if(dupResult.equals(fullAcc)) {
					return true;
				}
				
			}		
		}
		return false;	
	}
}